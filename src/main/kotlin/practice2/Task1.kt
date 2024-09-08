package practice2

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun writeToFile(fileName: String, lines: List<String>) {
    val filePath = Paths.get(fileName)
    try {
        Files.write(filePath, lines)
        println("The file $fileName has been written successfully.")
    } catch (e: IOException) {
        println("An error occurred: ${e.message}")
    }
}

fun readFromFile(fileName: String): List<String>? {
    val filePath = Paths.get(fileName)
    return try {
        Files.readAllLines(filePath)
    } catch (e: IOException) {
        println("An error occurred: ${e.message}")
        null
    }
}

fun main() {
    val fileName = "sample-directory/sample.txt"
    val lines = listOf(
        "Hello, world!",
        "This is a sample file.",
        "Goodbye!",
    )

    writeToFile(fileName, lines)
    readFromFile(fileName)?.forEach(::println)

}