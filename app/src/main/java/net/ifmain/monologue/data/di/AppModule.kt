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
import net.ifmain.monologue.BuildConfig
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.preference.UserPreferenceManager
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cookieJar: CookieJar
    ): OkHttpClient {
        val loggingInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            Log.d("NET-REQ", "${request.method} ${request.url}")
            Log.d("NET-REQ-HEADERS", request.headers.toString())
            Log.d("NET-RES", "Set-Cookie: ${response.headers("Set-Cookie")}")
            return@Interceptor response
        }

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDiaryApi(retrofit: Retrofit): DiaryApi =
        retrofit.create(DiaryApi::class.java)

    @Provides
    @Singleton
    fun provideUserPreferenceManager(
        @ApplicationContext context: Context
    ): UserPreferenceManager = UserPreferenceManager(context)

}
