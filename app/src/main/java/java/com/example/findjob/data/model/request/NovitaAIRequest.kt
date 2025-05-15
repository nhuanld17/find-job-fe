package com.example.findjob.data.model.request

data class NovitaAIRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int?
) {
    data class Message(
        val role: String,
        val content: String
    )
}
