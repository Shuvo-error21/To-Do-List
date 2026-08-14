package com.example.ui.screens.profile

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.local.model.AmbitionLevel
import com.example.data.local.model.UserProfileEntity
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.GlassCard
import com.example.ui.components.bounceClick
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onUpdateProfile: (name: String, education: String, dailyHours: Float, ambition: AmbitionLevel, biometric: Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Header
        item {
            Text(
                text = Strings.get("profile_title", language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Profile Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(LavenderPrimary, IceBlueAccent))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.firstOrNull()?.uppercase() ?: "A",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ElegantTextPrimary
                        )
                        Text(
                            text = userProfile.educationField,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LavenderPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${userProfile.dailyAvailableHours} ${Strings.get("hours", language)}/day • ${userProfile.ambitionLevel.name.lowercase().replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = LavenderPrimary.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.bounceClick { showEditDialog = true }
                    ) {
                        Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Language & Localization Settings
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Text(
                    text = Strings.get("language_setting", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isEn = language == AppLanguage.ENGLISH
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick { onLanguageChange(AppLanguage.ENGLISH) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isEn) LavenderPrimary else Color(0x14FFFFFF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isEn) LavenderPrimary else Color(0x1AFFFFFF))
                    ) {
                        Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = Strings.get("english", language),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isEn) CharcoalDark else ElegantTextPrimary
                            )
                        }
                    }

                    val isBn = language == AppLanguage.BENGALI
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick { onLanguageChange(AppLanguage.BENGALI) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isBn) LavenderPrimary else Color(0x14FFFFFF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isBn) LavenderPrimary else Color(0x1AFFFFFF))
                    ) {
                        Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = Strings.get("bengali", language),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isBn) CharcoalDark else ElegantTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Biometric Security Toggle
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LavenderPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Fingerprint, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(
                                text = Strings.get("biometric_security", language),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = ElegantTextPrimary
                            )
                            Text(
                                text = "Biometric lock on app launch",
                                style = MaterialTheme.typography.labelSmall,
                                color = ElegantTextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = userProfile.biometricEnabled,
                        onCheckedChange = { checked ->
                            onUpdateProfile(
                                userProfile.name,
                                userProfile.educationField,
                                userProfile.dailyAvailableHours,
                                userProfile.ambitionLevel,
                                checked
                            )
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CharcoalDark,
                            checkedTrackColor = LavenderPrimary,
                            uncheckedThumbColor = ElegantTextMuted,
                            uncheckedTrackColor = Color(0x26FFFFFF)
                        )
                    )
                }
            }
        }

        // Productivity Analytics Visualizer
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Text(
                    text = Strings.get("analytics_overview", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )

                Spacer(Modifier.height(14.dp))

                // Weekly completion bar charts
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val heights = listOf(0.8f, 0.9f, 0.6f, 1.0f, 0.85f, 0.7f, 0.95f)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    days.forEachIndexed { idx, day ->
                        val barHeight by animateFloatAsState(
                            targetValue = heights[idx] * 70,
                            animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
                            label = "bar_$day"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(barHeight.dp)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(LavenderPrimary.copy(alpha = if (idx == 3) 1f else 0.45f))
                            )
                            Text(
                                text = day,
                                style = MaterialTheme.typography.labelSmall,
                                color = ElegantTextSecondary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${userProfile.totalCompletedTasks} Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                        Text(
                            text = Strings.get("weekly_completion_rate", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${userProfile.totalFocusMinutes} min",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberStreak
                        )
                        Text(
                            text = Strings.get("study_hours_logged", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }
                }
            }
        }

        // Data Storage Info
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F).copy(alpha = 0.5f),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Storage, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(20.dp))
                    Column {
                        Text(
                            text = Strings.get("export_data", language),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = ElegantTextPrimary
                        )
                        Text(
                            text = Strings.get("saved_locally", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary
                        )
                    }
                }
            }
        }

        // Logout
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick { onLogout() },
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(1.dp, RosePriority.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null, tint = RosePriority, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = Strings.get("logout", language),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        color = RosePriority
                    )
                }
            }
        }
    }

    // Edit Profile Modal
    if (showEditDialog) {
        var editName by remember { mutableStateOf(userProfile.name) }
        var editEducation by remember { mutableStateOf(userProfile.educationField) }
        var editHours by remember { mutableFloatStateOf(userProfile.dailyAvailableHours) }
        var editAmbition by remember { mutableStateOf(userProfile.ambitionLevel) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = Color(0xFF1C1B1F),
            title = {
                Text(Strings.get("profile_setup_title", language), fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text(Strings.get("name_label", language), color = ElegantTextSecondary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LavenderPrimary,
                            unfocusedBorderColor = Color(0x26FFFFFF),
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary
                        )
                    )

                    OutlinedTextField(
                        value = editEducation,
                        onValueChange = { editEducation = it },
                        label = { Text(Strings.get("education_profession_label", language), color = ElegantTextSecondary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LavenderPrimary,
                            unfocusedBorderColor = Color(0x26FFFFFF),
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary
                        )
                    )

                    Column {
                        Text(
                            text = "${Strings.get("daily_hours_label", language)}: ${String.format("%.1f", editHours)}h",
                            style = MaterialTheme.typography.labelMedium,
                            color = ElegantTextSecondary
                        )
                        Slider(
                            value = editHours,
                            onValueChange = { editHours = it },
                            valueRange = 1f..12f,
                            steps = 21,
                            colors = SliderDefaults.colors(
                                thumbColor = LavenderPrimary,
                                activeTrackColor = LavenderPrimary,
                                inactiveTrackColor = Color(0x26FFFFFF)
                            )
                        )
                    }

                    Column {
                        Text(
                            text = Strings.get("ambition_level_label", language),
                            style = MaterialTheme.typography.labelMedium,
                            color = ElegantTextSecondary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AmbitionLevel.values().forEach { level ->
                                val isSelected = level == editAmbition
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) LavenderPrimary else Color(0x14FFFFFF),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) LavenderPrimary else Color(0x1AFFFFFF)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .bounceClick { editAmbition = level }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = level.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) CharcoalDark else ElegantTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = LavenderPrimary,
                    modifier = Modifier.bounceClick {
                        onUpdateProfile(
                            editName,
                            editEducation,
                            editHours,
                            editAmbition,
                            userProfile.biometricEnabled
                        )
                        showEditDialog = false
                    }
                ) {
                    Text(
                        text = Strings.get("save_profile", language),
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = ElegantTextSecondary)
                }
            }
        )
    }
}

