package com.fitnessapp.domain.usecase

class ValidateLogin {
    companion object {

        private val EMAIL_REGEX = Regex(
            "[a-zA-Z\\d+._%\\-]{1,20}" +
                    "@" +
                    "[a-zA-Z\\d][a-zA-Z\\d\\-]{0,15}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z\\d][a-zA-Z\\d\\-]{0,4}" +
                    ")+")
    }

    operator fun invoke(email: String): Boolean {
        return (email.matches(EMAIL_REGEX))
    }
}