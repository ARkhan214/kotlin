package com.emranhss.kotlinfullworkflow.data.model.network

import com.emranhss.kotlinfullworkflow.data.model.UserRequest
import com.emranhss.kotlinfullworkflow.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/save")
    suspend fun saveUser(@Body user: UserRequest): Response<UserResponse>
}