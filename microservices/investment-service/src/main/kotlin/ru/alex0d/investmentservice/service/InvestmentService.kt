package ru.alex0d.investmentservice.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.alex0d.investmentservice.client.NotificationClient
import ru.alex0d.investmentservice.dto.*
import ru.alex0d.investmentservice.repository.PortfolioRepository
import ru.alex0d.investmentservice.model.Portfolio
import ru.alex0d.investmentservice.model.PortfolioStock
import ru.alex0d.investmentservice.repository.StockRepository
import ru.alex0d.investmentservice.mapper.NotificationMessageMapper
import ru.alex0d.investmentservice.mapper.toDto
import java.math.BigDecimal

@Service
class InvestmentService(
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val notificationClient: NotificationClient,
) {
    fun getAllStocks(): List<StockDto> =
        stockRepository.findAll().map {
            StockDto(it.symbol, it.name, it.price)
        }

    @Transactional
    fun buyStock(request: BuyStockRequest, username: String, tg: String?): PortfolioDto {
        val stock = stockRepository.findBySymbol(request.symbol)
            ?: throw IllegalArgumentException("Stock not found")

        val portfolio = portfolioRepository.findByUsername(username)
            ?: Portfolio(username = username).also { portfolioRepository.save(it) }

        portfolio.stocks.add(PortfolioStock(stock = stock, quantity = request.quantity))
        portfolioRepository.save(portfolio)

        val portfolioDto = portfolio.toDto()

        tg?.let {
            val message = NotificationMessageMapper.map(portfolioDto)
            runCatching {
                notificationClient.sendNotification(NotificationRequest(it, message))
            }.onFailure {
                System.err.println("Failed to send notification: $it")
            }
        }

        return portfolioDto
    }

    fun getPortfolio(username: String): PortfolioDto {
        val portfolio = portfolioRepository.findByUsername(username)
            ?: return PortfolioDto(emptyList(), BigDecimal.ZERO)

        return portfolio.toDto()
    }
}