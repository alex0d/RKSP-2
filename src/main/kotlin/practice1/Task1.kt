package practice1

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask
import kotlin.random.Random
import kotlin.time.measureTimedValue

fun calculateSumSequentially(randomValues: List<Int>): Long {
    var sum = 0L
    for (value in randomValues) {
        sum += value
        Thread.sleep(1)
    }
    return sum
}

fun calculateSumWithThreadPool(randomValues: List<Int>): Long {
    val numberOfThreads = Runtime.getRuntime().availableProcessors()

    return Executors.newFixedThreadPool(numberOfThreads).use { pool ->
        var sum = 0L

        randomValues
            .chunked(randomValues.size / numberOfThreads)
            .map { chunk ->
                pool.submit(Callable {
                    calculateSumSequentially(chunk)
                })
            }
            .forEach {
                sum += it.get()
                Thread.sleep(1)
            }

        sum
    }
}

fun calculateSumWithForkJoin(randomValues: List<Int>): Long {

    class RecursiveSumTask(private val values: List<Int>) : RecursiveTask<Long>() {
        override fun compute(): Long {
            if (values.size <= 625) {
                return calculateSumSequentially(values)
            }
            val middle = values.size / 2
            val left = RecursiveSumTask(values.subList(0, middle))
            val right = RecursiveSumTask(values.subList(middle, values.size))
            left.fork()
            right.fork()
            Thread.sleep(1)
            return left.join() + right.join()
        }
    }

    val forkJoinPool = ForkJoinPool()
    return forkJoinPool.invoke(RecursiveSumTask(randomValues))
}

fun main() {
    val randomValues = List(10_000) { Random.nextInt() }

    val (sequentialSum, sequentialDuration) =  measureTimedValue {
        calculateSumSequentially(randomValues)
    }
    println("Sequential sum took $sequentialDuration")

    val (threadPoolSum, threadPoolDuration) = measureTimedValue {
        calculateSumWithThreadPool(randomValues)
    }
    println("ThreadPool sum took $threadPoolDuration")

    val (forkJoinSum, forkJoinDuration) = measureTimedValue {
        calculateSumWithForkJoin(randomValues)
    }
    println("ForkJoin sum took $forkJoinDuration")

    if (!(sequentialSum == threadPoolSum && threadPoolSum == forkJoinSum)) {
        throw IllegalStateException("Results are different")
    }
}