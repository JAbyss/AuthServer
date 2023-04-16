package com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tempSignUp")
@JsonIgnoreProperties(ignoreUnknown = true)
data class TempSingUp(
    @Id
    val email: String,
    val username: String,
    var status: Boolean = false
)