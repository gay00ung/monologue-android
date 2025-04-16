package net.ifmain.monologue.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryApi {
    @POST("/api/users/signup")
    suspend fun postSignUp(@Body user: UserEntryDto): Response<UserResponse>

    @POST("api/users/login")
    suspend fun postSignIn(@Body request: UserDto): Response<UserResponse>


    @POST("/api/diary")
    suspend fun postDiary(@Body entry: DiaryEntryDto): Response<Unit>

    @GET("/api/diary")
    suspend fun getDiaries(@Query("user_id") userId: String): List<DiaryEntryDto>
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.45.196:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: DiaryApi = retrofit.create(DiaryApi::class.java)
