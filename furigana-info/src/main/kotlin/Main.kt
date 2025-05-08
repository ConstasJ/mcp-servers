package io.github.constasj.mcp.furigana

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered

fun main(args: Array<String>) {
    if (args.contains("--sse")) {
        if (args.contains("--port")) {
            // Running in SSE mode with custom port
            val port = args[args.indexOf("--port") + 1].toInt()
            getSSEServer(port).start(wait = true)
            return
        }
        getSSEServer().start(wait = true)
        return
    } else {
        // Running in STDIO mode
        runBlocking {
            val server = getMcpServer()
            val transport = StdioServerTransport(
                inputStream = System.`in`.asSource().buffered(),
                outputStream = System.out.asSink().buffered()
            )
            server.connect(transport)
        }
    }
}

fun getSSEServer(port: Int = 8080) =
    embeddedServer(
        factory = CIO,
        port = 8080,
        module = Application::module
    )

fun getMcpServer() = Server(
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

fun Application.module() {
    mcp(::getMcpServer)
}