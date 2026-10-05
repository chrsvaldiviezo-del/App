package com.example.data

enum class CurrencyType(
    val code: String,
    val title: String,
    val shortName: String,
    val symbol: String,
    val defaultRate: Double
) {
    BCV_USD("USD_BCV", "Dólar BCV", "BCV", "$", 54.65),
    PARALELO_USD("USD_PARALELO", "Dólar Paralelo", "Paralelo", "$", 64.20),
    BINANCE_USDT("USDT_BINANCE", "Binance USDT", "Binance", "₮", 63.80),
    BCV_EUR("EUR_BCV", "Euro BCV", "Euro", "€", 59.45)
}

data class LiveCurrencyRate(
    val type: CurrencyType,
    val currentRate: Double,
    val previousRate: Double,
    val changePercent: Double,
    val lastUpdated: String,
    val sourceName: String,
    val isOfficial: Boolean
)

enum class VerificationStatus(
    val badgeText: String,
    val subtitle: String
) {
    VERIFIED("VERIFICADO", "Confirmado Oficial / Prensa Acreditada"),
    DEVELOPING("EN DESARROLLO", "Información en curso y contrastación"),
    RUMOR("RUMOR DE REDES", "No verificado — Viral en X / Telegram")
}

enum class NewsCategory(val displayName: String) {
    ALL("Todas"),
    POLITICS("Política"),
    ECONOMY("Economía"),
    NATIONAL("Nacional"),
    INTERNATIONAL("Internacional"),
    SERVICES("Servicios")
}

data class NewsArticle(
    val id: String,
    val title: String,
    val summary: String,
    val fullContent: String,
    val category: NewsCategory,
    val status: VerificationStatus,
    val source: String,
    val timeAgo: String,
    val verificationNotice: String,
    val factCheckNotes: String,
    val isBreaking: Boolean = false,
    val likesCount: Int = 120,
    val readsCount: String = "2.4k",
    val publishedAtMillis: Long = System.currentTimeMillis(),
    val sourceUrl: String = "https://www.bcv.org.ve/"
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isGenerating: Boolean = false,
    val relatedOptions: List<String> = emptyList()
)
