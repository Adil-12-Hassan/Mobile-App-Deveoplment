package com.example.taskflow

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class TaskAdapter(
    private val context: Context,
    private var tasks: List<Task>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)
    fun submit(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
    override fun getCount(): Int = tasks.size
    override fun getItem(position: Int): Task = tasks[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.item_task, parent, false)
        val task = tasks[position]

        val titleView = view.findViewById<TextView>(R.id.itemTitle)
        val courseView = view.findViewById<TextView>(R.id.itemCourse)
        val priorityView = view.findViewById<TextView>(R.id.itemPriority)
        val dueView = view.findViewById<TextView>(R.id.itemDue)

        titleView.text = task.title
        titleView.paintFlags = if (task.done) {
            titleView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            titleView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        courseView.text = task.course

        priorityView.text = context.priorityLabel(task.priority)
        priorityView.setTextColor(context.priorityColor(task.priority))

        dueView.text = context.getString(R.string.due_format, TaskDates.toDisplay(task.dueDate))
        val overdue = !task.done && TaskDates.isOverdue(task.dueDate)
        val dueColor = if (overdue) R.color.danger else R.color.text_secondary
        dueView.setTextColor(ContextCompat.getColor(context, dueColor))

        view.alpha = if (task.done) 0.6f else 1f
        return view
    }
}