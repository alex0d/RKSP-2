package ru.alex0d.rsocketserver.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("stocks")
data class Stock(
    val ticker: String,
    val name: String,
    val exchange: String,
    val sector: String,
    @Id val id: Long? = null
)