package com.example.todoappcompose.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.data.repositories.ToDoRepository
import com.example.todoappcompose.util.RequestState
import com.example.todoappcompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSE)

    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTAsks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTAsks

    private val _selectedTask = MutableStateFlow<ToDoTask?>(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

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

}
