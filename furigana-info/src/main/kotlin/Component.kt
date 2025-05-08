package io.github.constasj.mcp.furigana

import io.github.stream29.langchain4kt2.mcp.McpServer
import io.github.stream29.langchain4kt2.mcp.McpTool
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

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
            val document = client.get(redirectUrl).bodyAsText().let {
                Jsoup.parse(it)
            }
            return@runBlocking document.select("#main > div.sum > table > tbody").firstOrNull()
                ?.select("tr:not(.sum_head):not(.show_more) td:not(.right)").orEmpty().map {
                it.text()
            }.toList()
        } else {
            return@runBlocking listOf()
        }
    }
}