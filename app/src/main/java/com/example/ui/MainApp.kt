package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.*
import com.example.ui.screens.assistant.AiAssistantScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.goals.GoalsScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.schedule.ScheduleScreen
import com.example.ui.theme.*

data class NavItem(
    val route: String,
    val stringKey: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    if (!uiState.isAuthenticated) {
        AuthScreen(
            language = uiState.language,
            onLanguageChange = { viewModel.setLanguage(it) },
            onLoginSuccess = { isGuest -> viewModel.authenticate(isGuest) }
        )
    } else {
        val navItems = listOf(
            NavItem("dashboard", "nav_dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
            NavItem("schedule", "nav_schedule", Icons.Filled.EventNote, Icons.Outlined.EventNote),
            NavItem("goals", "nav_goals", Icons.Filled.Flag, Icons.Outlined.Flag),
            NavItem("ai", "nav_ai", Icons.Filled.Psychology, Icons.Outlined.Psychology),
            NavItem("profile", "nav_profile", Icons.Filled.Person, Icons.Outlined.Person)
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Brush.linearGradient(listOf(LavenderPrimary, IceBlueAccent))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = CharcoalDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "GoalAI",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        StreakBadge(
                            streakDays = uiState.userProfile.currentStreak,
                            language = uiState.language,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        LanguageToggleButton(
                            currentLanguage = uiState.language,
                            onLanguageChange = { viewModel.setLanguage(it) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 12.dp
                ) {
                    NavigationBar(
                        modifier = Modifier.testTag("bottom_nav_bar"),
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
                    ) {
                        navItems.forEach { item ->
                            val isSelected = uiState.currentTab == item.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setTab(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = Strings.get(item.stringKey, uiState.language)
                                    )
                                },
                                label = {
                                    Text(
                                        text = Strings.get(item.stringKey, uiState.language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = LavenderPrimary,
                                    selectedTextColor = LavenderPrimary,
                                    indicatorColor = LavenderPrimary.copy(alpha = 0.18f),
                                    unselectedIconColor = ElegantTextSecondary,
                                    unselectedTextColor = ElegantTextSecondary
                                ),
                                modifier = Modifier.testTag("nav_item_${item.route}")
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Crossfade(targetState = uiState.currentTab, label = "screen_crossfade") { tab ->
                    when (tab) {
                        "dashboard" -> DashboardScreen(
                            userProfile = uiState.userProfile,
                            language = uiState.language,
                            goals = uiState.goals,
                            todayTasks = uiState.todayTasks,
                            onToggleTaskComplete = { viewModel.toggleTaskComplete(it) },
                            onStartPomodoroForTask = { viewModel.showPomodoroSheet(true, it) },
                            onOpenAddGoal = { viewModel.showAddGoalDialog(true) },
                            onOpenAddTask = { viewModel.showAddTaskDialog(true) },
                            onOpenAiPlan = { viewModel.setTab("ai") },
                            onAutoRebalance = { viewModel.autoRebalanceOverdue() },
                            onNavigateToGoals = { viewModel.setTab("goals") },
                            onNavigateToSchedule = { viewModel.setTab("schedule") }
                        )
                        "schedule" -> ScheduleScreen(
                            language = uiState.language,
                            todayTasks = uiState.todayTasks,
                            allTasks = uiState.allTasks,
                            onToggleComplete = { viewModel.toggleTaskComplete(it) },
                            onDeleteTask = { viewModel.deleteTask(it) },
                            onStartPomodoro = { viewModel.showPomodoroSheet(true, it) },
                            onOpenAddTask = { viewModel.showAddTaskDialog(true) },
                            onAutoRebalance = { viewModel.autoRebalanceOverdue() }
                        )
                        "goals" -> GoalsScreen(
                            language = uiState.language,
                            goals = uiState.goals,
                            allTasks = uiState.allTasks,
                            onOpenAddGoal = { viewModel.showAddGoalDialog(true) },
                            onDeleteGoal = { viewModel.deleteGoal(it) },
                            onAiBreakdownGoal = { viewModel.breakdownGoalWithAi(it) }
                        )
                        "ai" -> AiAssistantScreen(
                            language = uiState.language,
                            isThinking = uiState.isAiThinking,
                            lastAiMessage = uiState.aiLastMessage,
                            suggestedTasks = uiState.aiSuggestedTasks,
                            aiLogs = uiState.aiLogs,
                            onAskAi = { viewModel.askAiAssistant(it) },
                            onApplySuggestedTasks = { viewModel.applyAiSuggestedTasks() }
                        )
                        "profile" -> ProfileScreen(
                            userProfile = uiState.userProfile,
                            language = uiState.language,
                            onLanguageChange = { viewModel.setLanguage(it) },
                            onUpdateProfile = { name, edu, hours, amb, bio ->
                                viewModel.updateProfile(name, edu, hours, amb, bio)
                            },
                            onLogout = { viewModel.authenticate(false) }
                        )
                    }
                }
            }
        }

        // Pomodoro Sheet
        if (uiState.isPomodoroSheetVisible) {
            PomodoroSheet(
                language = uiState.language,
                linkedTask = uiState.pomodoroLinkedTask,
                onSessionCompleted = { minutes, type ->
                    viewModel.logFocusSessionCompleted(minutes, type)
                },
                onDismiss = { viewModel.showPomodoroSheet(false) }
            )
        }

        // Add Goal Dialog
        if (uiState.isAddGoalDialogVisible) {
            AddGoalDialog(
                language = uiState.language,
                onDismiss = { viewModel.showAddGoalDialog(false) },
                onConfirm = { title, desc, category, ambition, hours ->
                    viewModel.addNewGoal(title, desc, category, ambition, hours)
                }
            )
        }

        // Add Task Dialog
        if (uiState.isAddTaskDialogVisible) {
            AddTaskDialog(
                language = uiState.language,
                goals = uiState.goals,
                onDismiss = { viewModel.showAddTaskDialog(false) },
                onConfirm = { title, desc, cat, pri, dur, date, goalId ->
                    viewModel.addNewTask(title, desc, cat, pri, dur, date, goalId)
                }
            )
        }
    }
}
