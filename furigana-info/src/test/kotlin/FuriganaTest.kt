package io.github.constasj.mcp.furigana

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.sse.*
import io.modelcontextprotocol.kotlin.sdk.ClientCapabilities
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.ClientOptions
import io.modelcontextprotocol.kotlin.sdk.client.SseClientTransport
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class FuriganaTest {
    private val server = getServer()

    private val httpClient = HttpClient(CIO) {
        install(SSE)
    }

    private val mcpClient = Client(
        clientInfo = Implementation("FuriganaTest Client", "0.0.1"), options = ClientOptions(
            capabilities = ClientCapabilities()
        )
    )

    @BeforeTest
    fun init() {
        server.start(wait = false)
        runBlocking {
            mcpClient.connect(
                SseClientTransport(
                    client = httpClient, urlString = "http://localhost:8080"
                )
            )
        }
    }

    @Test
    fun testGetFurigana() = runBlocking {
        assertTrue { mcpClient.listTools()!!.tools.asSequence().map { it.name }.contains("getFurigana") }
        val response =
            mcpClient.callTool("getFurigana", mapOf("value" to "道"))!!.content.first().let { it as TextContent }.text!!
                .let { Json.decodeFromString<List<String>>(it) }
        assertContentEquals(
            listOf(
                "みち",
                "どう",
                "い",
                "だう",
                "ことば",
                "みつ",
                "ぢ",
                "みっ",
                "よ",
                "みい",
                "おも",
                "ち",
                "どおり",
                "みちび",
                "ヂ",
                "ドー"
            ), response
        )
    }

    @AfterTest
    fun teardown() {
        server.stop(1_000, 5_000)
        runBlocking {
            mcpClient.close()
        }
    }
}