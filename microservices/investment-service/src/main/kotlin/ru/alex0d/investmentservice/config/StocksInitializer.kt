package ru.alex0d.investmentservice.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import ru.alex0d.investmentservice.model.Stock
import ru.alex0d.investmentservice.repository.StockRepository
import java.math.BigDecimal

@Component
class StocksInitializer(
    private val stockRepository: StockRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        if (stockRepository.count() == 0L) {
            val stocks = listOf(
                Stock(symbol = "AAPL", name = "Apple Inc.", price = BigDecimal("150.00")),
                Stock(symbol = "GOOGL", name = "Alphabet Inc.", price = BigDecimal("2800.00")),
                Stock(symbol = "MSFT", name = "Microsoft Corporation", price = BigDecimal("280.00")),
                Stock(symbol = "AMZN", name = "Amazon.com Inc.", price = BigDecimal("3200.00")),
                Stock(symbol = "TSLA", name = "Tesla Inc.", price = BigDecimal("900.00")),
                Stock(symbol = "META", name = "Meta Platforms Inc.", price = BigDecimal("330.00")),
                Stock(symbol = "NVDA", name = "NVIDIA Corporation", price = BigDecimal("240.00")),
                Stock(symbol = "JPM", name = "JPMorgan Chase & Co.", price = BigDecimal("160.00")),
                Stock(symbol = "V", name = "Visa Inc.", price = BigDecimal("220.00")),
                Stock(symbol = "JNJ", name = "Johnson & Johnson", price = BigDecimal("170.00"))
            )
            stockRepository.saveAll(stocks)
        }
    }
}