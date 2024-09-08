package practice2

import java.io.FileInputStream
import java.nio.ByteBuffer
import kotlin.experimental.xor

fun calculateCheckSum(filePath: String): Short {
    FileInputStream(filePath).use { fileInputStream ->
        val fileChannel = fileInputStream.channel
        val buffer: ByteBuffer = ByteBuffer.allocate(2)

        var checksum: Short = 0
        while (fileChannel.read(buffer) != -1) {
            buffer.flip()  // switch buffer to read mode
            while (buffer.hasRemaining()) {
                checksum = checksum xor buffer.get().toShort()
            }
            buffer.clear()  // switch buffer to write mode
        }
        return checksum
    }
}

fun main() {
    val filePath = "sample-directory/sample.txt"
    println("Checksum: ${calculateCheckSum(filePath)}")
}

