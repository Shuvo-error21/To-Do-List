package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.model.AmbitionLevel
import com.example.data.local.model.GoalCategory
import com.example.data.local.model.GoalEntity
import com.example.data.local.model.PriorityLevel
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.data.repository.GoalTrackerRepository

@Composable
fun AddGoalDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (title: String, desc: String, category: GoalCategory, ambition: AmbitionLevel, hours: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(GoalCategory.STUDY) }
    var selectedAmbition by remember { mutableStateOf(AmbitionLevel.MODERATE) }
    var estimatedHours by remember { mutableFloatStateOf(40f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(Strings.get("create_goal", language), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    placeholder = { Text(Strings.get("goal_title_hint", language)) },
                    modifier = Modifier
                        .testTag("new_goal_title_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Why is this important?") },
                    placeholder = { Text(Strings.get("goal_desc_hint", language)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = Strings.get("goal_category", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GoalCategory.values().forEach { cat ->
                        FilterChip(
                            selected = cat == selectedCategory,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.name, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Column {
                    Text(
                        text = "Estimated Total Effort: ${estimatedHours.toInt()} hrs",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Slider(
                        value = estimatedHours,
                        onValueChange = { estimatedHours = it },
                        valueRange = 10f..200f,
                        steps = 18
                    )
                }

                Column {
                    Text(
                        text = Strings.get("ambition_level_label", language),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AmbitionLevel.values().forEach { amb ->
                            FilterChip(
                                selected = amb == selectedAmbition,
                                onClick = { selectedAmbition = amb },
                                label = { Text(amb.name, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, selectedCategory, selectedAmbition, estimatedHours.toInt())
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text(Strings.get("create_goal", language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddTaskDialog(
    language: AppLanguage,
    goals: List<GoalEntity>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, desc: String, category: GoalCategory, priority: PriorityLevel, duration: Int, dueDate: String, goalId: String?) -> Unit
) {
    val today = remember { GoalTrackerRepository.getTodayFormattedDate() }
    val tomorrow = remember { GoalTrackerRepository.getOffsetFormattedDate(1) }
    val dayAfter = remember { GoalTrackerRepository.getOffsetFormattedDate(2) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(GoalCategory.STUDY) }
    var selectedPriority by remember { mutableStateOf(PriorityLevel.MEDIUM) }
    var selectedDuration by remember { mutableIntStateOf(45) }
    var selectedDueDate by remember { mutableStateOf(today) }
    var selectedGoalId by remember { mutableStateOf<String?>(goals.firstOrNull()?.id) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(Strings.get("btn_add_task", language), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier
                        .testTag("new_task_title_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Notes / Sub-steps") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Date Selector
                Text(text = "Schedule Date", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val dateOptions = listOf("Today" to today, "Tomorrow" to tomorrow, "+2 Days" to dayAfter)
                    dateOptions.forEach { (label, dateVal) ->
                        FilterChip(
                            selected = selectedDueDate == dateVal,
                            onClick = { selectedDueDate = dateVal },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                // Priority
                Text(text = "Priority Level", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PriorityLevel.values().forEach { pri ->
                        FilterChip(
                            selected = selectedPriority == pri,
                            onClick = { selectedPriority = pri },
                            label = { Text(pri.name, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                // Duration Selector
                Text(text = "Estimated Duration: ${selectedDuration} min", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val durations = listOf(15, 25, 45, 60, 90)
                    durations.forEach { dur ->
                        FilterChip(
                            selected = selectedDuration == dur,
                            onClick = { selectedDuration = dur },
                            label = { Text("${dur}m", style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, selectedCategory, selectedPriority, selectedDuration, selectedDueDate, selectedGoalId)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text(Strings.get("btn_add_task", language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
