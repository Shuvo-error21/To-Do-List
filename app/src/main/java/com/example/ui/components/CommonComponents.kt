package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.GoalCategory
import com.example.data.local.model.PriorityLevel
import com.example.data.localization.AppLanguage
import com.example.data.localization.Strings
import com.example.ui.theme.*

/**
 * Smooth spring-based bounce click modifier for native iOS/Android fluidity.
 */
fun Modifier.bounceClick(
    scaleDown: Float = 0.96f,
    onClick: (() -> Unit)? = null
): Modifier = this.then(
    Modifier.composed {
        var isPressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isPressed) scaleDown else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "bounceScale"
        )

        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(onClick) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown(requireUnconsumed = false)
                        isPressed = true
                        val up = waitForUpOrCancellation()
                        isPressed = false
                        if (up != null && onClick != null) {
                            onClick()
                        }
                    }
                }
            }
    }
)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val baseModifier = modifier
        .shadow(4.dp, shape, spotColor = LavenderPrimary.copy(alpha = 0.08f), ambientColor = Color.Black)
        .clip(shape)
        .background(backgroundColor)
        .border(1.dp, borderColor, shape)

    val finalModifier = if (onClick != null) {
        baseModifier.bounceClick(onClick = onClick)
    } else {
        baseModifier
    }

    Column(
        modifier = finalModifier.padding(18.dp),
        content = content
    )
}

@Composable
fun StreakBadge(
    streakDays: Int,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flame")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flamePulse"
    )

    Surface(
        modifier = modifier
            .testTag("streak_badge")
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AmberStreak.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
        color = AmberStreak.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = "Streak",
                tint = AmberStreak,
                modifier = Modifier
                    .size(18.dp)
                    .graphicsLayer {
                        scaleX = flameScale
                        scaleY = flameScale
                    }
            )
            Text(
                text = "$streakDays ${Strings.get("streak_days", language)}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = AmberStreak
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: GoalCategory,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (labelKey, icon, color) = when (category) {
        GoalCategory.STUDY -> Triple("cat_study", Icons.Filled.MenuBook, LavenderPrimary)
        GoalCategory.CODING -> Triple("cat_coding", Icons.Filled.Code, IceBlueAccent)
        GoalCategory.CAREER -> Triple("cat_career", Icons.Filled.Work, VioletAccent)
        GoalCategory.HEALTH -> Triple("cat_health", Icons.Filled.FitnessCenter, EmeraldSuccess)
        GoalCategory.PERSONAL -> Triple("cat_personal", Icons.Filled.SelfImprovement, AmberStreak)
    }

    Surface(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = Strings.get(labelKey, language),
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PriorityBadge(
    priority: PriorityLevel,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (key, color) = when (priority) {
        PriorityLevel.HIGH -> "priority_high" to RosePriority
        PriorityLevel.MEDIUM -> "priority_medium" to AmberStreak
        PriorityLevel.LOW -> "priority_low" to EmeraldSuccess
    }

    Surface(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = Strings.get(key, language),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun LanguageToggleButton(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .testTag("language_toggle_btn")
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, LavenderPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .bounceClick {
                val nextLang = if (currentLanguage == AppLanguage.ENGLISH) AppLanguage.BENGALI else AppLanguage.ENGLISH
                onLanguageChange(nextLang)
            },
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Translate,
                contentDescription = "Language Switch",
                tint = LavenderPrimary,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = if (currentLanguage == AppLanguage.ENGLISH) "EN | বাংলা" else "বাংলা | EN",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
