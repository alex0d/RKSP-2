package ru.alex0d.investmentservice.dto

data class NotificationRequest(
    val tg: String,
    val message: String
)