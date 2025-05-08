package io.github.constasj.mcp.furigana

import io.github.constasj.mcp.utils.MockTransport
import io.modelcontextprotocol.kotlin.sdk.ClientCapabilities
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.JSONRPCMessage
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.ClientOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import kotlin.test.*

class FuriganaTest {
    private val server = getMcpServer()

    private val fromServerToClient = Channel<JSONRPCMessage>()
    private val fromClientToServer = Channel<JSONRPCMessage>()

    private val client = Client(
        clientInfo = Implementation("FuriganaTest Client", "0.0.1"), options = ClientOptions(
            capabilities = ClientCapabilities()
        )
    )

    @BeforeTest
    fun init() = runBlocking {
        server.connect(MockTransport(fromClientToServer, fromServerToClient))
        client.connect(MockTransport(fromServerToClient, fromClientToServer))
    }

    @Test
    fun testGetFurigana() = runBlocking {
        assertTrue { client.listTools()!!.tools.asSequence().map { it.name }.contains("getFurigana") }
        val response =
            client.callTool("getFurigana", mapOf("value" to "道"))!!.content.first().let { it as TextContent }.text!!
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
    fun teardown() = runBlocking {
        server.close()
        client.close()
    }
}