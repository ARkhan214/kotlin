package com.emranhss.mysplashpractice

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false
        }

//        Thread use is wrong way
//        Thread.sleep(2000)
//        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//
//        installSplashScreen()
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            // Next screen / HomeActivity
//        }, 1500)
//    }
}