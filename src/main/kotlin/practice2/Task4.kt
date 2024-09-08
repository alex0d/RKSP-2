package practice2

import java.nio.file.*
import java.security.DigestInputStream
import java.security.MessageDigest

private val fileContentsMap: MutableMap<Path, List<String>> = HashMap()
private val fileHashes: MutableMap<Path, String> = HashMap()

fun firstObserve(directory: Path) {
    Files.newDirectoryStream(directory).use { stream ->
        for (filePath in stream) {
            if (Files.isRegularFile(filePath)) {
                fileContentsMap[filePath] = readLinesFromFile(filePath)
                calculateFileHash(filePath)
            }
        }
    }
}

fun printContentChanges(oldFileContent: List<String>, newFileContent: List<String>) {
    val addedLines = newFileContent
        .filter { line -> !oldFileContent.contains(line) }

    val deletedLines = oldFileContent
        .filter { line -> !newFileContent.contains(line) }

    if (addedLines.isNotEmpty()) {
        println("Lines added to file:")
        addedLines.forEach { println("+ $it") }
    }
    if (deletedLines.isNotEmpty()) {
        println("Lines deleted from file:")
        deletedLines.forEach { println("- $it") }
    }
}

fun readLinesFromFile(filePath: Path): List<String> {
    val lines: MutableList<String> = ArrayList()
    Files.newBufferedReader(filePath).use { reader ->
        var line = reader.readLine()
        while (line != null) {
            lines.add(line)
            line = reader.readLine()
        }
    }
    return lines
}

fun calculateFileHash(filePath: Path) {
    val md = MessageDigest.getInstance("MD5")
    Files.newInputStream(filePath).use { `is` ->
        DigestInputStream(`is`, md).use { dis ->
            while (dis.read() != -1) { }
            val hash = md.digest().toHex()
            fileHashes.put(filePath, hash)
        }
    }
}

fun ByteArray.toHex(): String = buildString {
    for (b in this@toHex) {
        append(String.format("%02x", b))
    }
}

fun main() {
    val directory = Paths.get("sample-directory")

    val watchService = FileSystems.getDefault().newWatchService()
    directory.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_DELETE
    )

    firstObserve(directory)

    while (true) {
        val key = watchService.take()
        for (event in key.pollEvents()) {
            val kind = event.kind()
            val filePath = event.context() as Path

            when (kind) {
                StandardWatchEventKinds.ENTRY_CREATE -> {
                    println("New file created: $filePath")
                    fileContentsMap[filePath] = readLinesFromFile(directory.resolve(filePath))
                    calculateFileHash(directory.resolve(filePath))
                }

                StandardWatchEventKinds.ENTRY_MODIFY -> {
                    println("File modified: $filePath")
                    val newContent = readLinesFromFile(directory.resolve(filePath))
                    fileContentsMap[filePath]?.also {
                        printContentChanges(it, newContent)
                    }
                    fileContentsMap[filePath] = newContent
                    calculateFileHash(directory.resolve(filePath))
                }

                StandardWatchEventKinds.ENTRY_DELETE -> {
                    println("File deleted: $filePath")
                    fileHashes[directory.resolve(filePath)]?.also {
                        println("Deleted file hash sum: $it")
                    }
                }
            }
        }
        key.reset()
    }
}

