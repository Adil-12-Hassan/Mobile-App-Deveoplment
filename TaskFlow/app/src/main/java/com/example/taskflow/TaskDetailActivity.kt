package com.example.taskflow

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.appbar.MaterialToolbar

class TaskDetailActivity : AppCompatActivity() {
    private var taskTitle = ""
    private var taskCourse = ""
    private var taskPriority = ""
    private var taskDueDate = ""
    private var taskDone = false
    private var taskPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        attachToolbar(findViewById<MaterialToolbar>(R.id.toolbar), R.string.detail_title, true)

        taskTitle = intent.getStringExtra(IntentKeys.EXTRA_TITLE).orEmpty()
        taskCourse = intent.getStringExtra(IntentKeys.EXTRA_COURSE).orEmpty()
        taskPriority = intent.getStringExtra(IntentKeys.EXTRA_PRIORITY).orEmpty()
        taskDueDate = intent.getStringExtra(IntentKeys.EXTRA_DUE_DATE).orEmpty()
        taskDone = intent.getBooleanExtra(IntentKeys.EXTRA_DONE, false)
        taskPosition = intent.getIntExtra(IntentKeys.EXTRA_POSITION, -1)

        findViewById<TextView>(R.id.detailTitle).text = taskTitle
        findViewById<TextView>(R.id.detailCourse).text = taskCourse

        val priorityView = findViewById<TextView>(R.id.detailPriority)
        priorityView.text = priorityLabel(taskPriority)
        priorityView.setTextColor(priorityColor(taskPriority))

        findViewById<TextView>(R.id.detailDueDate).text = TaskDates.toDisplay(taskDueDate)
        findViewById<TextView>(R.id.detailStatus).text = getString(
            if (taskDone) R.string.status_done else R.string.status_pending
        )

        findViewById<ImageView>(R.id.moreButton).setOnClickListener { showPopupMenu(it) }
    }

    private fun showPopupMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_task_popup, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_share -> {
                    shareTask()
                    true
                }
                R.id.popup_mark_done -> {
                    returnAction(IntentKeys.ACTION_DONE)
                    true
                }
                R.id.popup_delete -> {
                    returnAction(IntentKeys.ACTION_DELETE)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun returnAction(action: String) {
        val result = Intent()
        result.putExtra(IntentKeys.EXTRA_ACTION, action)
        result.putExtra(IntentKeys.EXTRA_POSITION, taskPosition)
        setResult(RESULT_OK, result)
        finish()
    }

    private fun shareTask() {
        val status = getString(if (taskDone) R.string.status_done else R.string.status_pending)
        val summary = getString(
            R.string.share_text,
            taskTitle,
            taskCourse,
            priorityLabel(taskPriority),
            TaskDates.toDisplay(taskDueDate),
            status
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, summary)
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}