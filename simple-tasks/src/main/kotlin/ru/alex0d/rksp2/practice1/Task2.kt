package ru.alex0d.rksp2.practice1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

suspend fun slowlySquare(number: Long): Long = withContext(Dispatchers.Default) {
    delay(Random.nextLong(1000, 5000))
    number * number
}

suspend fun main() = withContext(Dispatchers.IO) {
    while (true) {
        val number = readln().toLongOrNull() ?: break
        launch {
            println("$number squared is ${slowlySquare(number)}")
        }
    }
}