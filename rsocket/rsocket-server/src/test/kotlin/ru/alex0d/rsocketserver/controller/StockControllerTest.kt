package ru.alex0d.rsocketserver.controller

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.alex0d.rsocketserver.model.Stock
import ru.alex0d.rsocketserver.repository.StockRepository
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class StockControllerTest {

    private val stockRepository: StockRepository = mockk()

    private lateinit var stockController: StockController

    @BeforeEach
    fun setup() {
        stockController = StockController(stockRepository)
    }

    @Test
    fun `getAllStocks should return all stocks`() = runTest {
        val stocks = listOf(
            Stock("AAPL", "Apple Inc.", "NASDAQ", "Technology", 1L),
            Stock("GOOGL", "Alphabet Inc.", "NASDAQ", "Technology", 2L)
        )

        coEvery { stockRepository.findAll() } returns stocks.asFlow()

        val result = stockController.getAllStocks().toList()

        assertEquals(stocks, result)
        coVerify { stockRepository.findAll() }
    }

    @Test
    fun `getStock should return stock by id`() = runTest {
        val stock = Stock("AAPL", "Apple Inc.", "NASDAQ", "Technology", 1L)
        coEvery { stockRepository.findById(1L) } returns stock

        val result = stockController.getStock(1L)

        assertEquals(stock, result)
        coVerify { stockRepository.findById(1L) }
    }

    @Test
    fun `getStock should return null when stock not found`() = runTest {
        coEvery { stockRepository.findById(1L) } returns null

        val result = stockController.getStock(1L)

        assertNull(result)
        coVerify { stockRepository.findById(1L) }
    }

    @Test
    fun `addStock should save and return the new stock`() = runTest {
        val newStock = Stock("MSFT", "Microsoft Corporation", "NASDAQ", "Technology")
        val savedStock = newStock.copy(id = 1L)
        coEvery { stockRepository.save(newStock) } returns savedStock

        val result = stockController.addStock(newStock)

        assertEquals(savedStock, result)
        coVerify { stockRepository.save(newStock) }
    }

    @Test
    fun `deleteStock should call repository's deleteById`() = runTest {
        coJustRun { stockRepository.deleteById(1L) }

        stockController.deleteStock(1L)

        coVerify { stockRepository.deleteById(1L) }
    }

    @Test
    fun `getMarketData should return market data for given tickers`() = runTest {
        val tickers = flowOf("AAPL", "GOOGL")

        val result = stockController.getMarketData(tickers).toList()

        assertEquals(2, result.size)
        assertEquals("AAPL", result[0].ticker)
        assertEquals("GOOGL", result[1].ticker)
        assert(result[0].price in 100.0..1000.0)
        assert(result[1].price in 100.0..1000.0)
    }
}