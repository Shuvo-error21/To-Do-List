package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class GoalCategory {
    STUDY, CODING, CAREER, HEALTH, PERSONAL
}

enum class PriorityLevel {
    HIGH, MEDIUM, LOW
}

enum class AmbitionLevel {
    CHILL, MODERATE, AGGRESSIVE
}

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String = "default_user",
    val name: String = "Goal Achiever",
    val email: String = "user@goalai.app",
    val avatarUrl: String = "",
    val targetGoalsSummary: String = "Pass with A+ and Master Development",
    val educationField: String = "Computer Science & Engineering",
    val dailyAvailableHours: Float = 4.0f,
    val ambitionLevel: AmbitionLevel = AmbitionLevel.MODERATE,
    val languageCode: String = "en",
    val isGuest: Boolean = true,
    val biometricEnabled: Boolean = false,
    val pinCode: String = "",
    val currentStreak: Int = 5,
    val totalCompletedTasks: Int = 18,
    val totalFocusMinutes: Int = 340,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val category: GoalCategory = GoalCategory.STUDY,
    val targetDateEpoch: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // default 30 days
    val ambitionLevel: AmbitionLevel = AmbitionLevel.MODERATE,
    val totalEstimatedHours: Int = 40,
    val progressPercentage: Float = 0.0f,
    val isCompleted: Boolean = false,
    val colorHex: String = "#6366F1",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val goalId: String,
    val title: String,
    val description: String = "",
    val orderIndex: Int = 0,
    val targetDateEpoch: Long = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000),
    val isCompleted: Boolean = false
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val goalId: String? = null,
    val milestoneId: String? = null,
    val title: String,
    val description: String = "",
    val category: GoalCategory = GoalCategory.STUDY,
    val priority: PriorityLevel = PriorityLevel.MEDIUM,
    val durationMinutes: Int = 45,
    val dueDate: String, // format: "YYYY-MM-DD" e.g., "2026-08-14"
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isAiGenerated: Boolean = false,
    val missedAndRolledOver: Boolean = false,
    val notes: String = "",
    val subTasksJson: String = "[]",
    val orderIndex: Int = 0
)

@Entity(tableName = "ai_logs")
data class AiLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val prompt: String,
    val response: String,
    val actionType: String = "CHAT", // "REBALANCE", "BREAKDOWN", "ADVICE", "CHAT"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val taskId: String? = null,
    val goalId: String? = null,
    val taskTitle: String = "Deep Work Session",
    val durationMinutes: Int = 25,
    val completedAt: Long = System.currentTimeMillis(),
    val sessionType: String = "POMODORO" // "POMODORO", "SHORT_BREAK", "LONG_BREAK"
)
