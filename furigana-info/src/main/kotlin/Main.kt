package io.github.constasj.mcp.furigana

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcp

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
    mcp {
        Server(
            serverInfo = Implementation(
                name = "Furigana Info",
                version = "1.0.0"
            ),
            options = ServerOptions(
                capabilities = ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = true)
                )
            )
        ).apply {
            val component = FuriganaComponent()
            addTools(component.tools())
        }
    }
}