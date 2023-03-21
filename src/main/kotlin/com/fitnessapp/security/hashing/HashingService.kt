package com.fitnessapp.security.hashing

interface HashingService {
    fun generateSaltedHash(password: String, saltLength: Int = 16) : SaltedHash
    fun verify(password: String, saltedHash: SaltedHash) : Boolean
}