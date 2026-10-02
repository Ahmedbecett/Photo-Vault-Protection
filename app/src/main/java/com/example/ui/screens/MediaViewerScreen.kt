package com.example.ui.screens

import android.content.Intent
import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaViewerScreen(
    mediaItem: MediaItem,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onRestoreOrExport: () -> Unit
) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }
    var showInfoSheet by remember { mutableStateOf(false) }

    // Zoom and pan state for photos
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Video playback simulation state
    var isPlaying by remember { mutableStateOf(false) }
    var currentVideoProgress by remember { mutableFloatStateOf(0f) }
    val videoDuration = if (mediaItem.durationMs > 0) mediaItem.durationMs else 45000L

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            currentVideoProgress = (currentVideoProgress + 500f / videoDuration).coerceAtMost(1f)
            if (currentVideoProgress >= 1f) {
                isPlaying = false
                currentVideoProgress = 0f
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Media Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showControls = !showControls },
                        onDoubleTap = {
                            if (scale > 1f) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            } else {
                                scale = 2.5f
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (mediaItem.mediaType == MediaType.PHOTO) {
                AsyncImage(
                    model = File(mediaItem.filePath),
                    contentDescription = mediaItem.originalName,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                if (scale > 1f) {
                                    offsetX += pan.x
                                    offsetY += pan.y
                                } else {
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
                        },
                    contentScale = ContentScale.Fit
                )
            } else {
                // Video Player Presentation
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = File(mediaItem.filePath),
                        contentDescription = mediaItem.originalName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    // Overlay dim when paused
                    if (!isPlaying) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f))
                        )
                    }

                    // Large Center Play / Pause button
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.65f))
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }

        // Top App Bar
        AnimatedVisibility(
            visible = showControls,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = mediaItem.originalName,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "مشفر بـ AES-256",
                            color = Color(0xFF10B981),
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = { showInfoSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "تفاصيل الملف",
                        tint = Color.White
                    )
                }
            }
        }

        // Bottom Controls Bar
        AnimatedVisibility(
            visible = showControls,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(16.dp)
            ) {
                // If video, show scrubber
                if (mediaItem.mediaType == MediaType.VIDEO) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val currentMs = (currentVideoProgress * videoDuration).toLong()
                        val currentSec = (currentMs / 1000) % 60
                        val currentMin = (currentMs / 1000) / 60
                        val totalSec = (videoDuration / 1000) % 60
                        val totalMin = (videoDuration / 1000) / 60

                        Text(
                            text = String.format("%02d:%02d", currentMin, currentSec),
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        Slider(
                            value = currentVideoProgress,
                            onValueChange = {
                                currentVideoProgress = it
                            },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF10B981),
                                activeTrackColor = Color(0xFF10B981),
                                inactiveTrackColor = Color.DarkGray
                            )
                        )

                        Text(
                            text = String.format("%02d:%02d", totalMin, totalSec),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Favorite
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (mediaItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "المفضلة",
                            tint = if (mediaItem.isFavorite) Color(0xFFEF4444) else Color.White
                        )
                    }

                    // Share / Export to public
                    IconButton(onClick = onRestoreOrExport) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = "استعادة للمظهر العام",
                            tint = Color(0xFF06B6D4)
                        )
                    }

                    // Share
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "تمت المشاركة من خزنة الصور والفيديو الآمنة: ${mediaItem.originalName}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "مشاركة الملف"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة",
                            tint = Color.White
                        )
                    }

                    // Delete / Trash
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "حذف إلى المهملات",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }

        // Info Details Modal Bottom Sheet
        if (showInfoSheet) {
            ModalBottomSheet(
                onDismissRequest = { showInfoSheet = false },
                containerColor = Color(0xFF1E293B)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF10B981)
                            )
                        }
                        Column {
                            Text(
                                text = "بيانات التشفير والحماية",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "مخزنة في مساحة معزولة ومحمية بالكامل",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFF334155))

                    InfoRow(label = "اسم الملف", value = mediaItem.originalName)
                    InfoRow(label = "نوع الوسائط", value = if (mediaItem.mediaType == MediaType.PHOTO) "صورة محمية" else "فيديو محمي")
                    InfoRow(label = "حجم الملف", value = Formatter.formatFileSize(context, mediaItem.fileSize))
                    val dateStr = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(mediaItem.dateAdded))
                    InfoRow(label = "تاريخ الإضافة", value = dateStr)
                    InfoRow(label = "خوارزمية التشفير", value = mediaItem.encryptionAlgorithm)
                    InfoRow(label = "حالة العزل", value = "معزول عن استوديو النظام والبرامج الأخرى")

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFF94A3B8), fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
