package ru.alex0d.investmentservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alex0d.investmentservice.model.Stock

interface StockRepository : JpaRepository<Stock, Long> {
    fun findBySymbol(symbol: String): Stock?
}