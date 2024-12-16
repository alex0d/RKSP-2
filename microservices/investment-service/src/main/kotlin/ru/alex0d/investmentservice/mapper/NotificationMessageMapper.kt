package ru.alex0d.investmentservice.mapper

import ru.alex0d.investmentservice.dto.PortfolioDto

object NotificationMessageMapper {
    fun map(portfolio: PortfolioDto): String {
        return buildString {
            appendLine("🎉 Portfolio Update:")
            appendLine("Total Value: $${portfolio.totalValue}")
            appendLine("\nPositions:")
            portfolio.stocks.forEach { position ->
                appendLine("- ${position.symbol}: ${position.quantity} shares")
                appendLine("  Current Price: $${position.currentPrice}")
                appendLine("  Total Value: $${position.totalValue}")
            }
        }
    }
}