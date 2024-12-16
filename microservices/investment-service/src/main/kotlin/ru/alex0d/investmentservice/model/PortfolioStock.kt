package ru.alex0d.investmentservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class PortfolioStock(
    @ManyToOne
    var stock: Stock,
    var quantity: Int,

    @Id @GeneratedValue
    var id: Long? = null,
)