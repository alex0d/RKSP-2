package ru.alex0d.fsspring

import com.mongodb.client.gridfs.model.GridFSFile
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/multipart")
@CrossOrigin
class MultipartController(
    private val gridFsTemplate: ReactiveGridFsTemplate
) {

    @GetMapping
    fun getFilesList(): Flux<DownloadableFileDto> {
        println("Getting files list")
        return gridFsTemplate.find(query(Criteria()))
            .map { file: GridFSFile ->
                val id = file.id.asObjectId().value.toHexString()
                DownloadableFileDto(
                    id = id,
                    filename = file.filename,
                    sizeBytes = file.length
                )
            }
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart("files") fileParts: Flux<FilePart>): Flux<Map<String, String>> {
        return fileParts
            .flatMap { filePart -> gridFsTemplate.store(filePart.content(), filePart.filename()) }
            .map { mapOf("id" to it.toHexString()) }
            .doOnNext { println("File uploaded: $it") }
    }

    @GetMapping("{id}")
    fun read(@PathVariable id: String, exchange: ServerWebExchange): Mono<Void> {
        println("Reading file with id: $id")
        return gridFsTemplate.findOne(query(where("_id").isEqualTo(id)))
            .flatMap { file -> gridFsTemplate.getResource(file) }
            .flatMap { resource -> exchange.response.writeWith(resource.downloadStream) }
    }

    @GetMapping("/download/{id}")
    suspend fun download(@PathVariable id: String): ResponseEntity<Flux<DataBuffer>> {
        println("Downloading file with id: $id")
        return gridFsTemplate.findOne(query(where("_id").isEqualTo(id)))
            .flatMap { file: GridFSFile ->
                gridFsTemplate.getResource(file)
            }
            .map { r: ReactiveGridFsResource ->
                val encodedFilename = URLEncoder.encode(r.filename, StandardCharsets.UTF_8)
                    .replace("+", "%20")

                ok()
                    .header(
                        "Content-Disposition",
                        "attachment; filename*=UTF-8''$encodedFilename"
                    )
                    .body(r.downloadStream)
            }
            .awaitSingle()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: String): Mono<Void> {
        println("Deleting file with id: $id")
        return gridFsTemplate.delete(query(where("_id").isEqualTo(id)))
    }

}