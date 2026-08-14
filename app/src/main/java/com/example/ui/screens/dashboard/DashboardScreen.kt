package com.example.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.GoalCategory
import com.example.data.local.model.GoalEntity
import com.example.data.local.model.PriorityLevel
import com.example.data.local.model.TaskEntity
import com.example.data.local.model.UserProfileEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.*
import com.example.ui.theme.*
import java.util.Calendar

@Composable
fun DashboardScreen(
    userProfile: UserProfileEntity,
    language: AppLanguage,
    goals: List<GoalEntity>,
    todayTasks: List<TaskEntity>,
    onToggleTaskComplete: (TaskEntity) -> Unit,
    onStartPomodoroForTask: (TaskEntity?) -> Unit,
    onOpenAddGoal: () -> Unit,
    onOpenAddTask: () -> Unit,
    onOpenAiPlan: () -> Unit,
    onAutoRebalance: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onNavigateToSchedule: () -> Unit
) {
    val greetingKey = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 4..11 -> "greeting_morning"
            in 12..16 -> "greeting_afternoon"
            else -> "greeting_evening"
        }
    }

    val completedCount = todayTasks.count { it.isCompleted }
    val totalCount = todayTasks.size
    val dailyProgress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedDailyProgress by animateFloatAsState(
        targetValue = dailyProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "dailyProgressAnim"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // 1. Clean Hero Greeting
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(LavenderPrimary, IceBlueAccent))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.firstOrNull()?.uppercase() ?: "A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark
                        )
                    }
                    Column {
                        Text(
                            text = "${Strings.get(greetingKey, language)}, ${userProfile.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${userProfile.educationField} • ${userProfile.dailyAvailableHours}h target",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LavenderPrimary.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = "${(dailyProgress * 100).toInt()}% Done",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 2. Main Goal Featured Card
        item {
            val mainGoal = goals.firstOrNull()
            val mainProgress = mainGoal?.progressPercentage ?: 0.72f
            val animatedGoalProgress by animateFloatAsState(
                targetValue = mainProgress,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
                label = "goalProgressAnim"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1C1B1F),
                                Color(0xFF252329)
                            )
                        )
                    )
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(24.dp))
                    .bounceClick { onNavigateToGoals() }
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(percent = 50),
                            color = LavenderPrimary.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "MAIN GOAL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = LavenderPrimary,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "${(animatedGoalProgress * 100).toInt()}% Complete",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = ElegantTextSecondary
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = mainGoal?.title ?: "Pass Exams with A+",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (!mainGoal?.description.isNullOrBlank()) mainGoal!!.description else Strings.get("app_tagline", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    // Luminous Smooth Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x1AFFFFFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedGoalProgress.coerceIn(0.01f, 1f))
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(LavenderPrimary)
                        )
                    }
                }
            }
        }

        // 3. AI Insight Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F).copy(alpha = 0.7f),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(LavenderPrimary.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Strings.get("ai_coach_tip", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.BENGALI) {
                                "আপনার সবচেয়ে গুরুত্বপূর্ণ কাজটি সকালের সেরা সময়ে সম্পন্ন করুন। পোমোডোরো স্প্রিন্ট সর্বোচ্চ মনোযোগ ধরে রাখে।"
                            } else {
                                "Focus on high-priority milestones during morning deep-work blocks. 25-minute sprints maximize retention."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = ElegantTextPrimary,
                            modifier = Modifier.padding(top = 3.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = LavenderPrimary.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.35f)),
                                modifier = Modifier.bounceClick { onAutoRebalance() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Filled.SyncAlt, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(15.dp))
                                    Text(
                                        text = Strings.get("rebalance_now", language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = LavenderPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Quick Action Pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Add Goal
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .bounceClick { onOpenAddGoal() }
                        .testTag("quick_add_goal_btn"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1C1B1F),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = Strings.get("btn_add_goal", language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ElegantTextPrimary
                        )
                    }
                }

                // Add Task
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .bounceClick { onOpenAddTask() }
                        .testTag("quick_add_task_btn"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1C1B1F),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.AddTask, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = Strings.get("btn_add_task", language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ElegantTextPrimary
                        )
                    }
                }

                // AI Coach
                Surface(
                    modifier = Modifier
                        .weight(1.1f)
                        .bounceClick { onOpenAiPlan() }
                        .testTag("quick_ai_plan_btn"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1C1B1F),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Psychology, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = Strings.get("btn_ai_suggest", language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                    }
                }
            }
        }

        // 5. Today's Dynamic Schedule Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Strings.get("today_focus", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(onClick = onNavigateToSchedule) {
                    Text(
                        text = "Schedule →",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary
                    )
                }
            }
        }

        // 6. Today's Task Items
        if (todayTasks.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = Strings.get("no_tasks_today", language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ElegantTextSecondary
                    )
                }
            }
        } else {
            items(todayTasks, key = { it.id }) { task ->
                DashboardTaskItem(
                    task = task,
                    language = language,
                    onToggleComplete = { onToggleTaskComplete(task) },
                    onStartPomodoro = { onStartPomodoroForTask(task) }
                )
            }
        }
    }
}

@Composable
fun DashboardTaskItem(
    task: TaskEntity,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onStartPomodoro: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}"),
        backgroundColor = if (task.isCompleted) Color(0xFF1C1B1F).copy(alpha = 0.55f) else Color(0xFF1C1B1F),
        borderColor = if (task.isCompleted) Color(0x12FFFFFF) else Color(0x1AFFFFFF)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Elegant Dark Animated Check Box Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        if (task.isCompleted) LavenderPrimary.copy(alpha = 0.2f) else Color.Transparent
                    )
                    .border(
                        width = 1.8.dp,
                        color = if (task.isCompleted) LavenderPrimary else Color(0x40FFFFFF),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .bounceClick { onToggleComplete() }
                    .testTag("task_checkbox_${task.id}"),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = task.isCompleted,
                    enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Completed",
                        tint = LavenderPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Title & Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                    color = if (task.isCompleted) ElegantTextSecondary else ElegantTextPrimary,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChip(category = task.category, language = language)
                    PriorityBadge(priority = task.priority, language = language)

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = Color(0x14FFFFFF)
                    ) {
                        Text(
                            text = "${task.durationMinutes}m",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (task.isAiGenerated) {
                        Surface(
                            modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                            color = LavenderPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = LavenderPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Quick Pomodoro trigger
            IconButton(
                onClick = onStartPomodoro,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(LavenderPrimary.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = Strings.get("start_pomodoro", language),
                    tint = LavenderPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

