package ru.alex0d.investmentservice.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthenticationToken(
    private val principal: UserPrincipal,
    private val authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {
    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = null
    override fun getPrincipal(): UserPrincipal = principal
}