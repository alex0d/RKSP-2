package ru.alex0d.eurekaserver

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class EurekaHealthCheck : HealthIndicator {
    override fun health(): Health {
        return try {
            Health.up()
                .withDetail("status", "Eureka Server is running")
                .build()
        } catch (e: Exception) {
            Health.down()
                .withDetail("error", e.message)
                .build()
        }
    }
}