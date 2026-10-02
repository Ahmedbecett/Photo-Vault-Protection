package com.example.data.i18n

enum class AppLanguage(
    val code: String,
    val title: String,
    val flag: String,
    val isRtl: Boolean
) {
    ARABIC("ar", "العربية", "🇸🇦", true),
    ENGLISH("en", "English", "🇺🇸", false),
    FRENCH("fr", "Français", "🇫🇷", false)
}

object AppStrings {
    fun appName(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "خزنة الصور والفيديو"
        AppLanguage.ENGLISH -> "Photo & Video Vault"
        AppLanguage.FRENCH -> "Coffre Photos et Vidéos"
    }

    fun proEdition(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "خزنة الصور والفيديو Pro"
        AppLanguage.ENGLISH -> "Photo & Video Vault Pro"
        AppLanguage.FRENCH -> "Coffre Photos & Vidéos Pro"
    }

    fun subtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مشفرة بـ AES-256 محلياً"
        AppLanguage.ENGLISH -> "AES-256 Hardware Encrypted"
        AppLanguage.FRENCH -> "Chiffré localement AES-256"
    }

    fun enterPin(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أدخل رمز PIN لفك القفل"
        AppLanguage.ENGLISH -> "Enter PIN to Unlock"
        AppLanguage.FRENCH -> "Entrez le code PIN pour déverrouiller"
    }

    fun drawPattern(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "ارسم نمط الشاشة (شيما)"
        AppLanguage.ENGLISH -> "Draw Pattern Gesture"
        AppLanguage.FRENCH -> "Dessinez le schéma de déverrouillage"
    }

    fun pinLock(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "رمز PIN"
        AppLanguage.ENGLISH -> "PIN Code"
        AppLanguage.FRENCH -> "Code PIN"
    }

    fun patternLock(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "نمط الشاشة (شيما)"
        AppLanguage.ENGLISH -> "Pattern (Schéma)"
        AppLanguage.FRENCH -> "Schéma (Motif)"
    }

    fun biometricUnlock(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "فتح بالبصمة أو الوجه"
        AppLanguage.ENGLISH -> "Unlock with Biometrics"
        AppLanguage.FRENCH -> "Déverrouiller avec biométrie"
    }

    fun allMedia(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "كل الوسائط"
        AppLanguage.ENGLISH -> "All Media"
        AppLanguage.FRENCH -> "Tous les médias"
    }

    fun photos(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الصور"
        AppLanguage.ENGLISH -> "Photos"
        AppLanguage.FRENCH -> "Photos"
    }

    fun videos(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الفيديوهات"
        AppLanguage.ENGLISH -> "Videos"
        AppLanguage.FRENCH -> "Vidéos"
    }

    fun favorites(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المفضلة"
        AppLanguage.ENGLISH -> "Favorites"
        AppLanguage.FRENCH -> "Favoris"
    }

    fun albums(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الألبومات"
        AppLanguage.ENGLISH -> "Albums"
        AppLanguage.FRENCH -> "Albums"
    }

    fun trash(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المهملات"
        AppLanguage.ENGLISH -> "Trash"
        AppLanguage.FRENCH -> "Corbeille"
    }

    fun security(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الأمان"
        AppLanguage.ENGLISH -> "Security"
        AppLanguage.FRENCH -> "Sécurité"
    }

    fun database(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "قاعدة البيانات"
        AppLanguage.ENGLISH -> "Database"
        AppLanguage.FRENCH -> "Base de données"
    }

    fun importMedia(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "استيراد وسائط"
        AppLanguage.ENGLISH -> "Import Media"
        AppLanguage.FRENCH -> "Importer des médias"
    }

    fun developerStore(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "متجر المطور (تليجرام)"
        AppLanguage.ENGLISH -> "Developer Store (Telegram)"
        AppLanguage.FRENCH -> "Boutique Développeur (Telegram)"
    }

    fun visitStore(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "زيارة المتجر"
        AppLanguage.ENGLISH -> "Visit Store"
        AppLanguage.FRENCH -> "Visiter la boutique"
    }

    fun releaseDateLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تاريخ الإصدار"
        AppLanguage.ENGLISH -> "Release Date"
        AppLanguage.FRENCH -> "Date de sortie"
    }

    fun releaseDateValue(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "02 أكتوبر 2026"
        AppLanguage.ENGLISH -> "October 02, 2026"
        AppLanguage.FRENCH -> "02 Octobre 2026"
    }

    fun developerEmail(lang: AppLanguage) = "ahmedbecetti41@gmail.com"

    fun storeUrl(lang: AppLanguage) = "https://t.me/seniordesigners"

    fun language(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "اللغة"
        AppLanguage.ENGLISH -> "Language"
        AppLanguage.FRENCH -> "Langue"
    }

    fun changeLanguage(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تغيير لغة التطبيق"
        AppLanguage.ENGLISH -> "Change Language"
        AppLanguage.FRENCH -> "Changer de langue"
    }
}
