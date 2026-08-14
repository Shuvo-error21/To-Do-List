package com.example.ui.screens.goals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.GoalEntity
import com.example.data.local.model.MilestoneEntity
import com.example.data.local.model.TaskEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun GoalsScreen(
    language: AppLanguage,
    goals: List<GoalEntity>,
    allTasks: List<TaskEntity>,
    onOpenAddGoal: () -> Unit,
    onDeleteGoal: (GoalEntity) -> Unit,
    onAiBreakdownGoal: (GoalEntity) -> Unit
) {
    var selectedGoalForDetails by remember { mutableStateOf<GoalEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenAddGoal,
                containerColor = LavenderPrimary,
                contentColor = CharcoalDark,
                shape = CircleShape,
                modifier = Modifier
                    .testTag("fab_add_goal")
                    .padding(bottom = 64.dp)
                    .bounceClick { onOpenAddGoal() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Goal")
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
                        text = Strings.get("goals_header", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${goals.size} Active Goals",
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LavenderPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "${goals.count { it.progressPercentage >= 1f }} Completed",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            if (goals.isEmpty()) {
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
                            imageVector = Icons.Outlined.Flag,
                            contentDescription = null,
                            tint = ElegantTextMuted,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = Strings.get("no_goals", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElegantTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(goals, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            language = language,
                            linkedTasksCount = allTasks.count { it.goalId == goal.id },
                            onCardClick = { selectedGoalForDetails = goal },
                            onAiBreakdown = { onAiBreakdownGoal(goal) },
                            onDelete = { onDeleteGoal(goal) }
                        )
                    }
                }
            }
        }

        // Goal Detail Dialog
        selectedGoalForDetails?.let { goal ->
            val linkedTasks = allTasks.filter { it.goalId == goal.id }
            GoalDetailModal(
                goal = goal,
                language = language,
                tasks = linkedTasks,
                onAiBreakdown = {
                    onAiBreakdownGoal(goal)
                },
                onDismiss = { selectedGoalForDetails = null }
            )
        }
    }
}

@Composable
fun GoalCard(
    goal: GoalEntity,
    language: AppLanguage,
    linkedTasksCount: Int,
    onCardClick: () -> Unit,
    onAiBreakdown: () -> Unit,
    onDelete: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = goal.progressPercentage,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
        label = "goal_progress"
    )

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_card_${goal.id}"),
        onClick = onCardClick,
        backgroundColor = Color(0xFF1C1B1F),
        borderColor = Color(0x1AFFFFFF)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Circular Progress Indicator
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(56.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0x1AFFFFFF),
                    strokeWidth = 5.dp
                )
                CircularProgressIndicator(
                    progress = { animatedProgress.coerceIn(0.01f, 1f) },
                    modifier = Modifier.fillMaxSize(),
                    color = LavenderPrimary,
                    strokeWidth = 5.dp,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(goal.progressPercentage * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )
            }

            // Goal Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )

                if (goal.description.isNotBlank()) {
                    Text(
                        text = goal.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChip(category = goal.category, language = language)

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = Color(0x14FFFFFF)
                    ) {
                        Text(
                            text = "${goal.totalEstimatedHours}h est",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = Color(0x14FFFFFF)
                    ) {
                        Text(
                            text = "$linkedTasksCount tasks",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = ElegantTextMuted)
            }
        }

        Spacer(Modifier.height(12.dp))

        // AI Smart Breakdown Button
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = LavenderPrimary.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick { onAiBreakdown() }
                .testTag("btn_breakdown_${goal.id}")
        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = Strings.get("btn_ai_breakdown", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = LavenderPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailModal(
    goal: GoalEntity,
    language: AppLanguage,
    tasks: List<TaskEntity>,
    onAiBreakdown: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1B1F),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                CategoryChip(category = goal.category, language = language)
            }

            if (goal.description.isNotBlank()) {
                Text(
                    text = goal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ElegantTextSecondary
                )
            }

            LinearProgressIndicator(
                progress = { goal.progressPercentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LavenderPrimary,
                trackColor = Color(0x1AFFFFFF)
            )

            Text(
                text = "${(goal.progressPercentage * 100).toInt()}% Completed • ${goal.totalEstimatedHours}h Target",
                style = MaterialTheme.typography.labelMedium,
                color = LavenderPrimary,
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider(color = Color(0x1AFFFFFF))

            Text(
                text = "Linked Daily Tasks (${tasks.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ElegantTextPrimary
            )

            if (tasks.isEmpty()) {
                Text(
                    text = "No daily tasks linked yet. Use 'AI Smart Breakdown' to auto-generate milestones and daily action steps!",
                    style = MaterialTheme.typography.bodySmall,
                    color = ElegantTextSecondary
                )
            } else {
                tasks.forEach { task ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) EmeraldSuccess else ElegantTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElegantTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = task.dueDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = LavenderPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick { onAiBreakdown() }
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = CharcoalDark, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = Strings.get("btn_ai_breakdown", language),
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

