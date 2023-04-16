package com.notmorron.serveronspring_01.utils

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

object EmailSender {
    val rawUrl = "http://192.168.0.217"
    val port = 29021

    val baseUrl = "$rawUrl:$port"


    val client
        get() = WebClient.create("$baseUrl/sendVerifyCode")

    suspend fun sendCode(verifyCode: EmailAndDataEntity) = runCatching {
         client.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(verifyCode))
            .retrieve()
            .awaitBodilessEntity().statusCode
    }

    data class EmailAndDataEntity(
        val email: String,
        val data: String
    )
}

