package com.emranhss.kotlinfullworkflow.data.model

// Post data to api
data class UserRequest(
    val name: String,
    val email: String
)

// for api response
data class UserResponse(
    val id: Int? = null,
    val message: String? = null
)