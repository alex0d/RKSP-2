package ru.alex0d.investmentservice.dto

import java.math.BigDecimal

data class StockDto(
    val symbol: String,
    val name: String,
    val price: BigDecimal
)

data class PortfolioDto(
    val stocks: List<PortfolioStockDto>,
    val totalValue: BigDecimal
)

data class PortfolioStockDto(
    val symbol: String,
    val quantity: Int,
    val currentPrice: BigDecimal,
    val totalValue: BigDecimal
)

data class BuyStockRequest(
    val symbol: String,
    val quantity: Int
)