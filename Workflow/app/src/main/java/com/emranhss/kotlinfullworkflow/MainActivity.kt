package com.emranhss.kotlinfullworkflow

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.emranhss.kotlinfullworkflow.data.model.network.RetrofitClient
import com.emranhss.kotlinfullworkflow.data.model.repository.UserRepository

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // Force light mode programmatically(ei kajta themes.xml(night file er vetor) a o kora jay tobe seta onno code likhte hoy)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// 1. Initialize Repository and ViewModel
        val repository = UserRepository(RetrofitClient.apiService)
        viewModel = UserViewModel(repository)

        // 2. Initialize UI Components (Find views by ID)
        val nameET = findViewById<EditText>(R.id.nameEditText)
        val emailET = findViewById<EditText>(R.id.emailEditText)
        val saveBtn = findViewById<Button>(R.id.saveButton)

        // 3. Set up Observer for Status Messages
        // This listens for any changes in statusMessage within the ViewModel
        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }

// 4. Handle Save Button Click
        saveBtn.setOnClickListener {
            val name = nameET.text.toString().trim()
            val email = emailET.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // Call ViewModel function to save data to API
                viewModel.createAccount(name, email)
            } else {
                // Show local validation error
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}