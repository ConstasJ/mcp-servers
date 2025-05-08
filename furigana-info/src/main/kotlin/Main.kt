package io.github.constasj.mcp.furigana

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main(args: Array<String>) {
    getServer().start(wait = true)
}

fun getServer() =
    embeddedServer(
        factory = CIO,
        port = 8080,
        module = Application::module
    )

fun Application.module() {

}