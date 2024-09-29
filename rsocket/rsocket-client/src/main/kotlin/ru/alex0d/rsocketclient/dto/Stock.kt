package ru.alex0d.rsocketclient.dto

data class Stock(
    val ticker: String,
    val name: String,
    val exchange: String,
    val sector: String,
    val id: Long? = null
)