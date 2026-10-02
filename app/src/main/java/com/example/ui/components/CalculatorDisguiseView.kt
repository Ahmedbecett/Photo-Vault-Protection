package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorDisguiseView(
    secretPin: String,
    decoyPin: String,
    onSecretMatched: (isDecoy: Boolean) -> Unit,
    onExitDisguise: () -> Unit
) {
    val view = LocalView.current
    var displayText by remember { mutableStateOf("0") }
    var inputBuffer by remember { mutableStateOf("") }
    var currentOp by remember { mutableStateOf<String?>(null) }
    var firstOperand by remember { mutableStateOf(0.0) }
    var isNewNumber by remember { mutableStateOf(true) }

    fun onDigit(d: String) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        inputBuffer += d
        if (isNewNumber || displayText == "0") {
            displayText = d
            isNewNumber = false
        } else {
            displayText += d
        }
    }

    fun onOperator(op: String) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        firstOperand = displayText.toDoubleOrNull() ?: 0.0
        currentOp = op
        isNewNumber = true
    }

    fun onEquals() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        // Check secret pin in input buffer or displayText
        if (inputBuffer == secretPin || displayText == secretPin) {
            onSecretMatched(false)
            return
        }
        if (inputBuffer == decoyPin || displayText == decoyPin) {
            onSecretMatched(true)
            return
        }

        val secondOperand = displayText.toDoubleOrNull() ?: 0.0
        val result = when (currentOp) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×" -> firstOperand * secondOperand
            "÷" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            else -> secondOperand
        }

        displayText = if (result.isNaN()) "خطأ" else {
            if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                String.format("%.4f", result).trimEnd('0').trimEnd('.')
            }
        }
        inputBuffer = ""
        currentOp = null
        isNewNumber = true
    }

    fun onClear() {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        displayText = "0"
        inputBuffer = ""
        firstOperand = 0.0
        currentOp = null
        isNewNumber = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Discreet top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "الآلة الحاسبة",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
            IconButton(onClick = onExitDisguise) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "قفل الخزنة",
                    tint = Color(0xFF475569)
                )
            }
        }

        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayText,
                fontSize = if (displayText.length > 8) 38.sp else 54.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }

        // Keypad
        val calcRows = listOf(
            listOf("C", "±", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        for (row in calcRows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (btn in row) {
                    val isZero = btn == "0"
                    val weight = if (isZero) 2.1f else 1f

                    val (bg, fg) = when (btn) {
                        "÷", "×", "-", "+", "=" -> Pair(Color(0xFFF59E0B), Color.White) // Amber orange
                        "C", "±", "%" -> Pair(Color(0xFF334155), Color.White)
                        else -> Pair(Color(0xFF1E293B), Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .weight(weight)
                            .height(68.dp)
                            .clip(RoundedCornerShape(34.dp))
                            .background(bg)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true),
                                onClick = {
                                    when (btn) {
                                        "C" -> onClear()
                                        "±" -> {
                                            displayText = if (displayText.startsWith("-")) displayText.drop(1) else "-$displayText"
                                        }
                                        "%" -> {
                                            val v = (displayText.toDoubleOrNull() ?: 0.0) / 100.0
                                            displayText = v.toString()
                                        }
                                        "÷", "×", "-", "+" -> onOperator(btn)
                                        "=" -> onEquals()
                                        "." -> {
                                            if (!displayText.contains(".")) {
                                                displayText += "."
                                                isNewNumber = false
                                            }
                                        }
                                        else -> onDigit(btn)
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = btn,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            color = fg
                        )
                    }
                }
            }
        }
    }
}
