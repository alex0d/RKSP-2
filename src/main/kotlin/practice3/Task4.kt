package practice3

import io.reactivex.rxjava3.core.BackpressureOverflowStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import practice1.File
import kotlin.random.Random

fun main() {

    val files = Flowable.generate { emitter ->
        val file = File(
            type = File.Type.entries.random(),
            size = Random.nextInt(10, 100)
        )
        emitter.onNext(file)
        Thread.sleep(Random.nextLong(100, 1000))
    }
        .onBackpressureBuffer(
            5,
            { println("Buffer overflow!") },
            BackpressureOverflowStrategy.DROP_LATEST
        )
        .subscribeOn(Schedulers.io())
        .publish()
        .autoConnect(3)

    files
        .observeOn(Schedulers.computation())
        .filter { it.type == File.Type.XML }
        .subscribe {
            Thread.sleep(it.size * 7L)
            println("Processed $it")
        }

    files
        .observeOn(Schedulers.computation())
        .filter { it.type == File.Type.JSON }
        .subscribe {
            Thread.sleep(it.size * 7L)
            println("Processed $it")
        }

    files
        .observeOn(Schedulers.computation())
        .filter { it.type == File.Type.XLSX }
        .subscribe {
            Thread.sleep(it.size * 7L)
            println("Processed $it")
        }

    Thread.sleep(300000)
}