package com.example.myredirect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val google = findViewById<Button>(R.id.google)
        google.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.google.com")
            startActivity(intent)
        }

        val call = findViewById<Button>(R.id.call)
        call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+923281511293")
            startActivity(intent)
        }

        val email = findViewById<Button>(R.id.email)
        email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:syedadilhassan06@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hello")
            intent.putExtra(Intent.EXTRA_TEXT, "How are you?")
            startActivity(intent)
        }

        val map = findViewById<Button>(R.id.map)
        map.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q=GCUF")
            startActivity(intent)
        }

        val website = findViewById<Button>(R.id.website)
        website.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://code-with-hassan-phi.vercel.app")
            startActivity(intent)
        }
    }
}
