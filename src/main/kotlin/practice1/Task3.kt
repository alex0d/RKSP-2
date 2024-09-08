package practice1

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

data class File(
    val type: Type,
    val size: Int
) {
    enum class Type {
        XML, JSON, XLSX
    }
}

class FileGenerator(
    private val queue: BlockingQueue<File>
) : Runnable {
    override fun run() {
        while (true) {
            Thread.sleep(Random.nextLong(100, 1000))
            val file = File(
                type = File.Type.entries.random(),
                size = Random.nextInt(10, 100)
            )
            queue.put(file)
        }
    }
}

class FileProcessor(
    private val queue: BlockingQueue<File>,
    private val type: File.Type
) : Runnable {
    override fun run() {
        while (true) {
            val file = queue.take().takeIf { it.type == type } ?: continue
            val processingTimeMs = file.size * 7L
            Thread.sleep(processingTimeMs)
            println("$file processed in $processingTimeMs ms")
        }
    }
}

fun main() {
    val queue = LinkedBlockingQueue<File>()

    val generator = FileGenerator(queue)
    val xmlProcessor = FileProcessor(queue, File.Type.XML)
    val jsonProcessor = FileProcessor(queue, File.Type.JSON)
    val xlsxProcessor = FileProcessor(queue, File.Type.XLSX)

    Thread(generator).start()
    Thread(xmlProcessor).start()
    Thread(jsonProcessor).start()
    Thread(xlsxProcessor).start()
}