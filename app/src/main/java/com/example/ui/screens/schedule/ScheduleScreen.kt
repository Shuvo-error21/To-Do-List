package com.example.ui.screens.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.TaskEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.data.repository.GoalTrackerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ScheduleScreen(
    language: AppLanguage,
    todayTasks: List<TaskEntity>,
    allTasks: List<TaskEntity>,
    onToggleComplete: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onStartPomodoro: (TaskEntity) -> Unit,
    onOpenAddTask: () -> Unit,
    onAutoRebalance: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Today, 1: Upcoming, 2: All
    val today = remember { GoalTrackerRepository.getTodayFormattedDate() }

    val overdueTasks = remember(allTasks, today) {
        allTasks.filter { !it.isCompleted && it.dueDate < today }
    }

    val displayedTasks = remember(selectedTab, todayTasks, allTasks, today) {
        when (selectedTab) {
            0 -> todayTasks
            1 -> allTasks.filter { it.dueDate > today }
            else -> allTasks
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenAddTask,
                containerColor = LavenderPrimary,
                contentColor = CharcoalDark,
                shape = CircleShape,
                modifier = Modifier
                    .testTag("fab_add_task")
                    .padding(bottom = 64.dp)
                    .bounceClick { onOpenAddTask() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Strings.get("schedule_title", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = today,
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary
                    )
                }

                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .bounceClick { onAutoRebalance() },
                    color = LavenderPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f))
                ) {
                    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.AutoMode,
                            contentDescription = "Auto Rebalance",
                            tint = LavenderPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Overdue Tasks Alert if any
            if (overdueTasks.isNotEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = RosePriority.copy(alpha = 0.12f),
                    borderColor = RosePriority.copy(alpha = 0.35f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Filled.Warning, contentDescription = null, tint = RosePriority, modifier = Modifier.size(22.dp))
                        Text(
                            text = "${overdueTasks.size} ${Strings.get("overdue_alert", language)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = RosePriority,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RosePriority.copy(alpha = 0.2f),
                            modifier = Modifier.bounceClick { onAutoRebalance() }
                        ) {
                            Text(
                                text = Strings.get("rebalance_now", language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = RosePriority,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Tab Switcher with animated pill highlight
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1C1B1F))
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val tabTitles = listOf("today_tab", "upcoming_tab", "all_tasks_tab")
                tabTitles.forEachIndexed { index, titleKey ->
                    val isSelected = selectedTab == index
                    val animatedBg by animateColorAsState(
                        targetValue = if (isSelected) LavenderPrimary else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "tabBg"
                    )
                    val animatedText by animateColorAsState(
                        targetValue = if (isSelected) CharcoalDark else ElegantTextSecondary,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "tabText"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(animatedBg)
                            .bounceClick { selectedTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Strings.get(titleKey, language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = animatedText
                        )
                    }
                }
            }

            // Task List
            if (displayedTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Checklist,
                            contentDescription = null,
                            tint = ElegantTextMuted,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = Strings.get("no_tasks_today", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElegantTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(displayedTasks, key = { it.id }) { task ->
                        DetailedTaskCard(
                            task = task,
                            language = language,
                            onToggleComplete = { onToggleComplete(task) },
                            onDelete = { onDeleteTask(task) },
                            onStartPomodoro = { onStartPomodoro(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedTaskCard(
    task: TaskEntity,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onStartPomodoro: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("detailed_task_${task.id}"),
        backgroundColor = if (task.isCompleted) Color(0xFF1C1B1F).copy(alpha = 0.55f) else Color(0xFF1C1B1F),
        borderColor = if (task.isCompleted) Color(0x12FFFFFF) else Color(0x1AFFFFFF)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Animated Checkbox
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
                    .testTag("detailed_task_checkbox_${task.id}"),
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                    color = if (task.isCompleted) ElegantTextSecondary else ElegantTextPrimary,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextMuted,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

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
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = ElegantTextSecondary, modifier = Modifier.size(11.dp))
                            Text(text = "${task.durationMinutes}m", style = MaterialTheme.typography.labelSmall, color = ElegantTextSecondary)
                        }
                    }

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = Color(0x14FFFFFF)
                    ) {
                        Text(
                            text = task.dueDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Expandable Notes
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        if (task.notes.isNotBlank()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                color = Color(0x14FFFFFF)
                            ) {
                                Text(
                                    text = "📝 ${task.notes}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ElegantTextPrimary,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = LavenderPrimary.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.35f)),
                                modifier = Modifier.bounceClick { onStartPomodoro() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Filled.Timer, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(15.dp))
                                    Text(
                                        text = Strings.get("pomodoro_timer", language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = LavenderPrimary
                                    )
                                }
                            }

                            IconButton(onClick = onDelete) {
                                Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = RosePriority)
                            }
                        }
                    }
                }
            }
        }
    }
}
