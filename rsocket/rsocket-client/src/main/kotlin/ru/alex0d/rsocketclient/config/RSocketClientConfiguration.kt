package ru.alex0d.rsocketclient.config

import io.rsocket.core.RSocketConnector
import io.rsocket.frame.decoder.PayloadDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.MimeTypeUtils
import reactor.util.retry.Retry
import java.time.Duration


@Configuration
class RSocketClientConfiguration {
    @Bean
    fun getRSocketRequester(builder: RSocketRequester.Builder): RSocketRequester {
        return builder
            .rsocketStrategies(clientRSocketStrategies())
            .rsocketConnector { connector: RSocketConnector ->
                connector
                    .payloadDecoder(PayloadDecoder.ZERO_COPY)
                    .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
            }
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp("localhost", 5200)
    }

    @Bean
    fun clientRSocketStrategies(): RSocketStrategies {
        return RSocketStrategies.builder()
            .encoder(Jackson2JsonEncoder())
            .decoder(Jackson2JsonDecoder())
            .build()
    }
}