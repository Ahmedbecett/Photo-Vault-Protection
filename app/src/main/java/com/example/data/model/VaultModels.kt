package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MediaType {
    PHOTO,
    VIDEO
}

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey
    val id: String,
    val fileName: String,
    val originalName: String,
    val filePath: String,
    val mimeType: String,
    val mediaType: MediaType,
    val fileSize: Long,
    val durationMs: Long = 0L,
    val dateAdded: Long = System.currentTimeMillis(),
    val albumId: String = "default",
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedTimestamp: Long = 0L,
    val isDecoy: Boolean = false,
    val encryptionAlgorithm: String = "AES-256-GCM"
)

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String = "",
    val coverFilePath: String? = null,
    val iconName: String = "folder",
    val createdAt: Long = System.currentTimeMillis(),
    val isSystem: Boolean = false,
    val isDecoy: Boolean = false
)

@Entity(tableName = "security_logs")
data class SecurityLog(
    @PrimaryKey
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attemptType: String, // PIN, PATTERN, BIOMETRIC
    val success: Boolean,
    val details: String,
    val riskLevel: String // "منخفض", "متوسط", "عالي"
)

enum class LockType {
    PIN,
    PATTERN
}

data class VaultSettings(
    val activeLockType: LockType = LockType.PIN,
    val pinCode: String = "1234",
    val patternCode: String = "0,1,2,5,8", // default pattern: Top L to R, down
    val decoyPin: String = "0000",
    val decoyPattern: String = "0,3,6,7,8",
    val isBiometricEnabled: Boolean = true,
    val isSetupCompleted: Boolean = true,
    val isCalculatorDisguiseEnabled: Boolean = false,
    val autoLockOnBackground: Boolean = true,
    val vibrationFeedback: Boolean = true,
    val language: String = "ar"
)

data class DatabaseInfo(
    val dbName: String = "secure_vault_database.sqlite",
    val dbEngine: String = "SQLite 3.45 (AndroidX Room)",
    val encryptionStatus: String = "SQLCipher & AES-256 GCM",
    val mediaCount: Int = 0,
    val albumsCount: Int = 0,
    val securityLogsCount: Int = 0,
    val dbSizeBytes: Long = 0L,
    val isConnected: Boolean = true,
    val lastBackupDate: String = "2026/10/02"
)
