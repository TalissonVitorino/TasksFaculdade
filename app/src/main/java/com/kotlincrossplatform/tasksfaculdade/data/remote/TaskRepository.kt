package com.kotlincrossplatform.tasksfaculdade.data.remote

import com.kotlincrossplatform.tasksfaculdade.data.model.Task

class TaskRepository {

    private val api = RetrofitInstance.api

    suspend fun getAllTasks(): List<Task> = api.getAllTasks()

    suspend fun getTaskById(id: Long): Task = api.getTaskById(id)

    suspend fun createTask(task: Task): Task = api.createTask(task)

    suspend fun updateTask(id: Long, task: Task): Task = api.updateTask(id, task)

    suspend fun deleteTask(id: Long) = api.deleteTask(id)
}
