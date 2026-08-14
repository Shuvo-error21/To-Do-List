package com.example.ui.screens.assistant

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.AiLogEntity
import com.example.data.local.model.TaskEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.GlassCard
import com.example.ui.components.bounceClick
import com.example.ui.theme.*

@Composable
fun AiAssistantScreen(
    language: AppLanguage,
    isThinking: Boolean,
    lastAiMessage: String?,
    suggestedTasks: List<TaskEntity>,
    aiLogs: List<AiLogEntity>,
    onAskAi: (String) -> Unit,
    onApplySuggestedTasks: () -> Unit
) {
    var promptInput by remember { mutableStateOf("") }

    val quickChips = listOf(
        "ai_chip_sick",
        "ai_chip_lighten",
        "ai_chip_breakdown",
        "ai_chip_exam",
        "ai_chip_pomodoro"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LavenderPrimary.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = null,
                    tint = LavenderPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = Strings.get("ai_assistant_title", language),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Intelligent Study & Task Assistant",
                    style = MaterialTheme.typography.bodySmall,
                    color = ElegantTextSecondary
                )
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            items(quickChips) { chipKey ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1C1B1F),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF)),
                    modifier = Modifier.bounceClick { onAskAi(Strings.get(chipKey, language)) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = Strings.get(chipKey, language),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = ElegantTextPrimary
                        )
                    }
                }
            }
        }

        // Main Conversation & Output Area
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Thinking status indicator
            if (isThinking) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LavenderPrimary.copy(alpha = 0.1f),
                        borderColor = LavenderPrimary.copy(alpha = 0.3f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = LavenderPrimary,
                                strokeWidth = 2.5.dp
                            )
                            Text(
                                text = Strings.get("ai_thinking", language),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = LavenderPrimary
                            )
                        }
                    }
                }
            }

            // Latest Response & Suggested Tasks Card
            if (lastAiMessage != null) {
                item {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_response_card"),
                        backgroundColor = Color(0xFF1C1B1F),
                        borderColor = LavenderPrimary.copy(alpha = 0.35f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Recommendations",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LavenderPrimary
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = lastAiMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElegantTextPrimary,
                            lineHeight = 22.sp
                        )

                        if (suggestedTasks.isNotEmpty()) {
                            Spacer(Modifier.height(14.dp))
                            Text(
                                text = "Suggested Tasks (${suggestedTasks.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = LavenderPrimary
                            )

                            suggestedTasks.forEach { task ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "${task.title} (${task.durationMinutes}m)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ElegantTextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LavenderPrimary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bounceClick { onApplySuggestedTasks() }
                                    .testTag("btn_apply_ai_tasks")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.DoneAll, contentDescription = null, tint = CharcoalDark, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = Strings.get("apply_ai_changes", language),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CharcoalDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Past AI Logs
            if (aiLogs.isNotEmpty()) {
                item {
                    Text(
                        text = "History & Reasoning",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ElegantTextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(aiLogs, key = { it.id }) { log ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF1C1B1F).copy(alpha = 0.5f),
                        borderColor = Color(0x1AFFFFFF)
                    ) {
                        Text(
                            text = "💬 ${log.prompt}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ElegantTextPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = log.response,
                            style = MaterialTheme.typography.bodySmall,
                            color = ElegantTextSecondary,
                            maxLines = 3
                        )
                    }
                }
            }
        }

        // Input Field and Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 76.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text(Strings.get("ai_chat_placeholder", language), color = ElegantTextMuted) },
                modifier = Modifier
                    .testTag("ai_prompt_input")
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LavenderPrimary,
                    unfocusedBorderColor = Color(0x26FFFFFF),
                    focusedTextColor = ElegantTextPrimary,
                    unfocusedTextColor = ElegantTextPrimary,
                    focusedContainerColor = Color(0xFF1C1B1F),
                    unfocusedContainerColor = Color(0xFF1C1B1F)
                )
            )

            Box(
                modifier = Modifier
                    .testTag("ai_send_button")
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LavenderPrimary)
                    .bounceClick {
                        if (promptInput.isNotBlank()) {
                            val text = promptInput
                            promptInput = ""
                            onAskAi(text)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = CharcoalDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

