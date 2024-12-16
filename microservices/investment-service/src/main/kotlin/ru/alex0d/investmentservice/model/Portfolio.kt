package ru.alex0d.investmentservice.model

import jakarta.persistence.*

@Entity
class Portfolio(
    var username: String,

    @OneToMany(cascade = [CascadeType.ALL])
    val stocks: MutableList<PortfolioStock> = mutableListOf(),

    @Id @GeneratedValue
    var id: Long? = null,
)