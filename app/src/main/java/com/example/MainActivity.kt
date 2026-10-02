package com.example

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LockType
import com.example.security.BiometricHelper
import com.example.ui.VaultScreen
import com.example.ui.VaultViewModel
import com.example.ui.components.CalculatorDisguiseView
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : FragmentActivity() {
    private val viewModel: VaultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        VaultApp(
                            viewModel = viewModel,
                            onLaunchBiometric = { launchBiometricAuth() },
                            isBiometricAvailable = BiometricHelper.isBiometricAvailable(this)
                        )
                    }
                }
            }
        }
    }

    private fun launchBiometricAuth() {
        BiometricHelper.authenticate(
            activity = this,
            onSuccess = {
                viewModel.onBiometricSuccess()
            },
            onError = { _ ->
                // Handled gracefully in UI
            },
            onFailed = {
                viewModel.onBiometricFailed()
            }
        )
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.settings.value.autoLockOnBackground) {
            viewModel.lockApp()
        }
    }
}

@Composable
fun VaultApp(
    viewModel: VaultViewModel,
    onLaunchBiometric: () -> Unit,
    isBiometricAvailable: Boolean
) {
    val isUnlocked by viewModel.isUnlocked.collectAsStateWithLifecycle()
    val isDecoyVault by viewModel.isDecoyVault.collectAsStateWithLifecycle()
    val isCalculatorMode by viewModel.isCalculatorMode.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val mediaList by viewModel.mediaList.collectAsStateWithLifecycle()
    val albumsList by viewModel.albumsList.collectAsStateWithLifecycle()
    val trashList by viewModel.trashList.collectAsStateWithLifecycle()
    val securityLogs by viewModel.securityLogs.collectAsStateWithLifecycle()
    val selectedMediaItem by viewModel.selectedMediaItem.collectAsStateWithLifecycle()
    val currentPin by viewModel.currentPinInput.collectAsStateWithLifecycle()
    val isLockError by viewModel.isLockError.collectAsStateWithLifecycle()
    val failedAttempts by viewModel.failedAttempts.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    // Handle Back Press navigation
    BackHandler {
        when {
            selectedMediaItem != null -> viewModel.closeMediaViewer()
            currentScreen != VaultScreen.HOME -> viewModel.navigateTo(VaultScreen.HOME)
            isUnlocked -> viewModel.lockApp()
        }
    }

    // Auto-prompt biometrics once on start if enabled
    var promptedOnce by remember { mutableStateOf(false) }
    LaunchedEffect(isUnlocked, isCalculatorMode) {
        if (!isUnlocked && !isCalculatorMode && settings.isBiometricEnabled && isBiometricAvailable && !promptedOnce) {
            promptedOnce = true
            onLaunchBiometric()
        }
    }

    if (!isUnlocked) {
        if (isCalculatorMode) {
            CalculatorDisguiseView(
                secretPin = settings.pinCode,
                decoyPin = settings.decoyPin,
                onSecretMatched = { isDecoy ->
                    if (isDecoy) {
                        viewModel.onNumberInput(settings.decoyPin)
                    } else {
                        viewModel.onNumberInput(settings.pinCode)
                    }
                    viewModel.setCalculatorMode(false)
                },
                onExitDisguise = {
                    viewModel.setCalculatorMode(false)
                }
            )
        } else {
            LockScreen(
                activeLockType = settings.activeLockType,
                currentPin = currentPin,
                isError = isLockError,
                failedAttempts = failedAttempts,
                isBiometricAvailable = isBiometricAvailable && settings.isBiometricEnabled,
                onNumberClick = { viewModel.onNumberInput(it) },
                onDeleteClick = { viewModel.onDeletePinDigit() },
                onPatternComplete = { viewModel.onPatternCompleted(it) },
                onBiometricClick = onLaunchBiometric,
                onSwitchLockType = { newType ->
                    viewModel.updateSettings(settings.copy(activeLockType = newType))
                },
                onOpenCalculator = {
                    viewModel.setCalculatorMode(true)
                }
            )
        }
    } else {
        // App is Unlocked!
        if (selectedMediaItem != null) {
            MediaViewerScreen(
                mediaItem = selectedMediaItem!!,
                onBack = { viewModel.closeMediaViewer() },
                onToggleFavorite = { viewModel.toggleFavorite(selectedMediaItem!!) },
                onDelete = { viewModel.moveToTrash(selectedMediaItem!!) },
                onRestoreOrExport = {
                    viewModel.restoreFromTrash(selectedMediaItem!!)
                }
            )
        } else {
            when (currentScreen) {
                VaultScreen.HOME -> {
                    VaultHomeScreen(
                        mediaList = mediaList,
                        isDecoy = isDecoyVault,
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.setTab(it) },
                        onMediaClick = { viewModel.openMediaViewer(it) },
                        onImportMedia = { uris -> viewModel.importMediaUris(uris) },
                        onLockApp = { viewModel.lockApp() },
                        onOpenAlbums = { viewModel.navigateTo(VaultScreen.ALBUMS) },
                        onOpenSecurity = { viewModel.navigateTo(VaultScreen.SECURITY_CENTER) },
                        onOpenTrash = { viewModel.navigateTo(VaultScreen.RECYCLE_BIN) },
                        onOpenAboutDev = { viewModel.navigateTo(VaultScreen.ABOUT_DEV) },
                        onOpenStore = { viewModel.navigateTo(VaultScreen.APP_STORE) }
                    )
                }
                VaultScreen.ALBUMS -> {
                    AlbumsScreen(
                        albums = albumsList,
                        mediaList = mediaList,
                        onBack = { viewModel.navigateTo(VaultScreen.HOME) },
                        onAlbumSelect = { albumId ->
                            viewModel.selectAlbum(albumId)
                            viewModel.navigateTo(VaultScreen.HOME)
                        },
                        onCreateAlbum = { name, icon ->
                            viewModel.createAlbum(name, icon)
                        }
                    )
                }
                VaultScreen.SECURITY_CENTER -> {
                    SecurityCenterScreen(
                        settings = settings,
                        securityLogs = securityLogs,
                        onBack = { viewModel.navigateTo(VaultScreen.HOME) },
                        onUpdateSettings = { viewModel.updateSettings(it) },
                        onClearLogs = { viewModel.clearSecurityLogs() }
                    )
                }
                VaultScreen.RECYCLE_BIN -> {
                    RecycleBinScreen(
                        trashItems = trashList,
                        onBack = { viewModel.navigateTo(VaultScreen.HOME) },
                        onRestore = { viewModel.restoreFromTrash(it) },
                        onPermanentDelete = { viewModel.permanentlyDelete(it) },
                        onEmptyTrash = { viewModel.emptyTrash() }
                    )
                }
                VaultScreen.ABOUT_DEV -> {
                    AboutDeveloperScreen(
                        onBack = { viewModel.navigateTo(VaultScreen.HOME) }
                    )
                }
                VaultScreen.APP_STORE -> {
                    AppStoreScreen(
                        onBack = { viewModel.navigateTo(VaultScreen.HOME) }
                    )
                }
            }
        }
    }
}
