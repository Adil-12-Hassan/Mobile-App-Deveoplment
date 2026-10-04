package com.example.taskflow

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var titleLayout: TextInputLayout
    private lateinit var titleInput: TextInputEditText
    private lateinit var courseInput: TextInputEditText
    private lateinit var dueDateLayout: TextInputLayout
    private lateinit var dueDateInput: TextInputEditText
    private lateinit var priorityGroup: RadioGroup
    private var selectedDueDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        attachToolbar(findViewById<MaterialToolbar>(R.id.toolbar), R.string.add_title, true)

        titleLayout = findViewById(R.id.titleLayout)
        titleInput = findViewById(R.id.titleInput)
        courseInput = findViewById(R.id.courseInput)
        dueDateLayout = findViewById(R.id.dueDateLayout)
        dueDateInput = findViewById(R.id.dueDateInput)
        priorityGroup = findViewById(R.id.priorityGroup)

        selectedDueDate = savedInstanceState?.getString(KEY_DUE_DATE)

        titleInput.doAfterTextChanged { titleLayout.error = null }
        dueDateInput.setOnClickListener { pickDueDate() }

        findViewById<MaterialButton>(R.id.cancelButton).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener { saveTask() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_DUE_DATE, selectedDueDate)
    }

    private fun pickDueDate() {
        val today = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val iso = TaskDates.toIso(year, month, day)
                selectedDueDate = iso
                dueDateInput.setText(TaskDates.toDisplay(iso))
                dueDateLayout.error = null
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveTask() {
        val title = titleInput.text?.toString()?.trim().orEmpty()
        val course = courseInput.text?.toString()?.trim().orEmpty()
        val dueDate = selectedDueDate

        if (title.isEmpty()) {
            titleLayout.error = getString(R.string.error_title_required)
            return
        }
        if (dueDate == null) {
            dueDateLayout.error = getString(R.string.error_due_required)
            return
        }

        val priority = when (priorityGroup.checkedRadioButtonId) {
            R.id.priorityHigh -> Priorities.HIGH
            R.id.priorityLow -> Priorities.LOW
            else -> Priorities.MEDIUM
        }

        val result = Intent()
        result.putExtra(IntentKeys.EXTRA_TITLE, title)
        result.putExtra(IntentKeys.EXTRA_COURSE, course)
        result.putExtra(IntentKeys.EXTRA_PRIORITY, priority)
        result.putExtra(IntentKeys.EXTRA_DUE_DATE, dueDate)
        setResult(RESULT_OK, result)
        finish()
    }

    private companion object {
        const val KEY_DUE_DATE = "selectedDueDate"
    }
}