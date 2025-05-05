package net.ifmain.monologue.data.di

import android.content.Context
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
    ): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
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