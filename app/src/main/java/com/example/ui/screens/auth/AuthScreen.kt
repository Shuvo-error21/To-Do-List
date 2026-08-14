package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.components.GlassCard
import com.example.ui.components.LanguageToggleButton
import com.example.ui.components.bounceClick
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onLoginSuccess: (isGuest: Boolean) -> Unit
) {
    var email by remember { mutableStateOf("user@goalai.dev") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isSignUpMode by remember { mutableStateOf(false) }
    var showBiometricModal by remember { mutableStateOf(false) }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            CharcoalDark,
            Color(0xFF141419),
            Color(0xFF19181E)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with Language Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(LavenderPrimary, IceBlueAccent))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = CharcoalDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = "GoalAI",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ElegantTextPrimary
                    )
                }

                LanguageToggleButton(
                    currentLanguage = language,
                    onLanguageChange = onLanguageChange
                )
            }

            Spacer(Modifier.height(8.dp))

            // Hero Branding
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Strings.get("app_title", language),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = Strings.get("sign_in_subtitle", language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ElegantTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            // Auth Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1C1B1F),
                borderColor = Color(0x1AFFFFFF)
            ) {
                Text(
                    text = if (isSignUpMode) Strings.get("sign_up_button", language) else Strings.get("welcome_back", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )

                Spacer(Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(Strings.get("email_label", language), color = ElegantTextSecondary) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null, tint = LavenderPrimary)
                    },
                    modifier = Modifier
                        .testTag("email_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LavenderPrimary,
                        unfocusedBorderColor = Color(0x26FFFFFF),
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(Strings.get("password_label", language), color = ElegantTextSecondary) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, contentDescription = null, tint = LavenderPrimary)
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility",
                                tint = ElegantTextSecondary
                            )
                        }
                    },
                    modifier = Modifier
                        .testTag("password_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LavenderPrimary,
                        unfocusedBorderColor = Color(0x26FFFFFF),
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary
                    )
                )

                Spacer(Modifier.height(20.dp))

                // Submit Button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LavenderPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onLoginSuccess(false) }
                        .testTag("login_button")
                ) {
                    Box(modifier = Modifier.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isSignUpMode) Strings.get("sign_up_button", language) else Strings.get("sign_in_button", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Biometric / Quick Unlock Button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { showBiometricModal = true }
                        .testTag("biometric_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fingerprint,
                            contentDescription = "Biometric",
                            tint = LavenderPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = Strings.get("biometric_unlock_btn", language),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ElegantTextPrimary
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Toggle Sign Up vs Sign In
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUpMode) "Already have an account? " else "Need a new account? ",
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary
                    )
                    TextButton(onClick = { isSignUpMode = !isSignUpMode }) {
                        Text(
                            text = if (isSignUpMode) Strings.get("sign_in_button", language) else Strings.get("sign_up_button", language),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                    }
                }
            }

            // Divider & Guest Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0x1AFFFFFF))
                Text(text = Strings.get("or_divider", language), color = ElegantTextSecondary, style = MaterialTheme.typography.labelSmall)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0x1AFFFFFF))
            }

            // Guest Mode Action
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF1C1B1F),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick { onLoginSuccess(true) }
                    .testTag("guest_mode_button")
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.PersonOutline, contentDescription = null, tint = LavenderPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = Strings.get("guest_mode", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = ElegantTextPrimary
                    )
                }
            }

            Text(
                text = Strings.get("guest_disclaimer", language),
                style = MaterialTheme.typography.labelSmall,
                color = ElegantTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        // Biometric Unlock Simulation Dialog
        if (showBiometricModal) {
            AlertDialog(
                onDismissRequest = { showBiometricModal = false },
                containerColor = Color(0xFF1C1B1F),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Fingerprint, contentDescription = null, tint = LavenderPrimary)
                        Text(Strings.get("biometric_prompt_title", language), fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(LavenderPrimary.copy(alpha = 0.15f))
                                .border(2.dp, LavenderPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Fingerprint,
                                contentDescription = "Scan",
                                tint = LavenderPrimary,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                        Text(
                            text = Strings.get("biometric_prompt_subtitle", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElegantTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = LavenderPrimary,
                        modifier = Modifier.bounceClick {
                            showBiometricModal = false
                            onLoginSuccess(false)
                        }
                    ) {
                        Text(
                            text = "Unlock",
                            color = CharcoalDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBiometricModal = false }) {
                        Text("Cancel", color = ElegantTextSecondary)
                    }
                }
            )
        }
    }
}

