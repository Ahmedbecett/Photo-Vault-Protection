package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.math.min

@Composable
fun PatternLockView(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    onPatternComplete: (List<Int>) -> Unit,
    onPatternStart: () -> Unit = {}
) {
    val view = LocalView.current
    val selectedNodes = remember { mutableStateListOf<Int>() }
    var currentTouch by remember { mutableStateOf<Offset?>(null) }

    // Clear nodes on error
    LaunchedEffect(isError) {
        if (isError) {
            // Keep error visible briefly
        }
    }

    val primaryColor = if (isError) Color(0xFFEF4444) else Color(0xFF10B981) // Red on error, Emerald on normal
    val normalDotColor = Color(0xFF334155) // Slate
    val normalCenterColor = Color(0xFF64748B)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { offset ->
                        selectedNodes.clear()
                        currentTouch = offset
                        onPatternStart()

                        // Check if started on a node
                        val boxSize = min(size.width, size.height)
                        val padding = boxSize * 0.16f
                        val cellSpacing = (boxSize - 2 * padding) / 2f
                        val hitRadius = cellSpacing * 0.45f

                        for (i in 0 until 9) {
                            val row = i / 3
                            val col = i % 3
                            val cx = padding + col * cellSpacing
                            val cy = padding + row * cellSpacing
                            if (hypot(offset.x - cx, offset.y - cy) <= hitRadius) {
                                selectedNodes.add(i)
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                break
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        currentTouch = change.position

                        val boxSize = min(size.width, size.height)
                        val padding = boxSize * 0.16f
                        val cellSpacing = (boxSize - 2 * padding) / 2f
                        val hitRadius = cellSpacing * 0.45f

                        for (i in 0 until 9) {
                            if (!selectedNodes.contains(i)) {
                                val row = i / 3
                                val col = i % 3
                                val cx = padding + col * cellSpacing
                                val cy = padding + row * cellSpacing
                                if (hypot(change.position.x - cx, change.position.y - cy) <= hitRadius) {
                                    selectedNodes.add(i)
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    break
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        currentTouch = null
                        if (selectedNodes.isNotEmpty()) {
                            onPatternComplete(selectedNodes.toList())
                        }
                    },
                    onDragCancel = {
                        currentTouch = null
                        selectedNodes.clear()
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val boxSize = min(size.width, size.height)
            val padding = boxSize * 0.16f
            val cellSpacing = (boxSize - 2 * padding) / 2f

            fun getNodeCenter(index: Int): Offset {
                val row = index / 3
                val col = index % 3
                val cx = padding + col * cellSpacing
                val cy = padding + row * cellSpacing
                return Offset(cx, cy)
            }

            // Draw connecting lines
            if (selectedNodes.isNotEmpty()) {
                val path = Path()
                val firstCenter = getNodeCenter(selectedNodes.first())
                path.moveTo(firstCenter.x, firstCenter.y)

                for (i in 1 until selectedNodes.size) {
                    val center = getNodeCenter(selectedNodes[i])
                    path.lineTo(center.x, center.y)
                }

                // Line glow
                drawPath(
                    path = path,
                    color = primaryColor.copy(alpha = 0.35f),
                    style = Stroke(
                        width = 16.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Main stroke
                drawPath(
                    path = path,
                    color = primaryColor,
                    style = Stroke(
                        width = 6.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Dragging current touch line
                currentTouch?.let { touch ->
                    val lastCenter = getNodeCenter(selectedNodes.last())
                    drawLine(
                        color = primaryColor.copy(alpha = 0.7f),
                        start = lastCenter,
                        end = touch,
                        strokeWidth = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Draw 9 nodes
            for (i in 0 until 9) {
                val center = getNodeCenter(i)
                val isSelected = selectedNodes.contains(i)

                val outerRadius = cellSpacing * 0.28f
                val innerRadius = cellSpacing * 0.12f

                if (isSelected) {
                    // Outer glowing halo
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.25f),
                        radius = outerRadius * 1.25f,
                        center = center
                    )
                    // Selected outer ring
                    drawCircle(
                        color = primaryColor,
                        radius = outerRadius,
                        center = center,
                        style = Stroke(width = 3.dp.toPx())
                    )
                    // Selected solid core
                    drawCircle(
                        color = primaryColor,
                        radius = innerRadius * 1.2f,
                        center = center
                    )
                } else {
                    // Normal idle outer ring
                    drawCircle(
                        color = normalDotColor,
                        radius = outerRadius * 0.7f,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Idle inner dot
                    drawCircle(
                        color = normalCenterColor,
                        radius = innerRadius * 0.7f,
                        center = center
                    )
                }
            }
        }
    }
}
