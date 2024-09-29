package ru.alex0d.rsocketclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RSocketClient

fun main(args: Array<String>) {
    runApplication<RSocketClient>(*args)
}