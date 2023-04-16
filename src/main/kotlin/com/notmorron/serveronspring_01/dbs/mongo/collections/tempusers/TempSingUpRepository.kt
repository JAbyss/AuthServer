package com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers

import com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers.models.TempSingUp
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories
interface TempSingUpRepository: MongoRepository<TempSingUp, String>

fun TempSingUpRepository.setVerifyUser(email: String, status: Boolean){
    val document = findById(email)
    if (document.isPresent){
        val note = document.get()
        note.status = status
        save(note)
    }
}