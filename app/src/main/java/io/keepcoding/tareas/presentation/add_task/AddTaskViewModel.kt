package io.keepcoding.tareas.presentation.add_task

import androidx.lifecycle.MutableLiveData
import io.keepcoding.tareas.domain.TaskRepository
import io.keepcoding.tareas.domain.model.Task
import io.keepcoding.tareas.presentation.BaseViewModel
import io.keepcoding.util.DispatcherFactory
import io.keepcoding.util.Event
import io.keepcoding.util.extensions.call
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant

class AddTaskViewModel(
    private val taskRepository: TaskRepository,
    private val dispatcherFactory: DispatcherFactory
) : BaseViewModel(dispatcherFactory) {

    val closeAction = MutableLiveData<Event<Unit>>()

    fun save(content: String, description: String, priority: Boolean) {
        if (!validateContent(content) || !validateContent(description)) {
            return
        }

        launch {
            withContext(dispatcherFactory.getIO()) {
                taskRepository.addTask(Task(0, content, description, Instant.now(), priority, false))
            }
            closeAction.call()
        }
    }

    fun update(task: Task,content: String, description: String, priority: Boolean) {
        if (!validateContent(content) || !validateContent(description)) {
            return
        }

        launch {
            withContext(dispatcherFactory.getIO()) {
                taskRepository.updateTask(Task(task.id, content, description, task.createdAt, priority, task.isFinished))
            }
            closeAction.call()
        }
    }

    fun delete(task: Task) {

        launch {
            withContext(dispatcherFactory.getIO()) {
                taskRepository.deleteTask(task)
            }
            closeAction.call()
        }
    }

    private fun validateContent(content: String): Boolean =
        content.isNotEmpty()

}