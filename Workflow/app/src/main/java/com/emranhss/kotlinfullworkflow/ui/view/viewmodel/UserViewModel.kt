package com.emranhss.kotlinfullworkflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emranhss.kotlinfullworkflow.data.model.UserRequest
import com.emranhss.kotlinfullworkflow.data.model.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    //For show tost massage(i have to  hold message in a variable)
    val statusMessage = MutableLiveData<String>()
    fun createAccount(name: String, email: String) {
        val user = UserRequest(name, email)

        viewModelScope.launch {
            try {
                val response = repository.saveUserToServer(user)
                if (response.isSuccessful) {
                    statusMessage.value = "Success: ${response.body()?.message}"
                    println("Success: ${response.body()?.message}")
                } else {
                    statusMessage.value = "Failed: Check Internet or IP Address"
                    println("Error Code: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Network Failed: ${e.message}")
            }
        }
    }
}