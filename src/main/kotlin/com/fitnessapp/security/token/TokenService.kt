package com.fitnessapp.security.token

interface TokenService {
    //интерфейс для генерации токена
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ) : String
}