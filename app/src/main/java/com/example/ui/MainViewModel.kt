package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.model.*
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.data.repository.GoalTrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MainUiState(
    val userProfile: UserProfileEntity = UserProfileEntity(),
    val language: AppLanguage = AppLanguage.ENGLISH,
    val goals: List<GoalEntity> = emptyList(),
    val todayTasks: List<TaskEntity> = emptyList(),
    val allTasks: List<TaskEntity> = emptyList(),
    val aiLogs: List<AiLogEntity> = emptyList(),
    val focusSessions: List<FocusSessionEntity> = emptyList(),
    val isAuthenticated: Boolean = false,
    val isBiometricPromptVisible: Boolean = false,
    val selectedGoalForDetail: GoalEntity? = null,
    val isAddGoalDialogVisible: Boolean = false,
    val isAddTaskDialogVisible: Boolean = false,
    val isPomodoroSheetVisible: Boolean = false,
    val pomodoroLinkedTask: TaskEntity? = null,
    val isAiThinking: Boolean = false,
    val aiLastMessage: String? = null,
    val aiSuggestedTasks: List<TaskEntity> = emptyList(),
    val snackbarMessage: String? = null,
    val currentTab: String = "dashboard" // "dashboard", "schedule", "goals", "ai", "profile"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GoalTrackerRepository(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initDefaultDataIfEmpty()
            observeData()
        }
    }

    private fun observeData() {
        val today = GoalTrackerRepository.getTodayFormattedDate()

        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    val lang = if (profile.languageCode == "bn") AppLanguage.BENGALI else AppLanguage.ENGLISH
                    _uiState.update { it.copy(userProfile = profile, language = lang) }
                }
            }
        }

        viewModelScope.launch {
            repository.allGoals.collect { goalsList ->
                _uiState.update { it.copy(goals = goalsList) }
            }
        }

        viewModelScope.launch {
            repository.getTasksForDate(today).collect { todayList ->
                _uiState.update { it.copy(todayTasks = todayList) }
            }
        }

        viewModelScope.launch {
            repository.allTasks.collect { allList ->
                _uiState.update { it.copy(allTasks = allList) }
            }
        }

        viewModelScope.launch {
            repository.aiLogs.collect { logs ->
                _uiState.update { it.copy(aiLogs = logs) }
            }
        }

        viewModelScope.launch {
            repository.focusSessions.collect { sessions ->
                _uiState.update { it.copy(focusSessions = sessions) }
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(language = language) }
        viewModelScope.launch {
            val current = _uiState.value.userProfile
            repository.saveProfile(current.copy(languageCode = language.code))
        }
    }

    fun setTab(tab: String) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun authenticate(isGuest: Boolean = true) {
        _uiState.update { it.copy(isAuthenticated = true) }
    }

    fun authenticateWithBiometric() {
        _uiState.update { it.copy(isAuthenticated = true, isBiometricPromptVisible = false) }
    }

    fun showBiometricPrompt(show: Boolean) {
        _uiState.update { it.copy(isBiometricPromptVisible = show) }
    }

    fun toggleTaskComplete(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskComplete(task)
        }
    }

    fun addNewGoal(
        title: String,
        description: String,
        category: GoalCategory,
        ambitionLevel: AmbitionLevel,
        estimatedHours: Int
    ) {
        viewModelScope.launch {
            val newGoal = GoalEntity(
                title = title,
                description = description,
                category = category,
                ambitionLevel = ambitionLevel,
                totalEstimatedHours = estimatedHours,
                colorHex = when (category) {
                    GoalCategory.STUDY -> "#6366F1"
                    GoalCategory.CODING -> "#06B6D4"
                    GoalCategory.CAREER -> "#8B5CF6"
                    GoalCategory.HEALTH -> "#10B981"
                    GoalCategory.PERSONAL -> "#F59E0B"
                }
            )
            repository.insertGoal(newGoal)
            _uiState.update { it.copy(isAddGoalDialogVisible = false, snackbarMessage = "Goal created successfully!") }
        }
    }

    fun addNewTask(
        title: String,
        description: String,
        category: GoalCategory,
        priority: PriorityLevel,
        durationMinutes: Int,
        dueDate: String,
        goalId: String? = null
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                goalId = goalId,
                title = title,
                description = description,
                category = category,
                priority = priority,
                durationMinutes = durationMinutes,
                dueDate = dueDate
            )
            repository.insertTask(task)
            _uiState.update { it.copy(isAddTaskDialogVisible = false, snackbarMessage = "Task scheduled successfully!") }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun breakdownGoalWithAi(goal: GoalEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            val result = repository.breakdownGoalWithAi(goal, _uiState.value.language)
            _uiState.update {
                it.copy(
                    isAiThinking = false,
                    snackbarMessage = result.summary
                )
            }
        }
    }

    fun askAiAssistant(prompt: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true, aiLastMessage = null) }
            val result = repository.askAiAssistant(
                userPrompt = prompt,
                language = _uiState.value.language,
                currentGoals = _uiState.value.goals,
                todayTasks = _uiState.value.todayTasks
            )
            _uiState.update {
                it.copy(
                    isAiThinking = false,
                    aiLastMessage = result.text,
                    aiSuggestedTasks = result.suggestedTasks
                )
            }
        }
    }

    fun applyAiSuggestedTasks() {
        val suggested = _uiState.value.aiSuggestedTasks
        if (suggested.isNotEmpty()) {
            viewModelScope.launch {
                for (task in suggested) {
                    repository.insertTask(task)
                }
                _uiState.update {
                    it.copy(
                        aiSuggestedTasks = emptyList(),
                        snackbarMessage = Strings.get("ai_changes_applied", it.language)
                    )
                }
            }
        }
    }

    fun autoRebalanceOverdue() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            val rescheduled = repository.autoRebalanceOverdueSchedule()
            _uiState.update {
                it.copy(
                    isAiThinking = false,
                    snackbarMessage = if (rescheduled.isNotEmpty()) {
                        "Rescheduled ${rescheduled.size} tasks with AI load balancing!"
                    } else {
                        "All tasks are on track! No overdue rebalancing needed."
                    }
                )
            }
        }
    }

    fun logFocusSessionCompleted(minutes: Int, sessionType: String) {
        viewModelScope.launch {
            val session = FocusSessionEntity(
                taskId = _uiState.value.pomodoroLinkedTask?.id,
                taskTitle = _uiState.value.pomodoroLinkedTask?.title ?: "Deep Work Session",
                durationMinutes = minutes,
                sessionType = sessionType
            )
            repository.logFocusSession(session)
            _uiState.update {
                it.copy(
                    isPomodoroSheetVisible = false,
                    snackbarMessage = "Completed $minutes min focus session! +$minutes XP logged."
                )
            }
        }
    }

    fun updateProfile(
        name: String,
        education: String,
        dailyHours: Float,
        ambition: AmbitionLevel,
        biometricEnabled: Boolean
    ) {
        viewModelScope.launch {
            val current = _uiState.value.userProfile
            val updated = current.copy(
                name = name,
                educationField = education,
                dailyAvailableHours = dailyHours,
                ambitionLevel = ambition,
                biometricEnabled = biometricEnabled
            )
            repository.saveProfile(updated)
            _uiState.update { it.copy(userProfile = updated, snackbarMessage = "Profile updated!") }
        }
    }

    fun showAddGoalDialog(show: Boolean) = _uiState.update { it.copy(isAddGoalDialogVisible = show) }
    fun showAddTaskDialog(show: Boolean) = _uiState.update { it.copy(isAddTaskDialogVisible = show) }
    fun showPomodoroSheet(show: Boolean, linkedTask: TaskEntity? = null) =
        _uiState.update { it.copy(isPomodoroSheetVisible = show, pomodoroLinkedTask = linkedTask) }
    fun selectGoalDetail(goal: GoalEntity?) = _uiState.update { it.copy(selectedGoalForDetail = goal) }
    fun clearSnackbar() = _uiState.update { it.copy(snackbarMessage = null) }
}
