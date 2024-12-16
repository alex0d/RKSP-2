package ru.alex0d.investmentservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alex0d.investmentservice.model.Portfolio

interface PortfolioRepository : JpaRepository<Portfolio, Long> {
    fun findByUsername(username: String): Portfolio?
}