package io.github.constasj.mcp.furigana

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.sse.*
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.client.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class FuriganaTest {
    private val server = getServer()

    private val serverScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val httpClient = HttpClient(CIO) {
        install(SSE)
    }

    private val mcpClient = Client(
        clientInfo = Implementation("FuriganaTest Client", "0.0.1"),
        options = ClientOptions(
            capabilities = ClientCapabilities()
        )
    )

    @BeforeTest
    fun init() {
        serverScope.launch {
            server.start(wait = true)
        }
        runBlocking {
            mcpClient.connect(
                SseClientTransport(
                    client = httpClient,
                    urlString = "http://localhost:8080/sse"
                )
            )
        }
    }

    @Test
    fun testGetFurigana() = runBlocking {
        assertTrue { mcpClient.listTools()!!.tools.asSequence().map { it.name }.contains("getFurigana") }
        val response = mcpClient.callTool("getFurigana", mapOf("kanji" to "道"))!!
            .content.first().let { it as TextContent }.text!!
        println("response: $response")
        val resBody = Json {}.decodeFromString<List<String>>(response)
        println("resBody: $resBody")
    }

//    @AfterTest
//    fun teardown() {
//        runBlocking {
//            server.stop(1_000, 5_000)
//            mcpClient.close()
//        }
//        serverScope.cancel()
//    }
}