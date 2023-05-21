package com.fitnessapp.security.hashing

interface HashingService {
    //интерфейс для генерации и проверки солёного хэша пароля
    fun generateSaltedHash(password: String, saltLength: Int = 16) : SaltedHash
    fun verify(password: String, saltedHash: SaltedHash) : Boolean
}