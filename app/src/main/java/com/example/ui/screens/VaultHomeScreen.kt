package com.example.ui.screens

import android.net.Uri
import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.ui.MainTab
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultHomeScreen(
    mediaList: List<MediaItem>,
    isDecoy: Boolean,
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onMediaClick: (MediaItem) -> Unit,
    onImportMedia: (List<Uri>) -> Unit,
    onLockApp: () -> Unit,
    onOpenAlbums: () -> Unit,
    onOpenSecurity: () -> Unit,
    onOpenTrash: () -> Unit,
    onOpenAboutDev: () -> Unit
) {
    val context = LocalContext.current

    // Android Modern Zero-Permission Photo/Video Picker
    val pickMultipleVisualMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onImportMedia(uris)
        }
    }

    val photoCount = mediaList.count { it.mediaType == MediaType.PHOTO }
    val videoCount = mediaList.count { it.mediaType == MediaType.VIDEO }
    val totalBytes = mediaList.sumOf { it.fileSize }

    Scaffold(
        containerColor = Color(0xFF0B0F19), // Deep Obsidian
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isDecoy) Color(0xFF3B82F6).copy(alpha = 0.2f)
                                    else Color(0xFF10B981).copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDecoy) Icons.Default.FolderShared else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isDecoy) Color(0xFF3B82F6) else Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isDecoy) "الخزنة الثانوية" else "خزنة الصور والفيديو",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDecoy) "وضع العرض العادي" else "مشفرة بـ AES-256 محلياً",
                                color = if (isDecoy) Color(0xFF93C5FD) else Color(0xFF10B981),
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // Security Center shortcut
                    IconButton(onClick = onOpenSecurity) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "مركز الأمان",
                            tint = Color(0xFF06B6D4)
                        )
                    }

                    // Developer info & version
                    IconButton(onClick = onOpenAboutDev) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "معلومات المطور والاصدار",
                            tint = Color(0xFFF59E0B)
                        )
                    }

                    // Immediate Lock Button
                    IconButton(onClick = onLockApp) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = "قفل فوري",
                            tint = Color(0xFFEF4444)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B0F19)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    pickMultipleVisualMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                },
                containerColor = Color(0xFF10B981),
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = "استيراد وسائط",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Storage Statistics & Safety Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF161F30)
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DataUsage,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "حجم البيانات المحمية",
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = Formatter.formatFileSize(context, totalBytes),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Visual storage bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF334155))
                    ) {
                        val photoRatio = if (mediaList.isNotEmpty()) photoCount.toFloat() / mediaList.size else 0.5f
                        val videoRatio = if (mediaList.isNotEmpty()) videoCount.toFloat() / mediaList.size else 0.5f

                        Box(
                            modifier = Modifier
                                .weight(photoRatio.coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(Color(0xFF10B981))
                        )
                        Box(
                            modifier = Modifier
                                .weight(videoRatio.coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(Color(0xFFEF4444))
                        )
                    }

                    // Stat indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Text(text = "$photoCount صورة", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Text(text = "$videoCount فيديو", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                            Text(text = "مستوى التشفير فائق", color = Color(0xFF10B981), fontSize = 12.sp)
                        }
                    }
                }
            }

            // Quick Category Shortcuts (Albums, Trash, etc)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip(
                    icon = Icons.Default.Folder,
                    label = "الألبومات",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenAlbums
                )

                QuickActionChip(
                    icon = Icons.Default.DeleteOutline,
                    label = "المهملات",
                    tint = Color(0xFFF87171),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenTrash
                )

                QuickActionChip(
                    icon = Icons.Default.SecurityUpdateGood,
                    label = "الأمان",
                    tint = Color(0xFF34D399),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenSecurity
                )
            }

            // Filter Tabs (الكل، الصور، الفيديوهات، المفضلة)
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF10B981),
                edgePadding = 16.dp,
                divider = {}
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val label = when (tab) {
                        MainTab.ALL -> "كل الوسائط (${mediaList.size})"
                        MainTab.PHOTOS -> "الصور ($photoCount)"
                        MainTab.VIDEOS -> "الفيديوهات ($videoCount)"
                        MainTab.FAVORITES -> "المفضلة"
                    }
                    Tab(
                        selected = isSelected,
                        onClick = { onTabSelected(tab) },
                        text = {
                            Text(
                                text = label,
                                color = if (isSelected) Color(0xFF10B981) else Color(0xFF94A3B8),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            // Media Grid Content
            if (mediaList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Text(
                            text = "لا توجد ملفات في هذا القسم",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "اضغط على زر استيراد وسائط لحماية صورك وفيديوهاتك الآن وتشفيرها بشكل آمن تماماً.",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                pickMultipleVisualMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "اختر من الاستوديو", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(mediaList, key = { it.id }) { item ->
                        MediaThumbnailCard(
                            item = item,
                            onClick = { onMediaClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        color = Color(0xFF1E293B).copy(alpha = 0.7f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MediaThumbnailCard(
    item: MediaItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E293B))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = File(item.filePath),
            contentDescription = item.originalName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay gradient for badges
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                )
        )

        // Video Badge
        if (item.mediaType == MediaType.VIDEO) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "فيديو",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Favorite icon badge
        if (item.isFavorite) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(16.dp)
            )
        }

        // Encryption indicator in top left
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = Color(0xFF10B981).copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .size(12.dp)
        )
    }
}
