package ru.alex0d.investmentservice.mapper

import ru.alex0d.investmentservice.dto.PortfolioDto
import ru.alex0d.investmentservice.dto.PortfolioStockDto
import ru.alex0d.investmentservice.model.Portfolio
import java.math.BigDecimal

fun Portfolio.toDto(): PortfolioDto {
    val stocksDto = stocks.map {
        PortfolioStockDto(
            symbol = it.stock.symbol,
            quantity = it.quantity,
            currentPrice = it.stock.price,
            totalValue = it.stock.price.multiply(BigDecimal(it.quantity))
        )
    }

    val totalValue = stocksDto.fold(BigDecimal.ZERO) { acc, stock ->
        acc.add(stock.totalValue)
    }

    return PortfolioDto(stocksDto, totalValue)
}