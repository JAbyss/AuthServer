package com.notmorron.serveronspring_01.dbs.mongo.collections.users.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.notmorron.serveronspring_01.dbs.neo4j.models.Person
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
data class UsersEntity(
    @Id
    val idUser: Long,
    val username: String,
    val dateRegistration: String,
    val password: String,
    val email: String
){
    fun toPerson() = Person(
        idUser = idUser,
        username = username
    )
}