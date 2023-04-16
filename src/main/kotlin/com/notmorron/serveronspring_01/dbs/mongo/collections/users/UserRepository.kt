package com.notmorron.serveronspring_01.dbs.mongo.collections.users

import com.notmorron.serveronspring_01.controllers.registrations.models.RegistrationData
import com.notmorron.serveronspring_01.dbs.mongo.collections.users.models.UsersEntity
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories
interface UserRepository : MongoRepository<UsersEntity, Long> {
    fun findByUsername(username: String): UsersEntity?
    fun findByEmail(email: String): UsersEntity?
    fun findLastByOrderByIdUser(): UsersEntity?
}

//fun UserRepository.getLastUser(){
//    val query = Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1)
//    val a = findBy<UsersEntity>(query)
//}

fun UserRepository.checkOnExistUser(data: RegistrationData): Boolean {
    return findByEmail(data.email) != null || findByUsername (data.username) != null
}