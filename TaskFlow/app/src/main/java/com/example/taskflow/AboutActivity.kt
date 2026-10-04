package com.example.taskflow

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        attachToolbar(findViewById<MaterialToolbar>(R.id.toolbar), R.string.about_title, true)

        findViewById<MaterialButton>(R.id.emailButton).setOnClickListener { sendEmail() }
        findViewById<MaterialButton>(R.id.docsButton).setOnClickListener { openDocs() }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDocs() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.android_docs_url)))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}