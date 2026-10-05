package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurrencyType
import com.example.data.LiveCurrencyRate
import com.example.ui.components.CurrencyBadgeIcon
import com.example.ui.theme.BcvBlue
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.EuroIndigo
import com.example.ui.theme.ParaleloGreen
import com.example.ui.theme.VerifiedGreen
import com.example.viewmodel.VzlaHoyUiState
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun CalculatorScreen(
    uiState: VzlaHoyUiState,
    onInputChange: (String) -> Unit,
    onBaseCurrencyChange: (String) -> Unit,
    onSwapCurrency: () -> Unit,
    onPresetSelect: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var includeIgtf by remember { mutableStateOf(false) }

    val formatter = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.forLanguageTag("es-VE"))) }

    // Rates map with current market fallbacks
    val bcvRate = uiState.rates.find { it.type == CurrencyType.BCV_USD }?.currentRate ?: 871.37
    val paraleloRate = uiState.rates.find { it.type == CurrencyType.PARALELO_USD }?.currentRate ?: 972.27
    val binanceRate = uiState.rates.find { it.type == CurrencyType.BINANCE_USDT }?.currentRate ?: 977.45
    val euroRate = uiState.rates.find { it.type == CurrencyType.BCV_EUR }?.currentRate ?: 981.18

    val amountValue = uiState.converterInput.toDoubleOrNull() ?: 0.0

    // Brecha cambiaria
    val spreadPercent = if (bcvRate > 0) ((paraleloRate - bcvRate) / bcvRate) * 100.0 else 0.0
    val spreadVes = paraleloRate - bcvRate

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copiado", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Screen Title Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Calculadora Cambiaria",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tasas oficiales y de mercado en tiempo real",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Brecha Cambiaria Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BRECHA CAMBIARIA",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.2f", spreadPercent)}% de diferencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = "+Bs. ${formatter.format(spreadVes)} / $",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ParaleloGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4 Official Daily Rates Grid Overview (BCV Dólar, Euro BCV, Paralelo, Binance)
        Text(
            text = "COTIZACIONES DEL DÍA",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // BCV Dólar
                MiniRateOverviewCard(
                    title = "Dólar BCV",
                    symbol = "$",
                    badgeText = "OFICIAL",
                    rateString = "Bs. ${formatter.format(bcvRate)}",
                    accentColor = BcvBlue,
                    type = CurrencyType.BCV_USD,
                    modifier = Modifier.weight(1f)
                )
                // Euro BCV
                MiniRateOverviewCard(
                    title = "Euro BCV",
                    symbol = "€",
                    badgeText = "OFICIAL",
                    rateString = "Bs. ${formatter.format(euroRate)}",
                    accentColor = EuroIndigo,
                    type = CurrencyType.BCV_EUR,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Paralelo
                MiniRateOverviewCard(
                    title = "Paralelo",
                    symbol = "$",
                    badgeText = "MERCADO",
                    rateString = "Bs. ${formatter.format(paraleloRate)}",
                    accentColor = ParaleloGreen,
                    type = CurrencyType.PARALELO_USD,
                    modifier = Modifier.weight(1f)
                )
                // Binance USDT
                MiniRateOverviewCard(
                    title = "Binance P2P",
                    symbol = "₮",
                    badgeText = "CRIPTO",
                    rateString = "Bs. ${formatter.format(binanceRate)}",
                    accentColor = BinanceGold,
                    type = CurrencyType.BINANCE_USDT,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interactive Live Converter Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Conversor en Tiempo Real",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onSwapCurrency,
                        modifier = Modifier.testTag("swap_currency_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Invertir moneda",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Base Currency Selection Tabs
                val currencies = listOf("USD", "VES", "EUR", "USDT")
                val selectedIndex = currencies.indexOf(uiState.converterBaseCurrency).coerceAtLeast(0)

                TabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    divider = {}
                ) {
                    currencies.forEachIndexed { index, code ->
                        val isSelected = selectedIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { onBaseCurrencyChange(code) },
                            modifier = Modifier.testTag("tab_currency_$code"),
                            text = {
                                Text(
                                    text = when (code) {
                                        "USD" -> "$ USD"
                                        "VES" -> "Bs. VES"
                                        "EUR" -> "€ EUR"
                                        "USDT" -> "₮ USDT"
                                        else -> code
                                    },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Amount Input Field
                OutlinedTextField(
                    value = uiState.converterInput,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("converter_amount_input"),
                    label = { Text("Monto a convertir (${uiState.converterBaseCurrency})") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        Text(
                            text = uiState.converterBaseCurrency,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Preset amount buttons
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    val presets = if (uiState.converterBaseCurrency == "VES") {
                        listOf(50.0, 100.0, 200.0, 500.0, 1000.0, 5000.0)
                    } else {
                        listOf(5.0, 10.0, 20.0, 50.0, 100.0, 500.0)
                    }

                    items(presets) { preset ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onPresetSelect(preset) }
                                .testTag("preset_${preset.toInt()}"),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = if (uiState.converterBaseCurrency == "VES") "Bs. ${preset.toInt()}" else "$${preset.toInt()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // IGTF 3% Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Calcular IGTF (3%)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Aplica a pagos en divisas / tiendas especiales",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = includeIgtf,
                        onCheckedChange = { includeIgtf = it },
                        modifier = Modifier.testTag("igtf_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(14.dp))

                // Calculated Results Breakdown Cards
                Text(
                    text = "RESULTADOS EN TIEMPO REAL",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Conversion calculation logic
                val igtfFactor = if (includeIgtf) 1.03 else 1.0

                when (uiState.converterBaseCurrency) {
                    "USD" -> {
                        val usdEffective = amountValue * igtfFactor
                        val vesBcv = usdEffective * bcvRate
                        val vesParalelo = usdEffective * paraleloRate
                        val vesBinance = usdEffective * binanceRate
                        val eurVal = (usdEffective * bcvRate) / euroRate

                        ConversionResultItem(
                            label = "Tasa Oficial BCV",
                            resultText = "Bs. ${formatter.format(vesBcv)}",
                            subtext = "Tasa: Bs. ${formatter.format(bcvRate)} / USD",
                            badgeColor = BcvBlue,
                            onCopy = { copyToClipboard("Monto BCV", "Bs. ${formatter.format(vesBcv)}") }
                        )

                        ConversionResultItem(
                            label = "Dólar Paralelo",
                            resultText = "Bs. ${formatter.format(vesParalelo)}",
                            subtext = "Tasa: Bs. ${formatter.format(paraleloRate)} / USD",
                            badgeColor = ParaleloGreen,
                            onCopy = { copyToClipboard("Monto Paralelo", "Bs. ${formatter.format(vesParalelo)}") }
                        )

                        ConversionResultItem(
                            label = "Binance USDT (P2P)",
                            resultText = "Bs. ${formatter.format(vesBinance)}",
                            subtext = "Tasa: Bs. ${formatter.format(binanceRate)} / USDT",
                            badgeColor = BinanceGold,
                            onCopy = { copyToClipboard("Monto Binance", "Bs. ${formatter.format(vesBinance)}") }
                        )

                        ConversionResultItem(
                            label = "Equivalente en Euros BCV",
                            resultText = "€ ${formatter.format(eurVal)}",
                            subtext = "Tasa Euro: Bs. ${formatter.format(euroRate)}",
                            badgeColor = EuroIndigo,
                            onCopy = { copyToClipboard("Monto Euro", "€ ${formatter.format(eurVal)}") }
                        )
                    }

                    "VES" -> {
                        val usdBcv = if (bcvRate > 0) amountValue / bcvRate else 0.0
                        val usdParalelo = if (paraleloRate > 0) amountValue / paraleloRate else 0.0
                        val usdtBinance = if (binanceRate > 0) amountValue / binanceRate else 0.0
                        val eurBcv = if (euroRate > 0) amountValue / euroRate else 0.0

                        ConversionResultItem(
                            label = "En Dólares Oficial BCV",
                            resultText = "$ ${formatter.format(usdBcv)}",
                            subtext = "Tasa: Bs. ${formatter.format(bcvRate)}",
                            badgeColor = BcvBlue,
                            onCopy = { copyToClipboard("Monto USD BCV", "$ ${formatter.format(usdBcv)}") }
                        )

                        ConversionResultItem(
                            label = "En Dólar Paralelo",
                            resultText = "$ ${formatter.format(usdParalelo)}",
                            subtext = "Tasa: Bs. ${formatter.format(paraleloRate)}",
                            badgeColor = ParaleloGreen,
                            onCopy = { copyToClipboard("Monto USD Paralelo", "$ ${formatter.format(usdParalelo)}") }
                        )

                        ConversionResultItem(
                            label = "En Binance USDT (P2P)",
                            resultText = "${formatter.format(usdtBinance)} USDT",
                            subtext = "Tasa: Bs. ${formatter.format(binanceRate)}",
                            badgeColor = BinanceGold,
                            onCopy = { copyToClipboard("Monto USDT", "${formatter.format(usdtBinance)} USDT") }
                        )

                        ConversionResultItem(
                            label = "En Euros BCV",
                            resultText = "€ ${formatter.format(eurBcv)}",
                            subtext = "Tasa: Bs. ${formatter.format(euroRate)}",
                            badgeColor = EuroIndigo,
                            onCopy = { copyToClipboard("Monto EUR BCV", "€ ${formatter.format(eurBcv)}") }
                        )
                    }

                    "EUR" -> {
                        val eurEffective = amountValue * igtfFactor
                        val vesEuro = eurEffective * euroRate
                        val usdBcv = if (bcvRate > 0) vesEuro / bcvRate else 0.0
                        val usdParalelo = if (paraleloRate > 0) vesEuro / paraleloRate else 0.0

                        ConversionResultItem(
                            label = "Total en Bolívares (Tasa Euro BCV)",
                            resultText = "Bs. ${formatter.format(vesEuro)}",
                            subtext = "Tasa: Bs. ${formatter.format(euroRate)} / EUR",
                            badgeColor = EuroIndigo,
                            onCopy = { copyToClipboard("Monto Bolívares", "Bs. ${formatter.format(vesEuro)}") }
                        )

                        ConversionResultItem(
                            label = "Equivalente en Dólar BCV",
                            resultText = "$ ${formatter.format(usdBcv)}",
                            subtext = "Tasa: Bs. ${formatter.format(bcvRate)}",
                            badgeColor = BcvBlue,
                            onCopy = { copyToClipboard("Monto USD BCV", "$ ${formatter.format(usdBcv)}") }
                        )

                        ConversionResultItem(
                            label = "Equivalente en Dólar Paralelo",
                            resultText = "$ ${formatter.format(usdParalelo)}",
                            subtext = "Tasa: Bs. ${formatter.format(paraleloRate)}",
                            badgeColor = ParaleloGreen,
                            onCopy = { copyToClipboard("Monto Paralelo", "$ ${formatter.format(usdParalelo)}") }
                        )
                    }

                    "USDT" -> {
                        val usdtEffective = amountValue * igtfFactor
                        val vesBinance = usdtEffective * binanceRate
                        val usdBcv = if (bcvRate > 0) vesBinance / bcvRate else 0.0
                        val eurVal = if (euroRate > 0) vesBinance / euroRate else 0.0

                        ConversionResultItem(
                            label = "Total en Bolívares (Binance P2P)",
                            resultText = "Bs. ${formatter.format(vesBinance)}",
                            subtext = "Tasa: Bs. ${formatter.format(binanceRate)} / USDT",
                            badgeColor = BinanceGold,
                            onCopy = { copyToClipboard("Monto Bolívares", "Bs. ${formatter.format(vesBinance)}") }
                        )

                        ConversionResultItem(
                            label = "Equivalente en Dólares BCV",
                            resultText = "$ ${formatter.format(usdBcv)}",
                            subtext = "Tasa: Bs. ${formatter.format(bcvRate)}",
                            badgeColor = BcvBlue,
                            onCopy = { copyToClipboard("Monto USD BCV", "$ ${formatter.format(usdBcv)}") }
                        )

                        ConversionResultItem(
                            label = "Equivalente en Euros BCV",
                            resultText = "€ ${formatter.format(eurVal)}",
                            subtext = "Tasa: Bs. ${formatter.format(euroRate)}",
                            badgeColor = EuroIndigo,
                            onCopy = { copyToClipboard("Monto Euro", "€ ${formatter.format(eurVal)}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniRateOverviewCard(
    title: String,
    symbol: String,
    badgeText: String,
    rateString: String,
    accentColor: Color,
    type: CurrencyType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyBadgeIcon(type = type, size = 28.dp)
                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = rateString,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ConversionResultItem(
    label: String,
    resultText: String,
    subtext: String,
    badgeColor: Color,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    Text(
                        text = resultText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtext,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            IconButton(
                onClick = onCopy,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("copy_result_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copiar resultado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
