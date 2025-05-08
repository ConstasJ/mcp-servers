package io.github.constasj.mcp.furigana

import io.github.stream29.langchain4kt2.mcp.McpServer
import io.github.stream29.langchain4kt2.mcp.McpTool
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

@McpServer
class FuriganaComponent {
    val client = HttpClient(CIO) {
        followRedirects = false
    }
    @McpTool
    suspend fun getFurigana(kanji: String): List<String> = runBlocking {
        val response = client.get("https://furigana.info/search") {
            url {
                parameters.append("q", kanji)
            }
        }
        if (response.status.value == 302) {
            val redirectUrl = response.headers["Location"]!!
            val furiganaHtmlString = client.get(redirectUrl).bodyAsText()
            return@runBlocking listOf(furiganaHtmlString)
        } else {
            return@runBlocking listOf()
        }
    }
}