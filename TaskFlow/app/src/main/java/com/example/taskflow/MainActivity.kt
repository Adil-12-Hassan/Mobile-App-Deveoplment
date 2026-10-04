package com.example.taskflow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var pendingCountView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        attachToolbar(findViewById<MaterialToolbar>(R.id.toolbar), R.string.app_name, false)

        pendingCountView = findViewById(R.id.pendingCountView)

        findViewById<MaterialButton>(R.id.myTasksButton).setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra(IntentKeys.STUDENT_NAME, getString(R.string.student_name))
            startActivity(intent)
        }
        findViewById<MaterialButton>(R.id.aboutButton).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val pending = TaskRepository.tasks.count { !it.done }
        pendingCountView.text = resources.getQuantityString(R.plurals.pending_count, pending, pending)
    }
}