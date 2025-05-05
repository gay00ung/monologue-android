package net.ifmain.monologue.data.di

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import net.ifmain.monologue.BuildConfig
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.preference.UserPreferenceManager
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCookieJar(
        @ApplicationContext ctx: Context
    ): CookieJar = PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(ctx)
    )

    @Provides @Singleton
    fun provideOkHttpClient(
        cookieJar: CookieJar,
        userPrefs: UserPreferenceManager
    ): OkHttpClient {
        val saveCookieInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cookies = response.headers("Set-Cookie")
            if (cookies.isNotEmpty()) {
                val cookieString = cookies.joinToString(";")
                kotlinx.coroutines.runBlocking {
                    userPrefs.saveCookie(cookieString)
                }
            }
            response
        }

        val addCookieInterceptor = Interceptor { chain ->
            val orig = chain.request()
            val saved = kotlinx.coroutines.runBlocking { userPrefs.cookieFlow.firstOrNull() }
            val req = if (!saved.isNullOrBlank()) {
                orig.newBuilder()
                    .header("Cookie", saved)
                    .build()
            } else orig
            chain.proceed(req)
        }

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(saveCookieInterceptor)
            .addInterceptor(addCookieInterceptor)
            .addNetworkInterceptor { chain ->
                val req = chain.request()
                val res = chain.proceed(req)
                Log.d("NET-REQ", "${req.method} ${req.url}")
                Log.d("NET-RES", "Set-Cookie: ${res.headers("Set-Cookie")}")
                res
            }
            .build()
    }

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDiaryApi(
        retrofit: Retrofit
    ): DiaryApi = retrofit.create(DiaryApi::class.java)

    @Provides
    @Singleton
    fun provideUserPreferenceManager(
        @ApplicationContext ctx: Context
    ): UserPreferenceManager = UserPreferenceManager(ctx)
}