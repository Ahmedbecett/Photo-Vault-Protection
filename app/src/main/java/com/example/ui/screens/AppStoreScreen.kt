package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StoreItem(
    val id: String,
    val title: String,
    val category: String, // "تطبيق" أو "لعبة"
    val subcategory: String,
    val version: String,
    val size: String,
    val rating: String,
    val downloads: String,
    val description: String,
    val downloadUrl: String,
    val iconVector: ImageVector,
    val accentColor: Color,
    val isFeatured: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStoreScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("الكل") }
    var searchQuery by remember { mutableStateOf("") }
    var showAdminDialog by remember { mutableStateOf(false) }
    var showAddAppDialog by remember { mutableStateOf(false) }
    var adminPin by remember { mutableStateOf("") }
    var selectedItemForDetails by remember { mutableStateOf<StoreItem?>(null) }

    // Initial Store Catalog
    val storeItems = remember {
        mutableStateListOf(
            StoreItem(
                id = "1",
                title = "خزنة الصور والفيديو Pro",
                category = "تطبيق",
                subcategory = "الأمان والتشفير",
                version = "v2.5.0",
                size = "22 MB",
                rating = "4.9 ★",
                downloads = "+50K",
                description = "حماية وقفل الصور ومقاطع الفيديو الشخصية بتشفير AES-256، مع دعم فتح البصمة والوجه، ونمط الشاشة (شيما)، والخزنة الوهمية وتمويه الآلة الحاسبة.",
                downloadUrl = "https://github.com/Ahmedbecett/Photo-Vault-Protection/archive/refs/heads/main.zip",
                iconVector = Icons.Default.Shield,
                accentColor = Color(0xFF10B981),
                isFeatured = true
            ),
            StoreItem(
                id = "2",
                title = "درع الحماية المتكامل (Security Shield)",
                category = "تطبيق",
                subcategory = "مكافحة التجسس",
                version = "v1.8.4",
                size = "15 MB",
                rating = "4.8 ★",
                downloads = "+35K",
                description = "فحص التطبيقات والملفات في الوقت الفعلي وكشف محاولات التجسس واختراق خصوصية الهاتف.",
                downloadUrl = "https://ahmedbecett.github.io/Protection-Tele/",
                iconVector = Icons.Default.Security,
                accentColor = Color(0xFF06B6D4)
            ),
            StoreItem(
                id = "3",
                title = "مسرع الهاتف وتنظيف الرام",
                category = "تطبيق",
                subcategory = "أدوات النظام",
                version = "v3.1.0",
                size = "9 MB",
                rating = "4.7 ★",
                downloads = "+80K",
                description = "تسريع استجابة الهاتف وإيقاف التطبيقات في الخلفية وتفريغ ذاكرة التخزين المؤقتة بنقرة واحدة.",
                downloadUrl = "https://ahmedbecett.github.io/Protection-Tele/",
                iconVector = Icons.Default.Speed,
                accentColor = Color(0xFFF59E0B)
            ),
            StoreItem(
                id = "4",
                title = "مغامرات الفضاء السيبراني 3D",
                category = "لعبة",
                subcategory = "أكشن وحروب",
                version = "v1.4.0",
                size = "48 MB",
                rating = "4.9 ★",
                downloads = "+20K",
                description = "معارك فضائية ملحمية برسوميات ثلاثية الأبعاد بدون إنترنت، مراحل متدرجة وتحديثات أسلحة متقدمة.",
                downloadUrl = "https://ahmedbecett.github.io/Protection-Tele/",
                iconVector = Icons.Default.SportsEsports,
                accentColor = Color(0xFFEC4899),
                isFeatured = true
            ),
            StoreItem(
                id = "5",
                title = "سباق التوربو الأسطوري (Turbo Speed)",
                category = "لعبة",
                subcategory = "سباقات وسيارات",
                version = "v2.0.1",
                size = "65 MB",
                rating = "4.6 ★",
                downloads = "+40K",
                description = "سباقات سيارات سريعة مع فيزياء واقعية ومسارات ليلية وتعديلات غير محدودة للسيارات.",
                downloadUrl = "https://ahmedbecett.github.io/Protection-Tele/",
                iconVector = Icons.Default.DirectionsCar,
                accentColor = Color(0xFF8B5CF6)
            ),
            StoreItem(
                id = "6",
                title = "مانع الإعلانات والمواقع المشبوهة",
                category = "تطبيق",
                subcategory = "أمان التصفح",
                version = "v2.2.0",
                size = "11 MB",
                rating = "4.8 ★",
                downloads = "+60K",
                description = "حظر الإعلانات المزعجة وحماية متصفحك من الروابط الاحتيالية ومواقع التصيد.",
                downloadUrl = "https://ahmedbecett.github.io/Protection-Tele/",
                iconVector = Icons.Default.Block,
                accentColor = Color(0xFFEF4444)
            )
        )
    }

    val categories = listOf("الكل", "التطبيقات", "الألعاب", "الأكثر تحميلاً")

    val filteredItems = storeItems.filter { item ->
        val matchesCategory = when (selectedCategory) {
            "التطبيقات" -> item.category == "تطبيق"
            "الألعاب" -> item.category == "لعبة"
            "الأكثر تحميلاً" -> item.isFeatured
            else -> true
        }
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        containerColor = Color(0xFF0B0F19),
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
                                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shop,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "متجر Protection-Tele",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "المطور: Ahmed Becetti",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp
                            )
                        }
                    }
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
                    // Admin Manage Button
                    IconButton(onClick = { showAdminDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "إدارة المتجر والأدمن",
                            tint = Color(0xFFF59E0B)
                        )
                    }

                    // Open Web Store
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ahmedbecett.github.io/Protection-Tele/"))
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "الموقع على الويب",
                            tint = Color(0xFF38BDF8)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0B0F19))
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            // Search Input Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ابحث في التطبيقات والألعاب...", color = Color(0xFF64748B), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color(0xFF1E293B),
                        focusedContainerColor = Color(0xFF161F30),
                        unfocusedContainerColor = Color(0xFF161F30),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Categories Filter Bar
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF10B981),
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF161F30),
                                labelColor = Color(0xFF94A3B8)
                            )
                        )
                    }
                }
            }

            // Featured Hero Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val featured = storeItems.first()
                            selectedItemForDetails = featured
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF132238)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "⭐ التطبيق الأكثر أماناً وحماية",
                                color = Color(0xFF10B981),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF10B981), Color(0xFF06B6D4))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "خزنة الصور والفيديو Pro",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "تشفير عسكري AES-256 • بصمة ووجه • شيما",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ahmedbecett/Photo-Vault-Protection/archive/refs/heads/main.zip"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text("تنزيل", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "التطبيقات والألعاب المتاحة (${filteredItems.size})",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Items List
            items(filteredItems, key = { it.id }) { item ->
                StoreItemCard(
                    item = item,
                    onClick = { selectedItemForDetails = item },
                    onDownload = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.downloadUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }

    // Admin Verification Dialog
    if (showAdminDialog) {
        AlertDialog(
            onDismissRequest = { showAdminDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("🔐 دخول لوحة الأدمن (Ahmed Becetti)", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("أدخل رمز PIN للأدمن لإضافة تطبيقات وألعاب جديدة للمتجر:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    OutlinedTextField(
                        value = adminPin,
                        onValueChange = { adminPin = it },
                        placeholder = { Text("رمز PIN (1234)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF59E0B),
                            unfocusedBorderColor = Color(0xFF64748B),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (adminPin == "1234") {
                            showAdminDialog = false
                            adminPin = ""
                            showAddAppDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                ) {
                    Text("دخول", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }

    // Add New App / Game Dialog for Admin
    if (showAddAppDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newType by remember { mutableStateOf("تطبيق") }
        var newSubcategory by remember { mutableStateOf("") }
        var newVersion by remember { mutableStateOf("v1.0.0") }
        var newSize by remember { mutableStateOf("15 MB") }
        var newDesc by remember { mutableStateOf("") }
        var newUrl by remember { mutableStateOf("https://ahmedbecett.github.io/Protection-Tele/") }

        AlertDialog(
            onDismissRequest = { showAddAppDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("➕ إضافة تطبيق أو لعبة إلى المتجر", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("اسم التطبيق أو اللعبة") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = newType == "تطبيق",
                            onClick = { newType = "تطبيق" },
                            label = { Text("تطبيق") }
                        )
                        FilterChip(
                            selected = newType == "لعبة",
                            onClick = { newType = "لعبة" },
                            label = { Text("لعبة") }
                        )
                    }

                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text("الوصف المختصر") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    OutlinedTextField(
                        value = newUrl,
                        onValueChange = { newUrl = it },
                        label = { Text("رابط التحميل APK") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            storeItems.add(
                                0,
                                StoreItem(
                                    id = System.currentTimeMillis().toString(),
                                    title = newTitle.trim(),
                                    category = newType,
                                    subcategory = if (newSubcategory.isBlank()) "منتج جديد" else newSubcategory,
                                    version = newVersion,
                                    size = newSize,
                                    rating = "5.0 ★",
                                    downloads = "+1K",
                                    description = newDesc.ifBlank { "تطبيق احترافي متقدم للمطور Ahmed Becetti." },
                                    downloadUrl = newUrl,
                                    iconVector = if (newType == "لعبة") Icons.Default.SportsEsports else Icons.Default.Apps,
                                    accentColor = Color(0xFF10B981)
                                )
                            )
                            showAddAppDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("نشر في المتجر", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAppDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }

    // App Details Modal Sheet
    selectedItemForDetails?.let { item ->
        ModalBottomSheet(
            onDismissRequest = { selectedItemForDetails = null },
            containerColor = Color(0xFF161F30)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(item.accentColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.iconVector,
                            contentDescription = null,
                            tint = item.accentColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "المطور: Ahmed Becetti • ${item.subcategory}",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = item.rating, color = Color(0xFFF59E0B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "• ${item.size}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Text(text = "• ${item.downloads} تنزيل", color = Color(0xFF10B981), fontSize = 12.sp)
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF24324D))

                Text(
                    text = "نبذة عن التطبيق ومميزاته:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = item.description,
                    color = Color(0xFFCBD5E1),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.downloadUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تنزيل وتثبيت APK الآن", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun StoreItemCard(
    item: StoreItem,
    onClick: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.iconVector,
                    contentDescription = null,
                    tint = item.accentColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.subcategory} • ${item.size}",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = item.rating, color = Color(0xFFF59E0B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Surface(
                        color = if (item.category == "تطبيق") Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEC4899).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.category,
                            color = if (item.category == "تطبيق") Color(0xFF10B981) else Color(0xFFEC4899),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("تحميل", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
