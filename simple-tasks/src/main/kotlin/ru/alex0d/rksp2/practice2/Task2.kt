package ru.alex0d.rksp2.practice2

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.time.measureTime

private const val SOURCE_FILE = "sample-directory/source.txt"
private const val DESTINATION_FILE = "sample-directory/destination.txt"

fun createLargeFile(fileName: String, sizeInMb: Int) {
    val bytes = ByteArray(1024 * 1024)  // 1 MB

    FileOutputStream(fileName).use { fos ->
        repeat(sizeInMb) {
            fos.write(bytes)
        }
    }
}

fun copyUsingFileStreams(source: String, destination: String) {
    FileInputStream(source).use { fis ->
        FileOutputStream(destination).use { fos ->
            val buffer = ByteArray(1024)

            var bytesRead = fis.read(buffer)
            while (bytesRead != -1) {
                fos.write(buffer, 0, bytesRead)
                bytesRead = fis.read(buffer)
            }
        }
    }
}

fun copyUsingFileChannels(source: String, destination: String) {
    FileInputStream(source).use { fis ->
        val sourceChannel = fis.channel
        FileOutputStream(destination).use { fos ->
            val destinationChannel = fos.channel
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel)
        }
    }
}
fun copyUsingApacheCommonsIO(source: String, destination: String) {
    val sourceFile = File(source)
    val destinationFile = File(destination)
    FileUtils.copyFile(sourceFile, destinationFile)
}

fun copyUsingNIOFiles(source: String, destination: String) {
    val sourcePath = Paths.get(source)
    val destinationPath = Paths.get(destination)
    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
}

fun main() {
    createLargeFile(SOURCE_FILE, 100)

    measureTime {
        copyUsingFileStreams(SOURCE_FILE, DESTINATION_FILE)
    }.also {
        println("Copy using file streams took $it")
    }

    measureTime {
        copyUsingFileChannels(SOURCE_FILE, DESTINATION_FILE)
    }.also {
        println("Copy using file channels took $it")
    }

    measureTime {
        copyUsingApacheCommonsIO(SOURCE_FILE, DESTINATION_FILE)
    }.also {
        println("Copy using Apache Commons IO took $it")
    }

    measureTime {
        copyUsingNIOFiles(SOURCE_FILE, DESTINATION_FILE)
    }.also {
        println("Copy using NIO Files took $it")
    }

    File(SOURCE_FILE).delete()
    File(DESTINATION_FILE).delete()
}