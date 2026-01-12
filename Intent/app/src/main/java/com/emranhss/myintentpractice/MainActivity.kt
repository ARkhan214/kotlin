package com.emranhss.myintentpractice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emranhss.myintentpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/chrome/"))
            startActivity(intent)
        }


//        binding.openPhoneCall.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("01986823623")
//            startActivity(intent)
//        }

        binding.openPhoneCall.setOnClickListener {
            val phoneNumber = "+8801986823623"   // note: country code without space
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")  // <-- tel: scheme
            startActivity(intent)
        }


        binding.openCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }


        binding.openShear.setOnClickListener {
            val text = binding.editTextText.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Shear Data"))
        }


        fun cleanText() {
            binding.editTextText.text.clear()
        }

        binding.clearText.setOnClickListener {
            cleanText()
        }

    }
}