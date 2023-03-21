package com.fitnessapp.security.token

import com.fitnessapp.security.token.TokenClaim
import com.fitnessapp.security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ) : String
}