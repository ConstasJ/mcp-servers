package io.github.constasj.mcp.furigana

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.langchain4kt2.mcp.McpServer
import io.github.stream29.langchain4kt2.mcp.McpTool
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.Jsoup

@McpServer
class FuriganaComponent {
    val client = HttpClient(CIO) {
        followRedirects = false
    }

    @McpTool(description = "Get the potential furigana list of a kanji")
    suspend fun getFurigana(@Description("The Kanji to search for") kanji: String): List<String> {
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
            return document.select("#main > div.sum > table > tbody").firstOrNull()
                ?.select("tr:not(.sum_head):not(.show_more) td:not(.right)").orEmpty().map {
                    it.text()
                }.toList()
        } else {
            return listOf()
        }
    }
}