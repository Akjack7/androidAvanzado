package io.keepcoding.tareas.domain.model

import org.threeten.bp.Instant
import java.io.Serializable

data class Task(
    val id: Long,
    val content: String,
    val description: String,
    val createdAt: Instant,
    val isHighPriority: Boolean,
    val isFinished: Boolean
): Serializable