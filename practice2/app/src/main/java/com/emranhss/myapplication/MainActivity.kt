package com.emranhss.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

//--------------------------listOf changeable na
//        val dataList = listOf(
//            Organization("ARANYA CABLE NETWORK", "Jhenaidah", R.drawable.ic_logo1),
//            Organization("RAMDIA DIGITAL CABLE NETWORK", "Gopalganj", R.drawable.ic_logo2),
//            Organization("GAMMA INNOVATION LTD", "Jamalpur", R.drawable.ic_logo3),
//            Organization("REZVY CABLE NETWORK", "Faridpur", R.drawable.ic_logo4),
//            Organization("ASADUZZAMAN CABLE NETWORK", "Bokshiganj, Jamalpur", R.drawable.ic_logo5)
//        )

//-----------------------------mutableListof changable
        val dataList = mutableListOf(
            Organization("ARANYA CABLE NETWORK", "Jhenaidah", R.drawable.ic_logo1),
            Organization("RAMDIA DIGITAL CABLE NETWORK", "Gopalganj", R.drawable.ic_logo2),
            Organization("GAMMA INNOVATION LTD", "Jamalpur", R.drawable.ic_logo3),
            Organization("REZVY CABLE NETWORK", "Faridpur", R.drawable.ic_logo4),
            Organization("ASADUZZAMAN CABLE NETWORK", "Bokshiganj, Jamalpur", R.drawable.ic_logo5)
        )

        //
        val adapter = OrgAdapter(dataList)
        recyclerView.adapter = adapter
    }
}