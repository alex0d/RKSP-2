package ru.alex0d.fsspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FsSpringApp

fun main(args: Array<String>) {
    runApplication<FsSpringApp>(*args)
}
