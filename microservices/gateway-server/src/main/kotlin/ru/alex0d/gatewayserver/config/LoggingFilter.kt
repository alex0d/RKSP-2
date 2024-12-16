package ru.alex0d.gatewayserver.config

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingFilter : GlobalFilter {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        log.info(
            "Path: {}, Method: {}",
            exchange.request.path,
            exchange.request.method
        )
        return chain.filter(exchange)
    }
}