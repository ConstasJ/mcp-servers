package io.github.constasj.mcp.utils

import io.modelcontextprotocol.kotlin.sdk.JSONRPCMessage
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class MockTransport(
    private val inputChannel: ReceiveChannel<JSONRPCMessage>,
    private val outputChannel: SendChannel<JSONRPCMessage>,
) : Transport {

    @Volatile
    private var onClose: (() -> Unit)? = null

    @Volatile
    private var onError: ((Throwable) -> Unit)? = null

    @Volatile
    private var onMessage: (suspend (JSONRPCMessage) -> Unit)? = null

    @Volatile
    private var listeningJob: Job? = null

    override suspend fun close() {
        onClose?.invoke()
        listeningJob?.cancel()
    }

    override fun onClose(block: () -> Unit) {
        onClose = block
    }

    override fun onError(block: (Throwable) -> Unit) {
        onError = block
    }

    override fun onMessage(block: suspend (JSONRPCMessage) -> Unit) {
        onMessage = block
    }

    override suspend fun send(message: JSONRPCMessage) {
        outputChannel.send(message)
    }

    override suspend fun start() {
        listeningJob = CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            for (msg in inputChannel) {
                try {
                    onMessage?.invoke(msg)
                } catch (e: Throwable) {
                    break
                }
            }
        }
    }
}