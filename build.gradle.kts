plugins {
    kotlin("jvm") version "2.0.10"
}

group = "ru.alex0d"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")

    // Commons IO for copying files
    implementation("commons-io:commons-io:2.16.1")

    // RxJava 3
    implementation("io.reactivex.rxjava3:rxjava:3.1.9")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}