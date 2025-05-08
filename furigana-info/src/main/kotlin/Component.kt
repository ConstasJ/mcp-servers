package io.github.constasj.mcp.furigana

import io.github.stream29.langchain4kt2.mcp.McpServer
import io.github.stream29.langchain4kt2.mcp.McpTool

@McpServer
class FuriganaComponent {
    @McpTool
    suspend fun getFurigana(kanji: String): List<String> {
        return listOf(kanji)
    }
}