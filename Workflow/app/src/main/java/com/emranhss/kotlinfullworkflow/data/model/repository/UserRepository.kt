package com.emranhss.kotlinfullworkflow.data.model.repository

import com.emranhss.kotlinfullworkflow.data.model.UserRequest
import com.emranhss.kotlinfullworkflow.data.model.network.ApiService

class UserRepository(private val apiService: ApiService) {
    suspend fun saveUserToServer(user: UserRequest) = apiService.saveUser(user)
}