package ru.alex0d.investmentservice.auth

import java.security.Principal

data class UserPrincipal(
    val username: String,
    val tg: String?,
    val roles: List<String>
) : Principal {
    override fun getName(): String = username
}