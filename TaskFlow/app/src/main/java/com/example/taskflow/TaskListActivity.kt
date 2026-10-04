package com.example.taskflow

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class TaskListActivity : AppCompatActivity() {

    private enum class SortMode { NONE, PRIORITY, DUE_DATE, TITLE }
    private var sortMode = SortMode.NONE
    private var visibleTasks: List<Task> = emptyList()
    private lateinit var adapter: TaskAdapter
    private lateinit var taskListView: ListView
    private lateinit var emptyView: TextView

    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            TaskRepository.tasks.add(
                Task(
                    title = data.getStringExtra(IntentKeys.EXTRA_TITLE).orEmpty(),
                    course = data.getStringExtra(IntentKeys.EXTRA_COURSE).orEmpty(),
                    priority = data.getStringExtra(IntentKeys.EXTRA_PRIORITY).orEmpty(),
                    dueDate = data.getStringExtra(IntentKeys.EXTRA_DUE_DATE).orEmpty()
                )
            )
            refreshList()
            Toast.makeText(this, R.string.task_added, Toast.LENGTH_SHORT).show()
        }
    }

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            val position = data.getIntExtra(IntentKeys.EXTRA_POSITION, -1)
            val task = visibleTasks.getOrNull(position)
            if (task != null) {
                when (data.getStringExtra(IntentKeys.EXTRA_ACTION)) {
                    IntentKeys.ACTION_DONE -> markDone(task)
                    IntentKeys.ACTION_DELETE -> deleteTask(task)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        attachToolbar(findViewById<MaterialToolbar>(R.id.toolbar), R.string.my_tasks, true)
        supportActionBar?.subtitle = intent.getStringExtra(IntentKeys.STUDENT_NAME)

        taskListView = findViewById(R.id.taskListView)
        emptyView = findViewById(R.id.emptyView)
        adapter = TaskAdapter(this, emptyList())
        taskListView.adapter = adapter
        taskListView.setOnItemClickListener { _, _, position, _ -> openDetails(position) }
        registerForContextMenu(taskListView)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> addTaskLauncher.launch(Intent(this, AddTaskActivity::class.java))
            R.id.sort_priority -> applySort(SortMode.PRIORITY)
            R.id.sort_due_date -> applySort(SortMode.DUE_DATE)
            R.id.sort_title -> applySort(SortMode.TITLE)
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.action_exit -> finishAffinity()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu.setHeaderTitle(visibleTasks[info.position].title)
        menuInflater.inflate(R.menu.menu_task_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val task = visibleTasks[info.position]
        return when (item.itemId) {
            R.id.context_view_details -> {
                openDetails(info.position)
                true
            }
            R.id.context_mark_done -> {
                markDone(task)
                true
            }
            R.id.context_delete -> {
                confirmDelete(task)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun applySort(mode: SortMode) {
        sortMode = mode
        refreshList()
    }

    private fun refreshList() {
        visibleTasks = sorted(TaskRepository.tasks.toList())
        adapter.submit(visibleTasks)
        val isEmpty = visibleTasks.isEmpty()
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        taskListView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun sorted(tasks: List<Task>): List<Task> = when (sortMode) {
        SortMode.PRIORITY -> tasks.sortedBy { it.priorityRank }
        SortMode.DUE_DATE -> tasks.sortedBy { it.dueDate }
        SortMode.TITLE -> tasks.sortedBy { it.title.lowercase() }
        SortMode.NONE -> tasks
    }

    private fun openDetails(position: Int) {
        val task = visibleTasks[position]
        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putExtra(IntentKeys.EXTRA_TITLE, task.title)
        intent.putExtra(IntentKeys.EXTRA_COURSE, task.course)
        intent.putExtra(IntentKeys.EXTRA_PRIORITY, task.priority)
        intent.putExtra(IntentKeys.EXTRA_DUE_DATE, task.dueDate)
        intent.putExtra(IntentKeys.EXTRA_DONE, task.done)
        intent.putExtra(IntentKeys.EXTRA_POSITION, position)
        detailLauncher.launch(intent)
    }

    private fun markDone(task: Task) {
        if (task.done) {
            Toast.makeText(this, R.string.task_already_done, Toast.LENGTH_SHORT).show()
            return
        }
        task.done = true
        refreshList()
        Toast.makeText(this, R.string.task_done, Toast.LENGTH_SHORT).show()
    }

    private fun confirmDelete(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_confirm_title)
            .setMessage(getString(R.string.delete_confirm_message, task.title))
            .setPositiveButton(R.string.delete) { _, _ -> deleteTask(task) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun deleteTask(task: Task) {
        TaskRepository.tasks.removeAll { it === task }
        refreshList()
        Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show()
    }
}