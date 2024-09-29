package ru.alex0d.rksp2.practice3

import io.reactivex.rxjava3.core.Observable
import kotlin.random.Random

fun main() {
    // Задание 2.1.1: Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток,
    // содержащий квадраты данных чисел
    val task211 = Observable.range(0, 1000)
        .map { Random.nextInt(0, 1001) }
        .map { it * it }

    println("Задание 2.1.1:")
    task211.take(10).subscribe { println(it) }  // Первые 10 элементов для примера

    // Задание 2.2.1: Даны два потока по 1000 элементов: первый содержит случайную
    // букву, второй — случайную цифру. Сформировать поток, каждый элемент
    // которого объединяет элементы из обоих потоков. Например, при входных
    // потоках (A, B, C) и (1, 2, 3) выходной поток — (A1, B2, B3)
    val letters = ('A'..'Z').toList()
    val digits = ('0'..'9').toList()

    val letterStream = Observable.range(0, 1000)
        .map { letters.random() }

    val digitStream = Observable.range(0, 1000)
        .map { digits.random() }

    val task221 = Observable.zip(letterStream, digitStream) { letter, digit -> "$letter$digit" }

    println("\nЗадание 2.2.1:")
    task221.take(10).subscribe { println(it) }  // Первые 10 элементов для примера

    // Задание 2.3.1: Дан поток из 10 случайных чисел. Сформировать поток, содержащий
    // все числа, кроме первых трех
    val task231 = Observable.range(0, 10)
        .map { Random.nextInt(0, 101) }
        .skip(3)

    println("\nЗадание 2.3.1:")
    task231.subscribe { println(it) }
}