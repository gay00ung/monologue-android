package net.ifmain.monologue.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryApi {
    @POST("users")
    suspend fun postUser(@Body user: UserEntryDto): Response<Unit>

    @POST("diary")
    suspend fun postDiary(@Body entry: DiaryEntryDto): Response<Unit>

    @GET("diary")
    suspend fun getDiaries(@Query("user_id") userId: String): List<DiaryEntryDto>
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("http://localhost:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: DiaryApi = retrofit.create(DiaryApi::class.java)
