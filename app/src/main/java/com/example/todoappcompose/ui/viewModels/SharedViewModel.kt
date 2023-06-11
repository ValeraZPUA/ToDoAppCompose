package com.example.todoappcompose.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.data.repositories.ToDoRepository
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants.MAX_TITLE_LENGTH
import com.example.todoappcompose.util.RequestState
import com.example.todoappcompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSE)

    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTAsks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTAsks

    private val _selectedTask = MutableStateFlow<ToDoTask?>(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.addTask(toDoTask = toDoTask)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.deleteTask(toDoTask = toDoTask)
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {

            }
            Action.UNDO -> {

            }
            else -> {

            }
        }
        this.action.value = Action.NO_ACTION
    }

    fun getAllTasks() {
        _allTAsks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect {
                    _allTAsks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTAsks.value = RequestState.Error(e)
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            toDoRepository.getSelectedTask(taskId = taskId).collect {
                _selectedTask.value = it
            }
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

}
