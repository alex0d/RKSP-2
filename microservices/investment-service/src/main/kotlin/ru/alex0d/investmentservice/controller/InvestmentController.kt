package ru.alex0d.investmentservice.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentservice.auth.UserPrincipal
import ru.alex0d.investmentservice.dto.BuyStockRequest
import ru.alex0d.investmentservice.service.InvestmentService

@RestController
@RequestMapping("/api/investments")
class InvestmentController(
    private val investmentService: InvestmentService
) {
    @GetMapping("/stocks")
    fun getAllStocks() = investmentService.getAllStocks()

    @PostMapping("/buy")
    fun buyStock(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestBody request: BuyStockRequest
    ) = investmentService.buyStock(request, principal.username, principal.tg)

    @GetMapping("/portfolio")
    fun getPortfolio(
        @AuthenticationPrincipal principal: UserPrincipal
    ) = investmentService.getPortfolio(principal.username)
}