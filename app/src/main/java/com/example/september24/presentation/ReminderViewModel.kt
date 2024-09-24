package com.example.september24.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.september24.data.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.september24.domain.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // A Flow that represents the list of reminders
    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    // Add error state for insert and delete operations
    private val _insertError = MutableStateFlow<String?>(null)
    val insertError: StateFlow<String?> = _insertError

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError

    private val _fetchError = MutableStateFlow<String?>(null)
    val fetchError: StateFlow<String?> = _fetchError

    init {
        getReminders()
    }
    private fun getReminders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                reminderRepository.getAllReminders()
                    .collect { reminderList ->
                        _reminders.value = reminderList
                    }
            } catch (e: Exception) {
                // Set the error message for fetching reminders
                _fetchError.value = "Failed to fetch reminders: ${e.message}" } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                reminderRepository.insert(reminder)
                _insertError.value = null // Clear previous errors if insert is successful
            } catch (e: Exception) {
                // Set the error message
                _insertError.value = "Failed to insert reminder: ${e.message}"
            }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                reminderRepository.delete(reminder)
                _deleteError.value = null // Clear previous errors if delete is successful
            } catch (e: Exception) {
                // Set the error message
                _deleteError.value = "Failed to delete reminder: ${e.message}"
            }
        }
    }
}