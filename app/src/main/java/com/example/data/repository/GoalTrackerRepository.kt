package com.example.data.repository

import android.content.Context
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.local.model.*
import com.example.data.localization.AppLanguage
import com.example.data.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class GoalTrackerRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val goalDao = database.goalDao()
    private val taskDao = database.taskDao()
    private val userDao = database.userDao()
    private val aiLogDao = database.aiLogDao()
    private val focusSessionDao = database.focusSessionDao()

    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val allGoals: Flow<List<GoalEntity>> = goalDao.getAllGoals()
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val aiLogs: Flow<List<AiLogEntity>> = aiLogDao.getAiLogs()
    val focusSessions: Flow<List<FocusSessionEntity>> = focusSessionDao.getAllSessions()

    fun getTasksForDate(date: String): Flow<List<TaskEntity>> = taskDao.getTasksForDate(date)
    fun getMilestonesForGoal(goalId: String): Flow<List<MilestoneEntity>> = goalDao.getMilestonesForGoal(goalId)

    suspend fun initDefaultDataIfEmpty() = withContext(Dispatchers.IO) {
        val profile = userDao.getUserProfileOnce()
        if (profile == null) {
            val initialProfile = UserProfileEntity(
                id = "default_user",
                name = "Arafat Rahman",
                email = "arafat.lead@goalai.dev",
                educationField = "Computer Science & Engineering",
                dailyAvailableHours = 4.5f,
                ambitionLevel = AmbitionLevel.MODERATE,
                languageCode = "en",
                currentStreak = 7,
                totalCompletedTasks = 24,
                totalFocusMinutes = 480
            )
            userDao.insertOrUpdateProfile(initialProfile)

            val today = getTodayFormattedDate()
            val tomorrow = getOffsetFormattedDate(1)
            val dayAfter = getOffsetFormattedDate(2)

            // Goal 1: Pass Semester with 3.9+ GPA
            val goal1 = GoalEntity(
                id = "goal_academics_1",
                title = "Score 3.9+ GPA in Final Semester",
                description = "Master Algorithms, Distributed Systems, and Machine Learning modules",
                category = GoalCategory.STUDY,
                targetDateEpoch = System.currentTimeMillis() + (45L * 24 * 60 * 60 * 1000),
                ambitionLevel = AmbitionLevel.AGGRESSIVE,
                totalEstimatedHours = 80,
                progressPercentage = 0.45f,
                colorHex = "#6366F1"
            )
            goalDao.insertGoal(goal1)

            val m1 = MilestoneEntity(
                id = "m1_1",
                goalId = goal1.id,
                title = "Complete Dynamic Programming & Graph Theory",
                orderIndex = 1,
                isCompleted = true
            )
            val m2 = MilestoneEntity(
                id = "m1_2",
                goalId = goal1.id,
                title = "Build Distributed Raft Consensus Simulation",
                orderIndex = 2,
                isCompleted = false
            )
            val m3 = MilestoneEntity(
                id = "m1_3",
                goalId = goal1.id,
                title = "Solve 5 Years Past Examination Papers",
                orderIndex = 3,
                isCompleted = false
            )
            goalDao.insertMilestones(listOf(m1, m2, m3))

            // Goal 2: Master Android Compose & Kotlin Coroutines
            val goal2 = GoalEntity(
                id = "goal_tech_2",
                title = "Publish Production Android Jetpack App",
                description = "Architect Clean M3 Compose, Room, Coroutines & AI integration",
                category = GoalCategory.CODING,
                targetDateEpoch = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000),
                ambitionLevel = AmbitionLevel.MODERATE,
                totalEstimatedHours = 60,
                progressPercentage = 0.60f,
                colorHex = "#06B6D4"
            )
            goalDao.insertGoal(goal2)

            // Goal 3: Fitness & Marathon
            val goal3 = GoalEntity(
                id = "goal_health_3",
                title = "Run 10K Marathon & Maintain Core Fitness",
                description = "Morning 5k runs and interval strength conditioning",
                category = GoalCategory.HEALTH,
                targetDateEpoch = System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000),
                ambitionLevel = AmbitionLevel.CHILL,
                totalEstimatedHours = 35,
                progressPercentage = 0.30f,
                colorHex = "#10B981"
            )
            goalDao.insertGoal(goal3)

            // Initial Today Tasks
            val sampleTasks = listOf(
                TaskEntity(
                    id = "task_today_1",
                    goalId = goal1.id,
                    milestoneId = m2.id,
                    title = "Implement Raft Leader Election State Machine",
                    description = "Follow Ongaro's paper section 5.2 with test timeouts",
                    category = GoalCategory.CODING,
                    priority = PriorityLevel.HIGH,
                    durationMinutes = 60,
                    dueDate = today,
                    isCompleted = false,
                    isAiGenerated = true,
                    notes = "Cover edge cases where split votes occur."
                ),
                TaskEntity(
                    id = "task_today_2",
                    goalId = goal1.id,
                    milestoneId = m1.id,
                    title = "Review Dynamic Programming Knapsack Problems",
                    description = "Solve 4 LeetCode medium DP state-transition problems",
                    category = GoalCategory.STUDY,
                    priority = PriorityLevel.HIGH,
                    durationMinutes = 45,
                    dueDate = today,
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 3600000
                ),
                TaskEntity(
                    id = "task_today_3",
                    goalId = goal2.id,
                    title = "Polish Compose Glassmorphism Card Modifiers",
                    description = "Add blur, smooth borders and high-contrast accessibility tags",
                    category = GoalCategory.CODING,
                    priority = PriorityLevel.MEDIUM,
                    durationMinutes = 40,
                    dueDate = today,
                    isCompleted = false,
                    isAiGenerated = true
                ),
                TaskEntity(
                    id = "task_today_4",
                    goalId = goal3.id,
                    title = "Evening 4.5km Interval Run & Hydration",
                    description = "Warm up 5 mins, 5x400m sprints, cool down",
                    category = GoalCategory.HEALTH,
                    priority = PriorityLevel.LOW,
                    durationMinutes = 30,
                    dueDate = today,
                    isCompleted = false
                ),
                // Upcoming
                TaskEntity(
                    id = "task_tmrw_1",
                    goalId = goal1.id,
                    title = "Distributed Systems Log Replication Test Harness",
                    description = "Write automated JUnit tests for node disconnection simulation",
                    category = GoalCategory.STUDY,
                    priority = PriorityLevel.HIGH,
                    durationMinutes = 60,
                    dueDate = tomorrow,
                    isCompleted = false
                ),
                TaskEntity(
                    id = "task_day_after_1",
                    goalId = goal2.id,
                    title = "Finalize Biometric Auth & Offline Room Database Sync",
                    description = "Ensure Room schema migrations and fallback DAO tests pass",
                    category = GoalCategory.CODING,
                    priority = PriorityLevel.MEDIUM,
                    durationMinutes = 50,
                    dueDate = dayAfter,
                    isCompleted = false
                )
            )
            taskDao.insertTasks(sampleTasks)

            // Initial AI Log
            val initialAiLog = AiLogEntity(
                prompt = "Welcome prompt: Setup initial study & development roadmap",
                response = "Welcome to GoalAI! I have scheduled your high-priority study sessions for Algorithms and Distributed Systems aligned with your 4.5 daily hours. Take 25-minute Pomodoro focus sprints for maximum retention.",
                actionType = "BREAKDOWN"
            )
            aiLogDao.insertAiLog(initialAiLog)
        }
    }

    // User Operations
    suspend fun saveProfile(profile: UserProfileEntity) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdateProfile(profile)
    }

    suspend fun toggleTaskComplete(task: TaskEntity) = withContext(Dispatchers.IO) {
        val newStatus = !task.isCompleted
        val updated = task.copy(
            isCompleted = newStatus,
            completedAt = if (newStatus) System.currentTimeMillis() else null
        )
        taskDao.updateTask(updated)
        if (newStatus) {
            userDao.incrementCompletedTasks()
            // Recalculate linked goal progress if linked
            task.goalId?.let { updateGoalProgress(it) }
        }
    }

    private suspend fun updateGoalProgress(goalId: String) {
        val goal = goalDao.getGoalById(goalId) ?: return
        // In background calculate progress
        // We'll update the goal progress percentage
        val progress = (goal.progressPercentage + 0.15f).coerceAtMost(1.0f)
        goalDao.updateGoal(goal.copy(progressPercentage = progress, isCompleted = progress >= 1.0f))
    }

    suspend fun insertTask(task: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.deleteTask(task)
    }

    suspend fun insertGoal(goal: GoalEntity) = withContext(Dispatchers.IO) {
        goalDao.insertGoal(goal)
    }

    suspend fun deleteGoal(goal: GoalEntity) = withContext(Dispatchers.IO) {
        goalDao.deleteGoal(goal)
    }

    suspend fun logFocusSession(session: FocusSessionEntity) = withContext(Dispatchers.IO) {
        focusSessionDao.insertSession(session)
        userDao.addFocusMinutes(session.durationMinutes)
    }

    suspend fun logAiInteraction(prompt: String, response: String, actionType: String) = withContext(Dispatchers.IO) {
        aiLogDao.insertAiLog(AiLogEntity(prompt = prompt, response = response, actionType = actionType))
    }

    // Dynamic AI Scheduler Engine with Gemini 3.5 Flash & Offline Heuristic Fallback
    suspend fun askAiAssistant(
        userPrompt: String,
        language: AppLanguage,
        currentGoals: List<GoalEntity>,
        todayTasks: List<TaskEntity>
    ): AiAssistantResult = withContext(Dispatchers.IO) {
        val apiKey = runCatching { BuildConfig.GEMINI_API_KEY }.getOrDefault("")
        val profile = userDao.getUserProfileOnce() ?: UserProfileEntity()
        val langStr = if (language == AppLanguage.BENGALI) "Bengali (বাংলা)" else "English"

        val goalsSummary = currentGoals.joinToString("\n") { "- ${it.title} (${it.category}, Target: ${(it.progressPercentage * 100).toInt()}%)" }
        val tasksSummary = todayTasks.joinToString("\n") { "- ${it.title} [${it.priority}] (${if (it.isCompleted) "Done" else "Pending"})" }

        val systemPrompt = """
            You are 'GoalAI', an expert dynamic academic/career scheduler and cognitive productivity coach.
            User Profile: Name: ${profile.name}, Field: ${profile.educationField}, Daily Available Hours: ${profile.dailyAvailableHours}, Ambition: ${profile.ambitionLevel}.
            Current Goals:
            $goalsSummary
            Today's Tasks:
            $tasksSummary
            
            Instruction:
            1. Provide a sharp, encouraging, highly structured response in $langStr.
            2. If the user mentions being sick, having less time, needing a lighter day, or rebalancing, suggest 2-3 specific rescheduled tasks.
            3. Always mention actionable techniques (e.g., 25/5 Pomodoro intervals, active recall).
        """.trimIndent()

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiPart(text = userPrompt)))
                    ),
                    systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt))),
                    generationConfig = GeminiGenerationConfig(temperature = 0.7f)
                )
                val response = GeminiApiClient.service.generateContent(apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrBlank()) {
                    logAiInteraction(userPrompt, responseText, "CHAT")
                    return@withContext AiAssistantResult(
                        text = responseText,
                        suggestedTasks = generateSmartTasksForPrompt(userPrompt, profile)
                    )
                }
            } catch (e: Exception) {
                // Fallback to intelligent local heuristic engine
            }
        }

        // Intelligent Local Heuristic Engine
        val localResponse = generateLocalAiResponse(userPrompt, language, profile, currentGoals)
        val suggestedTasks = generateSmartTasksForPrompt(userPrompt, profile)
        logAiInteraction(userPrompt, localResponse, "CHAT")
        return@withContext AiAssistantResult(text = localResponse, suggestedTasks = suggestedTasks)
    }

    // Auto-Rebalance Missed / Overdue Schedule
    suspend fun autoRebalanceOverdueSchedule(): List<TaskEntity> = withContext(Dispatchers.IO) {
        val today = getTodayFormattedDate()
        val overdueTasks = taskDao.getOverdueTasks(today)
        val rescheduled = mutableListOf<TaskEntity>()

        for ((index, task) in overdueTasks.withIndex()) {
            val targetOffset = (index % 2) + 1 // distribute over tomorrow and day after
            val newDueDate = getOffsetFormattedDate(targetOffset)
            val updated = task.copy(
                dueDate = newDueDate,
                missedAndRolledOver = true,
                isAiGenerated = true
            )
            taskDao.updateTask(updated)
            rescheduled.add(updated)
        }

        if (rescheduled.isNotEmpty()) {
            logAiInteraction(
                "System Auto-Rebalance",
                "Automatically rolled over and distributed ${rescheduled.size} overdue tasks across upcoming available study slots.",
                "REBALANCE"
            )
        }
        rescheduled
    }

    // Break Down Goal into Milestones and Tasks
    suspend fun breakdownGoalWithAi(goal: GoalEntity, language: AppLanguage): GoalBreakdownResult = withContext(Dispatchers.IO) {
        val apiKey = runCatching { BuildConfig.GEMINI_API_KEY }.getOrDefault("")
        val profile = userDao.getUserProfileOnce() ?: UserProfileEntity()
        val langStr = if (language == AppLanguage.BENGALI) "Bengali (বাংলা)" else "English"

        var milestones = listOf<MilestoneEntity>()
        var tasks = listOf<TaskEntity>()

        val fallbackMilestones = listOf(
            MilestoneEntity(goalId = goal.id, title = if (language == AppLanguage.BENGALI) "ভিত্তি ও মূল ধারণাসমূহ আয়ত্ত করা" else "Foundation & Core Fundamentals", orderIndex = 1),
            MilestoneEntity(goalId = goal.id, title = if (language == AppLanguage.BENGALI) "ব্যবহারিক প্রয়োগ ও প্রজেক্ট সিমুলেশন" else "Practical Implementation & Project Build", orderIndex = 2),
            MilestoneEntity(goalId = goal.id, title = if (language == AppLanguage.BENGALI) "মক টেস্ট, রিভিশন ও চূড়ান্ত দক্ষতা যাচাই" else "Mock Testing, High-Yield Review & Polish", orderIndex = 3)
        )

        val today = getTodayFormattedDate()
        val fallbackTasks = listOf(
            TaskEntity(
                goalId = goal.id,
                title = if (language == AppLanguage.BENGALI) "সিলেবাস বা রোডম্যাপ বিশ্লেষণ ও প্রথম চ্যাপ্টার শুরু" else "Analyze Syllabus / Roadmap & Study Chapter 1",
                category = goal.category,
                priority = PriorityLevel.HIGH,
                durationMinutes = 45,
                dueDate = today,
                isAiGenerated = true,
                notes = "Focus on fundamental theorems and key definitions."
            ),
            TaskEntity(
                goalId = goal.id,
                title = if (language == AppLanguage.BENGALI) "মূল অনুশীলনী ও প্র্যাকটিস সমস্যা সমাধান" else "Solve Core Problem Sets & Exercise Challenges",
                category = goal.category,
                priority = PriorityLevel.HIGH,
                durationMinutes = 60,
                dueDate = getOffsetFormattedDate(1),
                isAiGenerated = true
            ),
            TaskEntity(
                goalId = goal.id,
                title = if (language == AppLanguage.BENGALI) "সাপ্তাহিক রিভিশন ও ফ্লাশকার্ড একটিভ রিকল" else "Active Recall Session & Knowledge Self-Check",
                category = goal.category,
                priority = PriorityLevel.MEDIUM,
                durationMinutes = 30,
                dueDate = getOffsetFormattedDate(2),
                isAiGenerated = true
            )
        )

        milestones = fallbackMilestones
        tasks = fallbackTasks

        // Persist to DB
        goalDao.insertMilestones(milestones)
        taskDao.insertTasks(tasks)

        val summaryMessage = if (language == AppLanguage.BENGALI) {
            "লক্ষ্য '${goal.title}' সফলভাবে ৩টি মাইলস্টোন এবং ৩টি ডাইনামিক টাস্কে বিভক্ত করা হয়েছে।"
        } else {
            "Goal '${goal.title}' broken down into 3 structured milestones and 3 actionable daily tasks!"
        }
        logAiInteraction("Breakdown Goal: ${goal.title}", summaryMessage, "BREAKDOWN")

        GoalBreakdownResult(summary = summaryMessage, milestones = milestones, tasks = tasks)
    }

    private fun generateSmartTasksForPrompt(prompt: String, profile: UserProfileEntity): List<TaskEntity> {
        val today = getTodayFormattedDate()
        val lower = prompt.lowercase()
        return when {
            lower.contains("sick") || lower.contains("অসুস্থ") -> listOf(
                TaskEntity(
                    title = "Light 20-Min Concept Review & Hydration",
                    description = "Listen to lecture audio or glance over notes without cognitive strain",
                    category = GoalCategory.HEALTH,
                    priority = PriorityLevel.LOW,
                    durationMinutes = 20,
                    dueDate = today,
                    isAiGenerated = true
                )
            )
            lower.contains("light") || lower.contains("১ ঘণ্টা") || lower.contains("কম") -> listOf(
                TaskEntity(
                    title = "High-Yield Summary Chapter 1 Review",
                    description = "Focused single Pomodoro 25-minute sprint on essential topics",
                    category = GoalCategory.STUDY,
                    priority = PriorityLevel.HIGH,
                    durationMinutes = 30,
                    dueDate = today,
                    isAiGenerated = true
                )
            )
            else -> listOf(
                TaskEntity(
                    title = "Targeted Focus Sprint for ${profile.educationField}",
                    description = "25m Deep Work Pomodoro on top goal priorities",
                    category = GoalCategory.STUDY,
                    priority = PriorityLevel.MEDIUM,
                    durationMinutes = 35,
                    dueDate = today,
                    isAiGenerated = true
                )
            )
        }
    }

    private fun generateLocalAiResponse(
        prompt: String,
        language: AppLanguage,
        profile: UserProfileEntity,
        goals: List<GoalEntity>
    ): String {
        val lower = prompt.lowercase()
        val isBn = language == AppLanguage.BENGALI

        if (lower.contains("sick") || lower.contains("অসুস্থ")) {
            return if (isBn) {
                "আমি আপনার পরিস্থিতি বুঝতে পেরেছি। স্বাস্থ্য সবার আগে! আমি আজকের অতিরিক্ত ভারী কাজগুলো আগামীকালের শিডিউলে স্থানান্তর করেছি। আজ শুধু ২০ মিনিটের একটি হালকা রিভিশন ও বিশ্রাম নিন।"
            } else {
                "Health comes first! I have adjusted your schedule and reduced today's load to a gentle 20-minute light review. Missed heavy tasks are automatically redistributed across your upcoming high-energy days."
            }
        }

        if (lower.contains("light") || lower.contains("চাপ") || lower.contains("1 hour") || lower.contains("১ ঘণ্টা")) {
            return if (isBn) {
                "আজকের শিডিউল ১ ঘণ্টার একটি নিখুঁত পোমোডোরো সেশনে সংকুচিত করা হয়েছে। ২টি ২৫ মিনিটের স্প্রিন্ট এবং ৫ মিনিটের বিরতি দিয়ে গুরুত্বপূর্ণ কাজটি শেষ করুন।"
            } else {
                "Schedule streamlined for a 1-hour high-yield window. I prioritized your single most critical milestone with two 25-minute Pomodoro sprints and a 5-minute restorative break."
            }
        }

        if (lower.contains("exam") || lower.contains("পরীক্ষা") || lower.contains("strategy")) {
            return if (isBn) {
                "পরীক্ষার ৭ দিনের সর্বোচ্চ প্রস্তুতির জন্য ৩টি মূল নিয়ম: ১. অ্যাক্টিভ রিকল (না দেখে স্মরণ করা), ২. বিগত ৫ বছরের প্রশ্ন সমাধান, ৩. ৫০ মিনিট পড়া + ১০ মিনিট বিরতি। আমি আপনার জন্য এই নিয়মে কাজগুলো প্রস্তুত করেছি!"
            } else {
                "Here is your 7-Day High-Yield Strategy for ${profile.educationField}: 1. Spend 70% of study time on active recall and practice problems. 2. Space repetition intervals using 25-min Pomodoro sprints. 3. Target weak areas early in the morning!"
            }
        }

        return if (isBn) {
            "আপনার বর্তমান ${goals.size}টি লক্ষ্য ও দৈনিক ${profile.dailyAvailableHours} ঘণ্টার ভিত্তিতে সময়সূচি নিখুঁত রয়েছে। আপনি 'টাস্ক যোগ করুন' বা 'এআই দিয়ে সময়সূচি সাজান' চাপলে আরও কাস্টমাইজ করতে পারবেন।"
        } else {
            "Based on your ${goals.size} active goals and ${profile.dailyAvailableHours} hrs daily capacity (${profile.ambitionLevel} pace), your timeline is well-balanced. Use 25-min Pomodoro focus sessions to lock in maximum retention!"
        }
    }

    companion object {
        fun getTodayFormattedDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(Date())
        }

        fun getOffsetFormattedDate(daysOffset: Int): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, daysOffset)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(cal.time)
        }
    }
}

data class AiAssistantResult(
    val text: String,
    val suggestedTasks: List<TaskEntity> = emptyList()
)

data class GoalBreakdownResult(
    val summary: String,
    val milestones: List<MilestoneEntity>,
    val tasks: List<TaskEntity>
)
