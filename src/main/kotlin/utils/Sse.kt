package utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow

suspend fun ApplicationCall.respondSse(dataFlow: Flow<String>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondBytesWriter(
        contentType = ContentType.Text.EventStream
    ) {
        dataFlow.collect { data ->
            writeStringUtf8(data + "\n")
            flush()
        }
    }
}