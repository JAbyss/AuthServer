package com.notmorron.serveronspring_01.utils

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

private const val idUserClaim = "idUser"

val Jws<Claims>.idUser
    get() =
        this.body[idUserClaim].toString().toLong()

@Service
class JwtService {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    private val jwtParser: JwtParser =
        Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()

    fun validateToken(token: String): Result<Jws<Claims>> = runCatching {
        jwtParser.parseClaimsJws(token)
    }

    fun generateToken(idUser: Long): String {

        val now = Date()
        val expireDate = Date(now.time + 10.m)


        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(mapOf(idUserClaim to idUser))
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(key)
            .compact()
    }
}