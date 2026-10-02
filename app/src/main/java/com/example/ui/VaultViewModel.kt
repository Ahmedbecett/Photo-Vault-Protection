package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.VaultRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
    ALL,
    PHOTOS,
    VIDEOS,
    FAVORITES
}

enum class VaultScreen {
    HOME,
    ALBUMS,
    SECURITY_CENTER,
    RECYCLE_BIN,
    ABOUT_DEV,
    APP_STORE
}

class VaultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = VaultRepository(application)

    val settings = repository.settings

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked = _isUnlocked.asStateFlow()

    private val _isDecoyVault = MutableStateFlow(false)
    val isDecoyVault = _isDecoyVault.asStateFlow()

    private val _currentScreen = MutableStateFlow(VaultScreen.HOME)
    val currentScreen = _currentScreen.asStateFlow()

    private val _selectedTab = MutableStateFlow(MainTab.ALL)
    val selectedTab = _selectedTab.asStateFlow()

    private val _selectedAlbumId = MutableStateFlow<String?>(null)
    val selectedAlbumId = _selectedAlbumId.asStateFlow()

    private val _selectedMediaItem = MutableStateFlow<MediaItem?>(null)
    val selectedMediaItem = _selectedMediaItem.asStateFlow()

    private val _currentPinInput = MutableStateFlow("")
    val currentPinInput = _currentPinInput.asStateFlow()

    private val _isLockError = MutableStateFlow(false)
    val isLockError = _isLockError.asStateFlow()

    private val _failedAttempts = MutableStateFlow(0)
    val failedAttempts = _failedAttempts.asStateFlow()

    private val _isCalculatorMode = MutableStateFlow(false)
    val isCalculatorMode = _isCalculatorMode.asStateFlow()

    // Data streams
    val mediaList = combine(_isDecoyVault, _selectedTab, _selectedAlbumId) { decoy, tab, albumId ->
        Triple(decoy, tab, albumId)
    }.flatMapLatest { (decoy, tab, albumId) ->
        if (albumId != null) {
            repository.getMediaByAlbum(decoy, albumId)
        } else {
            when (tab) {
                MainTab.ALL -> repository.getActiveMedia(decoy)
                MainTab.PHOTOS -> repository.getPhotos(decoy)
                MainTab.VIDEOS -> repository.getVideos(decoy)
                MainTab.FAVORITES -> repository.getFavorites(decoy)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val albumsList = _isDecoyVault.flatMapLatest { decoy ->
        repository.getAlbums(decoy)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashList = _isDecoyVault.flatMapLatest { decoy ->
        repository.getTrashMedia(decoy)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val securityLogs = repository.getSecurityLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            _isCalculatorMode.value = settings.value.isCalculatorDisguiseEnabled
        }
    }

    fun onNumberInput(digit: String) {
        if (_currentPinInput.value.length < 6) {
            _currentPinInput.value += digit
            _isLockError.value = false

            // Auto check when reaches 4 digits
            if (_currentPinInput.value.length >= 4) {
                checkPin(_currentPinInput.value)
            }
        }
    }

    fun onDeletePinDigit() {
        if (_currentPinInput.value.isNotEmpty()) {
            _currentPinInput.value = _currentPinInput.value.dropLast(1)
            _isLockError.value = false
        }
    }

    private fun checkPin(pin: String) {
        val currentSettings = settings.value
        when (pin) {
            currentSettings.pinCode -> {
                // Correct real PIN
                unlock(isDecoy = false, method = "رمز PIN")
            }
            currentSettings.decoyPin -> {
                // Decoy PIN entered!
                unlock(isDecoy = true, method = "رمز الخزنة الوهمية")
            }
            else -> {
                if (pin.length == currentSettings.pinCode.length) {
                    onLockFailed("رمز PIN غير صحيح: $pin")
                }
            }
        }
    }

    fun onPatternCompleted(patternNodes: List<Int>) {
        val currentSettings = settings.value
        val patternString = patternNodes.joinToString(",")
        when (patternString) {
            currentSettings.patternCode -> {
                unlock(isDecoy = false, method = "نمط الشاشة (شيما)")
            }
            currentSettings.decoyPattern -> {
                unlock(isDecoy = true, method = "نمط الخزنة الوهمية")
            }
            else -> {
                onLockFailed("نمط غير صحيح (شيما)")
            }
        }
    }

    fun onBiometricSuccess() {
        unlock(isDecoy = false, method = "المصادقة البيومترية (بصمة/وجه)")
    }

    fun onBiometricFailed() {
        viewModelScope.launch {
            repository.logSecurityAttempt("بصمة/وجه", false, "فشلت مطابقة البصمة أو الوجه")
        }
    }

    private fun unlock(isDecoy: Boolean, method: String) {
        _isUnlocked.value = true
        _isDecoyVault.value = isDecoy
        _currentPinInput.value = ""
        _isLockError.value = false
        _failedAttempts.value = 0

        viewModelScope.launch {
            repository.logSecurityAttempt(method, true, "تم إلغاء القفل بنجاح ($method)")
        }
    }

    private fun onLockFailed(details: String) {
        _isLockError.value = true
        _failedAttempts.value += 1
        _currentPinInput.value = ""

        viewModelScope.launch {
            repository.logSecurityAttempt(
                if (settings.value.activeLockType == LockType.PIN) "رمز PIN" else "نمط الشاشة (شيما)",
                false,
                details
            )
        }
    }

    fun lockApp() {
        _isUnlocked.value = false
        _currentPinInput.value = ""
        _isLockError.value = false
        _selectedMediaItem.value = null
        if (settings.value.isCalculatorDisguiseEnabled) {
            _isCalculatorMode.value = true
        }
    }

    fun setCalculatorMode(enabled: Boolean) {
        _isCalculatorMode.value = enabled
    }

    fun setTab(tab: MainTab) {
        _selectedTab.value = tab
        _selectedAlbumId.value = null
    }

    fun selectAlbum(albumId: String?) {
        _selectedAlbumId.value = albumId
    }

    fun navigateTo(screen: VaultScreen) {
        _currentScreen.value = screen
    }

    fun openMediaViewer(item: MediaItem) {
        _selectedMediaItem.value = item
    }

    fun closeMediaViewer() {
        _selectedMediaItem.value = null
    }

    fun importMediaUris(uris: List<Uri>) {
        viewModelScope.launch {
            val decoy = _isDecoyVault.value
            val album = _selectedAlbumId.value ?: "default"
            for (uri in uris) {
                repository.importMediaFromUri(uri, isDecoy = decoy, albumId = album)
            }
        }
    }

    fun toggleFavorite(item: MediaItem) {
        viewModelScope.launch {
            repository.toggleFavorite(item)
            if (_selectedMediaItem.value?.id == item.id) {
                _selectedMediaItem.value = item.copy(isFavorite = !item.isFavorite)
            }
        }
    }

    fun moveToTrash(item: MediaItem) {
        viewModelScope.launch {
            repository.moveToTrash(item)
            if (_selectedMediaItem.value?.id == item.id) {
                _selectedMediaItem.value = null
            }
        }
    }

    fun restoreFromTrash(item: MediaItem) {
        viewModelScope.launch {
            repository.restoreFromTrash(item)
        }
    }

    fun permanentlyDelete(item: MediaItem) {
        viewModelScope.launch {
            repository.permanentlyDelete(item)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            repository.emptyTrash(_isDecoyVault.value)
        }
    }

    fun createAlbum(name: String, iconName: String) {
        viewModelScope.launch {
            repository.createAlbum(name, iconName, _isDecoyVault.value)
        }
    }

    fun updateSettings(newSettings: VaultSettings) {
        viewModelScope.launch {
            repository.updateSettings(newSettings)
        }
    }

    fun clearSecurityLogs() {
        viewModelScope.launch {
            repository.clearSecurityLogs()
        }
    }
}
