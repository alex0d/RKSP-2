package ru.alex0d.rksp2.practice3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.concurrent.thread
import kotlin.random.Random

sealed class SensorData {
    data class Temperature(val value: Int) : SensorData()
    data class CO2(val value: Int) : SensorData()
}

class TemperatureSensor : Observable<SensorData>() {
    private val subject = PublishSubject.create<SensorData>()

    override fun subscribeActual(observer: Observer<in SensorData>) {
        subject.subscribe(observer)
    }

    fun start() {
        thread {
            while (true) {
                val temperature = Random.nextInt(15, 31)
                subject.onNext(SensorData.Temperature(temperature))
                Thread.sleep(1000)
            }
        }
    }
}

class CO2Sensor : Observable<SensorData>() {
    private val subject = PublishSubject.create<SensorData>()

    override fun subscribeActual(observer: Observer<in SensorData>) {
        subject.subscribe(observer)
    }

    fun start() {
        thread {
            while (true) {
                val co2 = Random.nextInt(30, 101)
                subject.onNext(SensorData.CO2(co2))
                Thread.sleep(1000)
            }
        }
    }
}

class Alarm : Observer<SensorData> {
    private val co2Limit = 70
    private val tempLimit = 25

    private var temperature = 0
    private var co2 = 0

    override fun onSubscribe(d: Disposable) {
        println(d.hashCode().toString() + " has subscribed")
    }

    override fun onNext(sensorData: SensorData) {
        println("Next value from Observable = $sensorData")
        when (sensorData) {
            is SensorData.Temperature -> {
                temperature = sensorData.value
            }
            is SensorData.CO2 -> {
                co2 = sensorData.value
            }
        }
        checkAndAlert()
    }

    private fun checkAndAlert() {
        if (temperature >= tempLimit && co2 >= co2Limit) {
            println("ALARM!!! Temperature = $temperature, CO2 = $co2")
        } else if (temperature >= tempLimit) {
            println("Warning: temperature = $temperature")
        } else if (co2 >= co2Limit) {
            println("Warning: CO2 = $co2")
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {
        println("Completed")
    }
}

fun main() {
    val temperatureSensor = TemperatureSensor()
    val co2Sensor = CO2Sensor()
    val alarm = Alarm()

    temperatureSensor.subscribe(alarm)
    co2Sensor.subscribe(alarm)

    temperatureSensor.start()
    co2Sensor.start()
}