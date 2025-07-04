package net.ifmain.monologue.data.api

import net.ifmain.monologue.data.model.DiaryEntryDto
import net.ifmain.monologue.data.model.EmailCheckResponse
import net.ifmain.monologue.data.model.UserDto
import net.ifmain.monologue.data.model.UserEntryDto
import net.ifmain.monologue.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface DiaryApi {
    @POST("/api/users/signup")
    suspend fun postSignUp(@Body user: UserEntryDto): Response<UserResponse>

    @POST("api/users/login")
    suspend fun postSignIn(@Body request: UserDto): Response<UserResponse>

    @GET("api/users/check-email")
    suspend fun checkEmailAvailability(@Query("email") email: String): Response<EmailCheckResponse>

    @GET("api/users/logout")
    suspend fun postLogout(): Response<Unit>

    @DELETE("api/users/withdraw")
    suspend fun deleteUser(): Response<Unit>

    @GET("/api/users/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @POST("/api/diary")
    suspend fun postDiary(@Body entry: DiaryEntryDto): Response<Unit>

    @PUT("/api/diary")
    suspend fun updateDiary(@Body entry: DiaryEntryDto): Response<Unit>

    @GET("/api/diary")
    suspend fun getDiaries(@Query("user_id") userId: String): List<DiaryEntryDto>
}