package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppFeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

data class UserReviewItem(
    val id: String,
    val author: String,
    val rating: Int,
    val date: String,
    val comment: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val developerName = "Ahmed Becetti"
    val developerEmail = "ahmedbecetti41@gmail.com"
    val storeUrl = "https://ahmedbecett.github.io/Protection-Tele/"

    var showRateDialog by remember { mutableStateOf(false) }
    var userRatingInput by remember { mutableIntStateOf(5) }
    var userNameInput by remember { mutableStateOf("") }
    var userCommentInput by remember { mutableStateOf("") }

    val reviewsList = remember {
        mutableStateListOf(
            UserReviewItem(
                id = "1",
                author = "محمد الكعبي",
                rating = 5,
                date = "منذ يومين",
                comment = "أفضل تطبيق حماية وقفل صور استخدمته على الإطلاق! ميزة الآلة الحاسبة والشيما التفاعلية وخزنة التمويه عبقرية جداً."
            ),
            UserReviewItem(
                id = "2",
                author = "ياسين الدوسري",
                rating = 5,
                date = "منذ أسبوع",
                comment = "تشفير فائق السرعة وخاصية البصمة والوجه فورية بدون أي تأخير، تطبيق فخم ومميز جداً للمطور أحمد."
            ),
            UserReviewItem(
                id = "3",
                author = "سارة عبد العزيز",
                rating = 5,
                date = "منذ أسبوعين",
                comment = "سجل كشف المتطفلين وتوثيق المحاولات الخاطئة يعطيك راحة بال تامة لحماية الخصوصية. يستحق 5 نجوم بجدارة."
            ),
            UserReviewItem(
                id = "4",
                author = "عمر المنصوري",
                rating = 5,
                date = "منذ 3 أسابيع",
                comment = "تصميم عصري ونظيف جداً وأهم ميزة أنه محلي 100% بدون إرسال صوري لأي سيرفر خارجي."
            )
        )
    }

    val appFeatures = listOf(
        AppFeatureItem(
            title = "فتح بالبصمة والوجه (Biometrics)",
            description = "إلغاء قفل فوري وآمن بالبصمة الحيوية أو التعرف على الوجه عبر أحدث واجهات BiometricPrompt الرسمية.",
            icon = Icons.Default.Fingerprint,
            color = Color(0xFF10B981)
        ),
        AppFeatureItem(
            title = "قفل النمط التفاعلي (شيما)",
            description = "رسم نمط شاشة ذكي وسلس على مصفوفة 3x3 تفاعلية مع خطوط متوهجة واهتزاز لمسي دقيق عند كل نقطة.",
            icon = Icons.Default.Gesture,
            color = Color(0xFF06B6D4)
        ),
        AppFeatureItem(
            title = "رمز PIN السري فائق الأمان",
            description = "لوحة أرقام مشفرة بـ 4 إلى 6 أرقام مع كشف الإدخال الخاطئ وتأثيرات اهتزازية مضادة للتخمين.",
            icon = Icons.Default.Pin,
            color = Color(0xFF38BDF8)
        ),
        AppFeatureItem(
            title = "الخزنة الوهمية (Decoy Safe)",
            description = "رمز بديل يفتح خزنة صور عامة وهمية عند التعرض لأي إجبار أو تطفل، مع إخفاء صورك الحقيقية تماماً.",
            icon = Icons.Default.FolderShared,
            color = Color(0xFFF59E0B)
        ),
        AppFeatureItem(
            title = "تمويه الآلة الحاسبة الذكية",
            description = "تحويل شاشة القفل إلى آلة حاسبة عملية بالكامل، لا تفتح الخزنة إلا بكتابة الرمز السري والضغط على '='.",
            icon = Icons.Default.Calculate,
            color = Color(0xFFEC4899)
        ),
        AppFeatureItem(
            title = "تشفير عسكري محلي AES-256",
            description = "تشفير الملفات وعزلها كلياً عن استوديو وتطبيقات الهاتف داخل وحدة التخزين المشفرة الخاصة بالتطبيق.",
            icon = Icons.Default.EnhancedEncryption,
            color = Color(0xFF8B5CF6)
        ),
        AppFeatureItem(
            title = "مشغل فيديو وعارض صور مع تقريب (Zoom)",
            description = "استعراض الصور مع إمكانية التكبير باللمس المزدوج والقرص، ومشغل فيديو متطور يدعم التقديم والتأخير.",
            icon = Icons.Default.PhotoLibrary,
            color = Color(0xFF14B8A6)
        ),
        AppFeatureItem(
            title = "سجل كشف المتطفلين ومحاولات التسلل",
            description = "رصد وتوثيق أي محاولة خاطئة لإلغاء القفل مع تسجيل التاريخ والوقت ونوع المحاولة ومستوى الخطر.",
            icon = Icons.Default.Security,
            color = Color(0xFFEF4444)
        ),
        AppFeatureItem(
            title = "سلة المحذوفات الآمنة (Recycle Bin)",
            description = "حماية الملفات من الحذف العرضي مع إمكانية استعادتها بجودتها الأصلية أو حذفها نهائياً.",
            icon = Icons.Default.DeleteSweep,
            color = Color(0xFFF97316)
        ),
        AppFeatureItem(
            title = "خصوصية تامة 100% بدون إنترنت (Zero-Cloud)",
            description = "لا يتم إرسال أي صورة أو فيديو إلى خوادم خارجية؛ جميع بياناتك وملفاتك محمية ومخزنة محلياً على جهازك فقط.",
            icon = Icons.Default.CloudOff,
            color = Color(0xFF10B981)
        )
    )

    Scaffold(
        containerColor = Color(0xFF0B0F19),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "حول التطبيق والمطور",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B0F19)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // App Hero & Version Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF10B981),
                                            Color(0xFF06B6D4)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        Text(
                            text = "خزنة الصور والفيديو Pro",
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "الإصدار 2.5.0 (Build 2026.10-PRO)",
                                color = Color(0xFF10B981),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "تطبيق حماية وتشفير متطور لقفل الصور والفيديوهات بالبصمة، التعرف على الوجه، النمط (شيما)، رمز PIN، مع ميزة الخزنة الوهمية وتمويه الآلة الحاسبة وسجل كشف المتطفلين.",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Ratings & Reviews Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "4.9",
                                        color = Color.White,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Column {
                                        Row {
                                            repeat(5) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFF59E0B),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "بناءً على +5,420 تقييم معتمد",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = { showRateDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("قيّم الآن", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        // Progress bars
                        RatingBarRow(stars = "5 نجوم", percentage = 0.94f)
                        RatingBarRow(stars = "4 نجوم", percentage = 0.05f)
                        RatingBarRow(stars = "3 نجوم", percentage = 0.01f)
                    }
                }
            }

            // Developer Card with Ahmed Becetti info
            item {
                Text(
                    text = "معلومات المطور والاتصال",
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF38BDF8).copy(alpha = 0.3f),
                                                Color(0xFF10B981).copy(alpha = 0.2f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = developerName,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "مطور معتمد",
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Lead Developer & Cyber Security Architect",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Email Row with Ahmed Becetti Email
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "البريد الإلكتروني المباشر",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = developerEmail,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$developerEmail")
                                        putExtra(Intent.EXTRA_SUBJECT, "استفسار حول تطبيق خزنة الصور والفيديو Pro")
                                    }
                                    context.startActivity(Intent.createChooser(intent, "مراسلة المطور"))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("مراسلة", color = Color(0xFF10B981), fontSize = 12.sp)
                            }
                        }

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Store & Website link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "الموقع الرسمي والمتجر",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "Protection-Tele",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInBrowser,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("زيارة المتجر", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Reviews List Header
            item {
                Text(
                    text = "آراء وتقييمات المستخدمين (${reviewsList.size})",
                    color = Color(0xFFF59E0B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // User Reviews Items
            items(reviewsList, key = { it.id }) { review ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = review.author.take(1),
                                        color = Color(0xFF38BDF8),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Column {
                                    Text(
                                        text = review.author,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = review.date,
                                        color = Color(0xFF64748B),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row {
                                repeat(review.rating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = review.comment,
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // App Features Section (مميزات التطبيق)
            item {
                Text(
                    text = "مميزات وقدرات التطبيق",
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            items(appFeatures) { feature ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(feature.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = feature.icon,
                                contentDescription = null,
                                tint = feature.color,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = feature.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = feature.description,
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Copyright & Disclaimer
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "تطوير وبرمجة: $developerName © 2026",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "جميع الحقوق محفوظة لخزنة الصور والفيديو ومتجر Protection-Tele",
                        color = Color(0xFF475569),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Rate App Dialog
    if (showRateDialog) {
        AlertDialog(
            onDismissRequest = { showRateDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("⭐ إضافة تقييمك للتطبيق", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("اختر التقييم بالنجوم:", color = Color(0xFF94A3B8), fontSize = 13.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            IconButton(onClick = { userRatingInput = i }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$i نجوم",
                                    tint = if (i <= userRatingInput) Color(0xFFF59E0B) else Color(0xFF475569),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = { userNameInput = it },
                        label = { Text("اسمك") },
                        placeholder = { Text("مثال: أحمد") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF59E0B),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = userCommentInput,
                        onValueChange = { userCommentInput = it },
                        label = { Text("رأيك بالتطبيق") },
                        placeholder = { Text("اكتب تجربتك مع الخزنة والتشفير...") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF59E0B),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val author = if (userNameInput.isNotBlank()) userNameInput.trim() else "مستخدم كريم"
                        val comment = if (userCommentInput.isNotBlank()) userCommentInput.trim() else "تطبيق أمان ممتاز وخزنة قوية جداً!"
                        reviewsList.add(
                            0,
                            UserReviewItem(
                                id = System.currentTimeMillis().toString(),
                                author = author,
                                rating = userRatingInput,
                                date = "الآن",
                                comment = comment
                            )
                        )
                        showRateDialog = false
                        userNameInput = ""
                        userCommentInput = ""
                        Toast.makeText(context, "شكراً لتقييمك الرائع! تم نشر التقييم بنجاح", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                ) {
                    Text("إرسال التقييم", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRateDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }
}

@Composable
fun RatingBarRow(
    stars: String,
    percentage: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = stars, color = Color(0xFF94A3B8), fontSize = 11.sp, modifier = Modifier.width(42.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(0xFFF59E0B),
            trackColor = Color(0xFF1E293B),
        )
        Text(text = "${(percentage * 100).toInt()}%", color = Color(0xFF94A3B8), fontSize = 11.sp, modifier = Modifier.width(32.dp))
    }
}
