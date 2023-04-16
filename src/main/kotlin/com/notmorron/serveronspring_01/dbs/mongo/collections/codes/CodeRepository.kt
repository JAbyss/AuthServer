package com.notmorron.serveronspring_01.dbs.mongo.collections.codes

import com.notmorron.serveronspring_01.dbs.mongo.collections.codes.models.VerifyCodeEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.repository.findByIdOrNull

@EnableMongoRepositories
interface CodeRepository : MongoRepository<VerifyCodeEntity, String>

fun CodeRepository.isExistCode(email: String): Boolean {
    return findByIdOrNull(email) != null
}