package ru.alex0d.rsocketclient.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.alex0d.rsocketclient.dto.MarketData
import ru.alex0d.rsocketclient.dto.Stock

@RestController
@RequestMapping("/api")
class RequestResponseController(
    private val rSocketRequester: RSocketRequester
) {

    @GetMapping("/stocks")
    fun getStocks(): Flow<Stock> {
        return rSocketRequester
            .route("api.stocks.getAll")
            .retrieveFlow<Stock>()
    }

    @GetMapping("/stocks/{id}")
    fun getStockById(@PathVariable id: Long): Mono<Stock> {
        return rSocketRequester
            .route("api.stocks.get")
            .data(id)
            .retrieveMono<Stock>()
    }

    @PostMapping("/stocks")
    fun addStock(@RequestBody stock: Stock): Mono<Stock> {
        println(stock)
        return rSocketRequester
            .route("api.stocks.add")
            .data(stock)
            .retrieveMono<Stock>()
    }

    @DeleteMapping("/stocks/{id}")
    fun deleteStock(@PathVariable id: Long): Mono<Void> {
        return rSocketRequester
            .route("api.stocks.delete")
            .data(id)
            .send()
    }

    @GetMapping("/market-data")
    fun getMarketData(@RequestBody tickers: List<String>): Flow<MarketData> {
        return rSocketRequester
            .route("api.stocks.marketData")
            .data(tickers.asFlow())
            .retrieveFlow<MarketData>()
    }
}
