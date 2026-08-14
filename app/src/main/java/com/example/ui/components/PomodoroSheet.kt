package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.TaskEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class PomodoroMode(val minutes: Int, val stringKey: String) {
    FOCUS(25, "focus_session"),
    SHORT_BREAK(5, "short_break"),
    LONG_BREAK(15, "long_break")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSheet(
    language: AppLanguage,
    linkedTask: TaskEntity? = null,
    onSessionCompleted: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(PomodoroMode.FOCUS) }
    var totalSeconds by remember(selectedMode) { mutableIntStateOf(selectedMode.minutes * 60) }
    var remainingSeconds by remember(selectedMode) { mutableIntStateOf(selectedMode.minutes * 60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else if (isRunning && remainingSeconds == 0) {
            isRunning = false
            onSessionCompleted(selectedMode.minutes, selectedMode.name)
        }
    }

    val progress = remember(remainingSeconds, totalSeconds) {
        if (totalSeconds > 0) (remainingSeconds.toFloat() / totalSeconds.toFloat()) else 0f
    }
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "pomodoro_progress")

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = Strings.get(selectedMode.stringKey, language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (linkedTask != null) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "🎯 ${linkedTask.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Mode Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PomodoroMode.values().forEach { mode ->
                    val isSelected = mode == selectedMode
                    val activeBg = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(activeBg)
                            .clickable {
                                if (!isRunning) {
                                    selectedMode = mode
                                }
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${mode.minutes}m",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            // Circular Clock Progress Visualizer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(210.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    strokeWidth = 12.dp
                )

                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = if (selectedMode == PomodoroMode.FOCUS) IndigoPrimary else EmeraldSuccess,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isRunning) "Focus Active" else "Ready",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isRunning) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isRunning = false
                        remainingSeconds = selectedMode.minutes * 60
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier
                        .testTag("pomodoro_play_btn")
                        .height(54.dp)
                        .widthIn(min = 150.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) RosePriority else IndigoPrimary
                    )
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) Strings.get("timer_pause", language) else Strings.get("timer_start", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
