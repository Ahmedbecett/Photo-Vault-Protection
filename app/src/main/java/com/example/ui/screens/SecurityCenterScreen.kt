package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LockType
import com.example.data.model.SecurityLog
import com.example.data.model.VaultSettings
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityCenterScreen(
    settings: VaultSettings,
    securityLogs: List<SecurityLog>,
    onBack: () -> Unit,
    onUpdateSettings: (VaultSettings) -> Unit,
    onClearLogs: () -> Unit
) {
    var showChangePinDialog by remember { mutableStateOf(false) }
    var showChangeDecoyDialog by remember { mutableStateOf(false) }
    var newPinText by remember { mutableStateOf("") }
    var newDecoyText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFF0B0F19),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "مركز الأمان والحماية",
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
            // Security Audit Score Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "درجة الأمان: 98%",
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Surface(
                                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "ممتاز",
                                        color = Color(0xFF10B981),
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "التشفير المحلي AES-256 مفعل، وجميع البيانات معزولة داخل وحدة التخزين المشفرة للجهاز.",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Section 1: Authentication Options
            item {
                Text(
                    text = "طرق القفل والتحقق",
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
                    Column {
                        // Lock Type Selection (PIN vs Pattern)
                        SettingItem(
                            icon = Icons.Default.Lock,
                            title = "نوع القفل الافتراضي",
                            subtitle = if (settings.activeLockType == LockType.PIN) "رمز PIN (4 أرقام)" else "نمط الشاشة (شيما)",
                            trailing = {
                                Button(
                                    onClick = {
                                        val newType = if (settings.activeLockType == LockType.PIN) LockType.PATTERN else LockType.PIN
                                        onUpdateSettings(settings.copy(activeLockType = newType))
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF1E293B)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (settings.activeLockType == LockType.PIN) "تحويل لشيما" else "تحويل لـ PIN",
                                        color = Color(0xFF10B981),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Change PIN
                        SettingItem(
                            icon = Icons.Default.Pin,
                            title = "تغيير رمز PIN",
                            subtitle = "الرمز الحالي: **** (انقر لتعديله)",
                            onClick = {
                                newPinText = settings.pinCode
                                showChangePinDialog = true
                            }
                        )

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Biometric Toggle (Fingerprint / Face)
                        SettingItem(
                            icon = Icons.Default.Fingerprint,
                            title = "المصادقة بالبصمة أو الوجه",
                            subtitle = "استخدام المستشعر البيومتري لفتح الخزنة بسرعة وأمان",
                            trailing = {
                                Switch(
                                    checked = settings.isBiometricEnabled,
                                    onCheckedChange = {
                                        onUpdateSettings(settings.copy(isBiometricEnabled = it))
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.Black,
                                        checkedTrackColor = Color(0xFF10B981)
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // Section 2: Stealth & Camouflage
            item {
                Text(
                    text = "التمويه والخزنة الوهمية",
                    color = Color(0xFFF59E0B),
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
                    Column {
                        // Calculator Disguise
                        SettingItem(
                            icon = Icons.Default.Calculate,
                            title = "وضع تمويه الآلة الحاسبة",
                            subtitle = "يظهر التطبيق كآلة حاسبة حقيقية لا تفتح إلا بكتابة الرمز والضغط على =",
                            trailing = {
                                Switch(
                                    checked = settings.isCalculatorDisguiseEnabled,
                                    onCheckedChange = {
                                        onUpdateSettings(settings.copy(isCalculatorDisguiseEnabled = it))
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.Black,
                                        checkedTrackColor = Color(0xFFF59E0B)
                                    )
                                )
                            }
                        )

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Decoy Vault PIN
                        SettingItem(
                            icon = Icons.Default.FolderShared,
                            title = "رمز الخزنة الوهمية (Decoy Safe)",
                            subtitle = "رمز بديل يفتح خزنة صور فارغة أو عامة عند التعرض للإجبار",
                            onClick = {
                                newDecoyText = settings.decoyPin
                                showChangeDecoyDialog = true
                            }
                        )

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Auto Lock
                        SettingItem(
                            icon = Icons.Default.ScreenLockPortrait,
                            title = "القفل التلقائي الفوري",
                            subtitle = "قفل الخزنة مباشرة بمجرد تصغير التطبيق أو إطفاء الشاشة",
                            trailing = {
                                Switch(
                                    checked = settings.autoLockOnBackground,
                                    onCheckedChange = {
                                        onUpdateSettings(settings.copy(autoLockOnBackground = it))
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.Black,
                                        checkedTrackColor = Color(0xFF10B981)
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // Section 3: Intruder Logs & Break-in Attempts
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سجل محاولات الاختراق والتسلل",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (securityLogs.isNotEmpty()) {
                        TextButton(onClick = onClearLogs) {
                            Text("مسح السجل", color = Color(0xFFEF4444), fontSize = 12.sp)
                        }
                    }
                }
            }

            if (securityLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161F30)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = "لم يتم رصد أي محاولات تسلل",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "سيسجل التطبيق تلقائياً أي محاولة إدخال خاطئة لرمز PIN أو النمط.",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            } else {
                items(securityLogs.take(10), key = { it.id }) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (log.success) Color(0xFF161F30) else Color(0xFF2C1318)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (log.success) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (log.success) Color(0xFF10B981) else Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = log.details,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                val dateStr = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date(log.timestamp))
                                Text(
                                    text = dateStr,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }

                            Surface(
                                color = if (log.success) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (log.success) "ناجح" else "تسلل!",
                                    color = if (log.success) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for changing Real PIN
    if (showChangePinDialog) {
        AlertDialog(
            onDismissRequest = { showChangePinDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("تغيير رمز PIN السري", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                OutlinedTextField(
                    value = newPinText,
                    onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) newPinText = it },
                    label = { Text("رمز PIN الجديد (4-6 أرقام)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color(0xFF64748B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPinText.length in 4..6) {
                            onUpdateSettings(settings.copy(pinCode = newPinText))
                            showChangePinDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("حفظ الرمز", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePinDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }

    // Dialog for changing Decoy PIN
    if (showChangeDecoyDialog) {
        AlertDialog(
            onDismissRequest = { showChangeDecoyDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Text("تحديد رمز الخزنة الوهمية", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                OutlinedTextField(
                    value = newDecoyText,
                    onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) newDecoyText = it },
                    label = { Text("رمز الخزنة الوهمية (مختلف عن الرمز الحقيقي)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF59E0B),
                        unfocusedBorderColor = Color(0xFF64748B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newDecoyText.length in 4..6 && newDecoyText != settings.pinCode) {
                            onUpdateSettings(settings.copy(decoyPin = newDecoyText))
                            showChangeDecoyDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                ) {
                    Text("حفظ", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangeDecoyDialog = false }) {
                    Text("إلغاء", color = Color(0xFF94A3B8))
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(22.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        trailing?.invoke()
    }
}
