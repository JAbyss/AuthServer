package com.notmorron.serveronspring_01.dbs.mongo.collections.codes.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "verifyCodes")
@JsonIgnoreProperties(ignoreUnknown = true)
data class VerifyCodeEntity(
    @Id
    val id: String,
    val code: String
)