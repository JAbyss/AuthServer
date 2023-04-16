package com.notmorron.serveronspring_01.controllers.verifyToken

import com.notmorron.serveronspring_01.utils.JwtService
import com.notmorron.serveronspring_01.utils.idUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class VerifyTokenController(
    private val jwtService: JwtService
) {

    @GetMapping("/verifyToken")
    fun verifyToken(
        @RequestHeader(name = "Authorization") authHeader: String
    ): ResponseEntity<Long> {

        val token = authHeader.drop(7)
        val result = jwtService.validateToken(token)
        result.onSuccess {
            return ResponseEntity(it.idUser, HttpStatus.OK)
        }.onFailure {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping("/createTokenTest")
    fun createToken(): String {
        return jwtService.generateToken(121)
    }
}