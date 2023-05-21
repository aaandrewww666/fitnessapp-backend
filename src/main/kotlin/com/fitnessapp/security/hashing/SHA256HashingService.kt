package com.fitnessapp.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.exposedLogger
import java.security.SecureRandom

class SHA256HashingService : HashingService {
    //реализация интерфейса HashingService
    override fun generateSaltedHash(password: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength) //получение рандомного набора символов определённой длины
        val saltAsHex = Hex.encodeHexString(salt) //преобразование соли в Hex
        val hash = DigestUtils.sha256Hex(saltAsHex + password) //применение хэш-алгоритма sha256
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + password) == saltedHash.hash //проверка соответствия шифрования введённым данным
    }
}