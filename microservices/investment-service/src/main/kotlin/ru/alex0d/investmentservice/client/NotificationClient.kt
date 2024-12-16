package ru.alex0d.investmentservice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.alex0d.investmentservice.dto.NotificationRequest

@FeignClient("notification-service")
interface NotificationClient {
    @PostMapping("/api/notifications/send")
    fun sendNotification(@RequestBody request: NotificationRequest)
}