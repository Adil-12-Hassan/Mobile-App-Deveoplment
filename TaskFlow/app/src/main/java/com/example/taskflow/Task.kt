package com.example.taskflow

import android.content.Context
import androidx.core.content.ContextCompat

data class Task(
    val title: String,
    val course: String,
    val priority: String,
    val dueDate: String,
    var done: Boolean = false
) {
    val priorityRank: Int
        get() = when (priority) {
            Priorities.HIGH -> 0
            Priorities.MEDIUM -> 1
            else -> 2
        }
}

object Priorities {
    const val HIGH = "High"
    const val MEDIUM = "Medium"
    const val LOW = "Low"
}

fun Context.priorityLabel(priority: String): String = getString(
    when (priority) {
        Priorities.HIGH -> R.string.priority_high
        Priorities.MEDIUM -> R.string.priority_medium
        else -> R.string.priority_low
    }
)

fun Context.priorityColor(priority: String): Int = ContextCompat.getColor(
    this,
    when (priority) {
        Priorities.HIGH -> R.color.priority_high
        Priorities.MEDIUM -> R.color.priority_medium
        else -> R.color.priority_low
    }
)