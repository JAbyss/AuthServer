package com.notmorron.serveronspring_01.controllers.registrations

import com.notmorron.serveronspring_01.controllers.registrations.models.CodeAndEmail
import com.notmorron.serveronspring_01.controllers.registrations.models.PasswordAndEmail
import com.notmorron.serveronspring_01.controllers.registrations.models.RegistrationData
import com.notmorron.serveronspring_01.dbs.mongo.collections.codes.CodeRepository
import com.notmorron.serveronspring_01.dbs.mongo.collections.codes.models.VerifyCodeEntity
import com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers.TempSingUpRepository
import com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers.models.TempSingUp
import com.notmorron.serveronspring_01.dbs.mongo.collections.tempusers.setVerifyUser
import com.notmorron.serveronspring_01.dbs.mongo.collections.users.UserRepository
import com.notmorron.serveronspring_01.dbs.mongo.collections.users.checkOnExistUser
import com.notmorron.serveronspring_01.dbs.mongo.collections.users.models.UsersEntity
import com.notmorron.serveronspring_01.dbs.neo4j.PersonRep
import com.notmorron.serveronspring_01.utils.*
import com.notmorron.utils.PasswordCoder
import com.notmorron.utils.generateUUID
import io.ktor.util.date.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class RegistrationController(
    val userRepository: UserRepository,
    val verifyCodeRepository: CodeRepository,
    val tempSingUpRepository: TempSingUpRepository,
    val neoRepository: PersonRep,
    val jwtService: JwtService
) {


    @PostMapping("/registrationGenerateCode")
    suspend fun GenerateSignUpCode(
        @RequestBody data: RegistrationData
    ): ResponseEntity<Nothing> {

        val isUserExist = userRepository.checkOnExistUser(data)

        val codeConfirmation = generateUUID(4)

        if (!verifyCodeRepository.existsById(data.email)) {
            verifyCodeRepository.save(VerifyCodeEntity(id = data.email, code = codeConfirmation))

            //Отправляю код по почте
            val result = EmailSender.sendCode(verifyCode = EmailSender.EmailAndDataEntity(email = data.email, data = codeConfirmation))

            result.onSuccess {
                tempSingUpRepository.save(TempSingUp(email = data.email, username = data.username))

                NewTaskManager.addTaskToQueue(
                    NewTaskManager.Task(
                        time = 15.m
                    ) {
                        tempSingUpRepository.deleteById(data.email)
                        verifyCodeRepository.deleteById(data.email)
                    })
                return ResponseEntity(HttpStatus.CREATED)
            }.onFailure {
                verifyCodeRepository.deleteById(data.email)
                return ResponseEntity(HttpStatus.CONFLICT)
            }
        } else
            return ResponseEntity(HttpStatus.CONFLICT)
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping("/confirmationSignUpCode")
    fun ConfirmationSignUpCode(
        @RequestBody codeAndEmail: CodeAndEmail
    ): ResponseEntity<Nothing> {

        val code = verifyCodeRepository.findByIdOrNull(codeAndEmail.email)?.code ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return if (code == codeAndEmail.code){

            verifyCodeRepository.deleteById(codeAndEmail.email)
            tempSingUpRepository.setVerifyUser(codeAndEmail.email, true)
            ResponseEntity(HttpStatus.ACCEPTED)
        } else
            ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/finalSignUpCode")
    fun FinalSignUpCode(
        @RequestBody passwordAndEmail: PasswordAndEmail
    ): ResponseEntity<String> {

        val user = tempSingUpRepository.findByIdOrNull(passwordAndEmail.email) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return if (user.status){
            val id = PK_ID_USER.getAndAdd(1)

            val user = UsersEntity(
                idUser = id,
                username = user.username,
                dateRegistration = Date(getTimeMillis()).time.toString(),
                password = PasswordCoder.encodeStringFS(passwordAndEmail.password),
                email = passwordAndEmail.email
            )
            // Создание пользователя в главной бд Авторизации
            userRepository.save(user)
            // Создание пользователя в бд Neo4j
            neoRepository.save(user.toPerson())

            val token = jwtService.generateToken(id)
            ResponseEntity(token, HttpStatus.CREATED)
        } else
            ResponseEntity(HttpStatus.CONFLICT)
    }
}