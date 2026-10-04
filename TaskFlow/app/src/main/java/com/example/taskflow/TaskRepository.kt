package com.example.taskflow

/** Shared in-memory task list. Tasks live only while the app process is alive. */
object TaskRepository {
    val tasks: MutableList<Task> = mutableListOf()
}