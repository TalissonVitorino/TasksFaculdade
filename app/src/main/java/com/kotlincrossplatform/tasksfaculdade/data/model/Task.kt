package com.kotlincrossplatform.tasksfaculdade.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id")          val id: Long? = null,
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("completed")   val completed: Boolean = false
)
