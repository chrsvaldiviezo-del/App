package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.viewmodel.VzlaHoyViewModel

@Composable
fun MainScreen(
    viewModel: VzlaHoyViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Using WindowInsets.safeDrawing ensures no clashing with status bar HUD,
    // camera punch-holes or bottom navigation gesture bars/buttons
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            // STRICT USER REQUIREMENT:
            // "para navegar entre pestañas solo deben haber iconos en la parte inferior, nda de texto con esos iconos"
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                // Tab 0: Inicio / Noticias
                NavigationBarItem(
                    selected = uiState.currentTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == 0) Icons.Filled.Newspaper else Icons.Outlined.Newspaper,
                            contentDescription = "Noticias e inicio",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = null, // Strictly NO text as requested
                    alwaysShowLabel = false,
                    modifier = Modifier.testTag("nav_tab_news"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                )

                // Tab 1: Calculadora en tiempo real
                NavigationBarItem(
                    selected = uiState.currentTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == 1) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                            contentDescription = "Calculadora cambiaria",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = null, // Strictly NO text as requested
                    alwaysShowLabel = false,
                    modifier = Modifier.testTag("nav_tab_calculator"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                )

                // Tab 2: Buscador / Chat Gemini IA
                NavigationBarItem(
                    selected = uiState.currentTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == 2) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                            contentDescription = "Consultas Gemini IA",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = null, // Strictly NO text as requested
                    alwaysShowLabel = false,
                    modifier = Modifier.testTag("nav_tab_gemini"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> NewsScreen(
                        uiState = uiState,
                        onRateClick = {
                            // Jump directly to Calculator tab
                            viewModel.selectTab(1)
                        },
                        onCategorySelect = { viewModel.setNewsCategory(it) },
                        onStatusFilterSelect = { viewModel.setNewsStatusFilter(it) },
                        onSearchChange = { viewModel.setNewsSearchQuery(it) },
                        onArticleClick = { viewModel.selectArticle(it) },
                        onDismissArticleDetail = { viewModel.selectArticle(null) },
                        onAskAiAboutArticle = { viewModel.askAiAboutArticle(it) }
                    )

                    1 -> CalculatorScreen(
                        uiState = uiState,
                        onInputChange = { viewModel.setConverterInput(it) },
                        onBaseCurrencyChange = { viewModel.setConverterBaseCurrency(it) },
                        onSwapCurrency = { viewModel.swapConverterCurrency() },
                        onPresetSelect = { viewModel.applyPresetAmount(it) }
                    )

                    2 -> GeminiChatScreen(
                        uiState = uiState,
                        onInputChange = { viewModel.updateChatInput(it) },
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onClearChat = { viewModel.clearChat() }
                    )
                }
            }
        }
    }
}
