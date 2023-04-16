package com.notmorron.serveronspring_01.dbs.neo4j.models

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Person(
    @Id
    val idUser: Long,
    var username: String,
    var isLocked: Boolean = false,
)
