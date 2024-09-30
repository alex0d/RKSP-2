package ru.alex0d.rsocketserver.controller

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import ru.alex0d.rsocketserver.dto.MarketData
import ru.alex0d.rsocketserver.model.Stock
import ru.alex0d.rsocketserver.repository.StockRepository
import kotlin.random.Random

@Controller
@MessageMapping("api.stocks")
class StockController(
    private val stockRepository: StockRepository
) {

    @MessageMapping("getAll")
    fun getAllStocks(): Flow<Stock> {
        return stockRepository.findAll()
    }

    @MessageMapping("get")
    suspend fun getStock(id: Long): Stock? {
        return stockRepository.findById(id)
    }

    @MessageMapping("add")
    suspend fun addStock(stock: Stock): Stock {
        return stockRepository.save(stock)
    }

    @MessageMapping("delete")
    suspend fun deleteStock(id: Long) {
        stockRepository.deleteById(id)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @MessageMapping("marketData")
    fun getMarketData(tickers: Flow<String>): Flow<MarketData> {
        return tickers.flatMapMerge {
            flow {
                delay(1000)
                emit(MarketData(it, Random.nextDouble(100.0, 1000.0)))
            }
        }
    }
}