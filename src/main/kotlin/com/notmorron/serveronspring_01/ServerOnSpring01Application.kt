package com.notmorron.serveronspring_01

import com.notmorron.serveronspring_01.dbs.mongo.collections.users.UserRepository
import com.notmorron.serveronspring_01.dbs.neo4j.*
import com.notmorron.serveronspring_01.utils.PK_ID_USER
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerOnSpring01Application(
    userRepository: UserRepository
) {
    init {
        val lastUser = userRepository.findLastByOrderByIdUser()
        PK_ID_USER.set(lastUser?.idUser ?: 0)
    }
}

fun main(args: Array<String>) {
    runApplication<ServerOnSpring01Application>(*args)
}