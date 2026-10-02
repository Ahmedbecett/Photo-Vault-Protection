package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LockType
import com.example.ui.components.PatternLockView
import com.example.ui.components.PinLockView

@Composable
fun LockScreen(
    activeLockType: LockType,
    currentPin: String,
    isError: Boolean,
    failedAttempts: Int,
    isBiometricAvailable: Boolean,
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onPatternComplete: (List<Int>) -> Unit,
    onBiometricClick: () -> Unit,
    onSwitchLockType: (LockType) -> Unit,
    onOpenCalculator: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // Slate 900
                        Color(0xFF0A0F1D), // Dark Vault Obsidian
                        Color(0xFF020617)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: App Header & Vault Emblem
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top controls (Calculator stealth mode)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onOpenCalculator,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B).copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "تمويه الآلة الحاسبة",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }

                // Glowing Vault Icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF10B981).copy(alpha = 0.25f),
                                    Color(0xFF06B6D4).copy(alpha = 0.15f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(38.dp)
                    )
                }

                Text(
                    text = "خزنة الصور والفيديو",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = if (activeLockType == LockType.PIN) "أدخل رمز PIN لفك القفل" else "ارسم نمط الشاشة (شيما)",
                    fontSize = 14.sp,
                    color = if (isError) Color(0xFFEF4444) else Color(0xFF94A3B8),
                    textAlign = TextAlign.Center
                )

                // Failed attempts warning banner
                AnimatedVisibility(
                    visible = failedAttempts > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        color = Color(0xFFEF4444).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "محاولة غير مصرح بها ($failedAttempts) - تم تسجيل التوقيت",
                                color = Color(0xFFEF4444),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Middle Section: Lock Mechanism (PIN vs Pattern)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (activeLockType == LockType.PIN) {
                    PinLockView(
                        currentPin = currentPin,
                        isError = isError,
                        showBiometricButton = isBiometricAvailable,
                        onNumberClick = onNumberClick,
                        onDeleteClick = onDeleteClick,
                        onBiometricClick = onBiometricClick
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PatternLockView(
                            isError = isError,
                            onPatternComplete = onPatternComplete
                        )

                        if (isBiometricAvailable) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onBiometricClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1E293B)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = Color(0xFF06B6D4),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "فتح بالبصمة أو الوجه",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Section: Lock Type Switcher Tab
            Surface(
                color = Color(0xFF1E293B).copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        selected = activeLockType == LockType.PIN,
                        onClick = { onSwitchLockType(LockType.PIN) },
                        label = { Text("رمز PIN") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Pin,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF10B981),
                            selectedLabelColor = Color.Black,
                            selectedLeadingIconColor = Color.Black,
                            containerColor = Color.Transparent,
                            labelColor = Color(0xFF94A3B8),
                            iconColor = Color(0xFF94A3B8)
                        )
                    )

                    FilterChip(
                        selected = activeLockType == LockType.PATTERN,
                        onClick = { onSwitchLockType(LockType.PATTERN) },
                        label = { Text("نمط الشاشة (شيما)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Gesture,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF10B981),
                            selectedLabelColor = Color.Black,
                            selectedLeadingIconColor = Color.Black,
                            containerColor = Color.Transparent,
                            labelColor = Color(0xFF94A3B8),
                            iconColor = Color(0xFF94A3B8)
                        )
                    )
                }
            }
        }
    }
}
