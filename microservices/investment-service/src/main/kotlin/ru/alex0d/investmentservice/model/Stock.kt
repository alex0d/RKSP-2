package ru.alex0d.investmentservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class Stock(
    var symbol: String,
    var name: String,
    var price: BigDecimal,

    @Id @GeneratedValue
    var id: Long? = null,
)