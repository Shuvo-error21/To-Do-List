package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :goalId LIMIT 1")
    suspend fun getGoalById(goalId: String): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM milestones WHERE goalId = :goalId ORDER BY orderIndex ASC")
    fun getMilestonesForGoal(goalId: String): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones ORDER BY orderIndex ASC")
    fun getAllMilestones(): Flow<List<MilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: MilestoneEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<MilestoneEntity>)

    @Update
    suspend fun updateMilestone(milestone: MilestoneEntity)

    @Query("DELETE FROM milestones WHERE id = :id")
    suspend fun deleteMilestone(id: String)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC, priority ASC, orderIndex ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate = :date ORDER BY isCompleted ASC, priority ASC, orderIndex ASC")
    fun getTasksForDate(date: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE goalId = :goalId ORDER BY dueDate ASC")
    fun getTasksForGoal(goalId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDate < :currentDate")
    suspend fun getOverdueTasks(currentDate: String): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE id = 'default_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = 'default_user' LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profiles SET totalCompletedTasks = totalCompletedTasks + 1 WHERE id = 'default_user'")
    suspend fun incrementCompletedTasks()

    @Query("UPDATE user_profiles SET totalFocusMinutes = totalFocusMinutes + :minutes WHERE id = 'default_user'")
    suspend fun addFocusMinutes(minutes: Int)
}

@Dao
interface AiLogDao {
    @Query("SELECT * FROM ai_logs ORDER BY timestamp DESC LIMIT 50")
    fun getAiLogs(): Flow<List<AiLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAiLog(log: AiLogEntity)

    @Query("DELETE FROM ai_logs")
    suspend fun clearLogs()
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity)
}
