package com.notmorron.serveronspring_01.controllers.authenticate

import com.notmorron.serveronspring_01.controllers.authenticate.models.AuthenticateData
import com.notmorron.serveronspring_01.dbs.mongo.collections.users.UserRepository
import com.notmorron.serveronspring_01.utils.JwtService
import com.notmorron.utils.PasswordCoder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticateController(
    val userRepository: UserRepository,
    val jwtService: JwtService
) {

    @PostMapping("/authenticate")
    fun authenticate(
        body: AuthenticateData
    ): ResponseEntity<String> {
        val user = userRepository.findByUsername(body.username) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val decodedPassword = PasswordCoder.decodeStringFS(user.password)

        return if (decodedPassword == body.password) {
            val token = jwtService.generateToken(user.idUser)
            ResponseEntity(token, HttpStatus.OK)
        } else
            ResponseEntity(HttpStatus.CONFLICT)
    }
}