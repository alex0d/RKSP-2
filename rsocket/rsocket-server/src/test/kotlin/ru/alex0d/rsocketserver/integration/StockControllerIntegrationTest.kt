package ru.alex0d.rsocketserver.integration

import io.rsocket.frame.decoder.PayloadDecoder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.util.MimeTypeUtils
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.retry.Retry
import ru.alex0d.rsocketserver.dto.MarketData
import ru.alex0d.rsocketserver.model.Stock
import ru.alex0d.rsocketserver.repository.StockRepository
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockControllerIntegrationTest {

    @LocalRSocketServerPort
    private var port: Int = 0

    private lateinit var requester: RSocketRequester

    @Autowired
    private lateinit var stockRepository: StockRepository

    private var testStock = Stock("AAPL", "Apple Inc.", "NASDAQ", "Technology")

    @BeforeAll
    fun setup() {
        requester = RSocketRequester.builder()
            .rsocketStrategies { builder ->
                builder.decoder(Jackson2JsonDecoder())
                builder.encoder(Jackson2JsonEncoder())
            }
            .rsocketConnector { connector ->
                connector.payloadDecoder(PayloadDecoder.ZERO_COPY)
                    .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
            }
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp("localhost", port)
    }

    @BeforeAll
    fun saveTestStock() = runTest {
        testStock = stockRepository.save(testStock)
    }

    @AfterAll
    fun deleteTestStock() = runTest {
        stockRepository.delete(testStock)
    }

    @Test
    fun testGetAllStocks() = runTest {
        val result: Flow<Stock> = requester
            .route("api.stocks.getAll")
            .retrieveFlow()

        val stocks = result.toList()
        assertNotNull(stocks)
        assert(stocks.isNotEmpty())
    }

    @Test
    fun testGetStock() = runTest {
        val result: Mono<Stock> = requester
            .route("api.stocks.get")
            .data(testStock.id!!)
            .retrieveMono()

        StepVerifier.create(result)
            .assertNext { stock ->
                assertEquals(testStock.id, stock.id)
                assertEquals(testStock.ticker, stock.ticker)
                assertEquals(testStock.name, stock.name)
            }
            .verifyComplete()
    }

    @Test
    fun testAddStock() = runTest {
        val newStock = Stock("MSFT", "Microsoft Corporation", "NASDAQ", "Technology")

        val result: Mono<Stock> = requester
            .route("api.stocks.add")
            .data(newStock)
            .retrieveMono()

        StepVerifier.create(result)
            .assertNext { stock ->
                assertNotNull(stock.id)
                assertEquals("MSFT", stock.ticker)
                assertEquals("Microsoft Corporation", stock.name)
            }
            .verifyComplete()
    }

    @Test
    fun testDeleteStock() = runTest {
        val result: Mono<Void> = requester
            .route("api.stocks.delete")
            .data(1L)
            .retrieveMono()

        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun testGetMarketData() = runTest {
        val tickers = flowOf("AAPL", "GOOGL")

        val result: Flow<MarketData> = requester
            .route("api.stocks.marketData")
            .data(tickers)
            .retrieveFlow()

        val marketDataList = result.toList()
        assertEquals(2, marketDataList.size)
        assert(marketDataList.any { it.ticker == "AAPL" })
        assert(marketDataList.any { it.ticker == "GOOGL" })
        assert(marketDataList.all { it.price in 100.0..1000.0 })
    }
}