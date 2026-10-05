package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatMessage
import com.example.data.CurrencyType
import com.example.data.LiveCurrencyRate
import com.example.data.NewsArticle
import com.example.data.NewsCategory
import com.example.data.NewsRepository
import com.example.data.VerificationStatus
import com.example.service.GeminiChatService
import com.example.service.LiveNewsService
import com.example.service.LiveRatesService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class VzlaHoyUiState(
    val currentTab: Int = 0,
    val rates: List<LiveCurrencyRate> = emptyList(),
    val lastRatesUpdated: String = "Actualizado recientemente",
    val isRatesRefreshing: Boolean = false,
    val newsList: List<NewsArticle> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val selectedStatusFilter: VerificationStatus? = null,
    val newsSearchQuery: String = "",
    val selectedArticle: NewsArticle? = null,
    val isNewsRefreshing: Boolean = false,
    // Calculator State
    val converterInput: String = "100",
    val converterBaseCurrency: String = "USD", // "USD", "VES", "EUR", "USDT"
    // Chat State
    val chatMessages: List<ChatMessage> = emptyList(),
    val chatInput: String = "",
    val isChatGenerating: Boolean = false
)

class VzlaHoyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VzlaHoyUiState())
    val uiState: StateFlow<VzlaHoyUiState> = _uiState.asStateFlow()

    private val numberFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.forLanguageTag("es-VE")))

    init {
        initializeData()
        fetchLiveNetworkData()
        startBackgroundMonitoring()
    }

    private fun initializeData() {
        // Updated baseline rates for current market reality
        val initialRates = listOf(
            LiveCurrencyRate(
                type = CurrencyType.BCV_USD,
                currentRate = 871.37,
                previousRate = 869.60,
                changePercent = 0.20,
                lastUpdated = getCurrentTimeString(),
                sourceName = "Banco Central de Venezuela",
                isOfficial = true
            ),
            LiveCurrencyRate(
                type = CurrencyType.PARALELO_USD,
                currentRate = 972.27,
                previousRate = 966.45,
                changePercent = 0.60,
                lastUpdated = getCurrentTimeString(),
                sourceName = "Monitor Dólar Venezuela",
                isOfficial = false
            ),
            LiveCurrencyRate(
                type = CurrencyType.BINANCE_USDT,
                currentRate = 977.45,
                previousRate = 972.60,
                changePercent = 0.50,
                lastUpdated = getCurrentTimeString(),
                sourceName = "Binance P2P Mercado VES",
                isOfficial = false
            ),
            LiveCurrencyRate(
                type = CurrencyType.BCV_EUR,
                currentRate = 981.18,
                previousRate = 980.20,
                changePercent = 0.10,
                lastUpdated = getCurrentTimeString(),
                sourceName = "Banco Central de Venezuela",
                isOfficial = true
            )
        )

        val welcomeChatMessage = ChatMessage(
            text = """
                👋 ¡Hola! Soy tu **Buscador y Asistente Inteligente VzlaHoy**.
                
                Pregúntame sobre **cualquier tema** (ciencia, tecnología, historia, dudas del día a día, o cotizaciones y finanzas en tiempo real) y te responderé de forma directa, rápida y objetiva.
            """.trimIndent(),
            isUser = false
        )

        _uiState.update { current ->
            current.copy(
                rates = initialRates,
                lastRatesUpdated = "Hoy a las ${getCurrentTimeString()}",
                newsList = NewsRepository.getInitialNews().sortedByDescending { it.publishedAtMillis },
                chatMessages = listOf(welcomeChatMessage)
            )
        }
    }

    private fun fetchLiveNetworkData() {
        viewModelScope.launch {
            try {
                val liveRates = LiveRatesService.fetchLiveRates()
                if (liveRates.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(
                            rates = liveRates,
                            lastRatesUpdated = "Hoy a las ${getCurrentTimeString()}"
                        )
                    }
                }
            } catch (e: Exception) {
                // Keep resilient fallback
            }

            try {
                val liveNews = LiveNewsService.fetchLiveNews()
                if (liveNews.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(newsList = liveNews.sortedByDescending { it.publishedAtMillis })
                    }
                }
            } catch (e: Exception) {
                // Keep resilient fallback
            }
        }
    }

    private fun startBackgroundMonitoring() {
        viewModelScope.launch {
            while (isActive) {
                delay(30_000) // Silent background live refresh every 30 seconds
                try {
                    val freshRates = LiveRatesService.fetchLiveRates()
                    if (freshRates.isNotEmpty()) {
                        _uiState.update { state ->
                            state.copy(
                                rates = freshRates,
                                lastRatesUpdated = "Hoy a las ${getCurrentTimeString()}"
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Ignore background transient network hiccups
                }

                try {
                    val freshNews = LiveNewsService.fetchLiveNews()
                    if (freshNews.isNotEmpty()) {
                        _uiState.update { state ->
                            state.copy(newsList = freshNews.sortedByDescending { it.publishedAtMillis })
                        }
                    }
                } catch (e: Exception) {
                    // Ignore background transient network hiccups
                }
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(currentTab = tabIndex) }
    }

    fun refreshRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRatesRefreshing = true) }
            try {
                val liveRates = LiveRatesService.fetchLiveRates()
                if (liveRates.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(
                            rates = liveRates,
                            lastRatesUpdated = "Hoy a las ${getCurrentTimeString()}"
                        )
                    }
                }
            } catch (e: Exception) {
                // fallback
            }
            _uiState.update { it.copy(isRatesRefreshing = false) }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isNewsRefreshing = true) }
            try {
                val freshNews = LiveNewsService.fetchLiveNews()
                if (freshNews.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(newsList = freshNews.sortedByDescending { it.publishedAtMillis })
                    }
                }
            } catch (e: Exception) {
                // fallback
            }
            _uiState.update { it.copy(isNewsRefreshing = false) }
        }
    }

    // News Operations
    fun setNewsCategory(category: NewsCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setNewsStatusFilter(status: VerificationStatus?) {
        _uiState.update { it.copy(selectedStatusFilter = status) }
    }

    fun setNewsSearchQuery(query: String) {
        _uiState.update { it.copy(newsSearchQuery = query) }
    }

    fun selectArticle(article: NewsArticle?) {
        _uiState.update { it.copy(selectedArticle = article) }
    }

    fun askAiAboutArticle(article: NewsArticle) {
        val prompt = "¿Podrías darme un análisis financiero y de impacto sobre esta noticia: '${article.title}'?"
        selectArticle(null)
        selectTab(2) // Jump to Gemini tab
        sendChatMessage(prompt)
    }

    // Calculator Operations
    fun setConverterInput(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' || it == ',' }.replace(',', '.')
        _uiState.update { it.copy(converterInput = sanitized) }
    }

    fun setConverterBaseCurrency(currencyCode: String) {
        _uiState.update { it.copy(converterBaseCurrency = currencyCode) }
    }

    fun swapConverterCurrency() {
        _uiState.update { state ->
            val nextCurrency = when (state.converterBaseCurrency) {
                "USD" -> "VES"
                "VES" -> "USD"
                "EUR" -> "VES"
                "USDT" -> "VES"
                else -> "USD"
            }
            state.copy(converterBaseCurrency = nextCurrency)
        }
    }

    fun applyPresetAmount(amount: Double) {
        _uiState.update { it.copy(converterInput = if (amount % 1.0 == 0.0) amount.toInt().toString() else amount.toString()) }
    }

    // Chat Operations
    fun updateChatInput(text: String) {
        _uiState.update { it.copy(chatInput = text) }
    }

    fun sendChatMessage(promptText: String? = null) {
        val messageToSend = (promptText ?: _uiState.value.chatInput).trim()
        if (messageToSend.isBlank() || _uiState.value.isChatGenerating) return

        val userMessage = ChatMessage(text = messageToSend, isUser = true)

        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + userMessage,
                chatInput = if (promptText == null) "" else state.chatInput,
                isChatGenerating = true
            )
        }

        viewModelScope.launch {
            val rates = _uiState.value.rates
            val bcvRate = rates.find { it.type == CurrencyType.BCV_USD }?.currentRate ?: 871.37
            val paraleloRate = rates.find { it.type == CurrencyType.PARALELO_USD }?.currentRate ?: 972.27
            val binanceRate = rates.find { it.type == CurrencyType.BINANCE_USDT }?.currentRate ?: 977.45
            val eurRate = rates.find { it.type == CurrencyType.BCV_EUR }?.currentRate ?: 981.18

            val summary = "BCV: $bcvRate Bs | Paralelo: $paraleloRate Bs | Binance USDT: $binanceRate Bs | Euro BCV: $eurRate Bs"

            val aiAnswer = GeminiChatService.askAssistant(messageToSend, summary)

            val aiMessage = ChatMessage(
                text = aiAnswer,
                isUser = false
            )
            _uiState.update { state ->
                state.copy(
                    chatMessages = state.chatMessages + aiMessage,
                    isChatGenerating = false
                )
            }
        }
    }

    fun clearChat() {
        val welcomeChatMessage = ChatMessage(
            text = "¡Conversación reiniciada! ¿Sobre qué tema te gustaría consultar ahora?",
            isUser = false
        )
        _uiState.update { it.copy(chatMessages = listOf(welcomeChatMessage)) }
    }

    // Helper calculations
    fun getSpreadPercent(): Double {
        val bcv = _uiState.value.rates.find { it.type == CurrencyType.BCV_USD }?.currentRate ?: 871.37
        val paralelo = _uiState.value.rates.find { it.type == CurrencyType.PARALELO_USD }?.currentRate ?: 972.27
        if (bcv <= 0.0) return 0.0
        val spread = ((paralelo - bcv) / bcv) * 100.0
        return Math.round(spread * 100.0) / 100.0
    }

    fun formatMoney(amount: Double): String {
        return numberFormat.format(amount)
    }

    private fun getCurrentTimeString(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.forLanguageTag("es-VE"))
        return sdf.format(Date())
    }
}
