package com.example.ui.screens

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    trashItems: List<MediaItem>,
    onBack: () -> Unit,
    onRestore: (MediaItem) -> Unit,
    onPermanentDelete: (MediaItem) -> Unit,
    onEmptyTrash: () -> Unit
) {
    val context = LocalContext.current
    var showEmptyConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0B0F19),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "سلة المحذوفات (${trashItems.size})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (trashItems.isNotEmpty()) {
                        TextButton(onClick = { showEmptyConfirmDialog = true }) {
                            Text("تفريغ السلة", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B0F19)
                )
            )
        }
    ) { innerPadding ->
        if (trashItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "سلة المحذوفات فارغة",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "الصور والفيديوهات المحذوفة ستظهر هنا مع إمكانية استعادتها",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(trashItems, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                            ) {
                                AsyncImage(
                                    model = File(item.filePath),
                                    contentDescription = item.originalName,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.originalName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${if (item.mediaType == MediaType.PHOTO) "صورة" else "فيديو"} • ${Formatter.formatFileSize(context, item.fileSize)}",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }

                            // Restore
                            IconButton(onClick = { onRestore(item) }) {
                                Icon(
                                    imageVector = Icons.Default.Restore,
                                    contentDescription = "استعادة",
                                    tint = Color(0xFF10B981)
                                )
                            }

                            // Permanent Delete
                            IconButton(onClick = { onPermanentDelete(item) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = "حذف نهائي",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEmptyConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyConfirmDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("تأكيد تفريغ سلة المحذوفات", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "سيتم حذف جميع الملفات الموجودة في السلة نهائياً من ذاكرة الجهاز المشفرة ولن يمكن استعادتها.",
                    color = Color(0xFFCBD5E1)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEmptyTrash()
                        showEmptyConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("حذف نهائي للكل", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmptyConfirmDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }
}
