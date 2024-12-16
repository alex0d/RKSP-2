package ru.alex0d.investmentservice.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.alex0d.investmentservice.service.JwtService

@Component
class JwtAuthFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substring(7)
            val username = jwtService.extractUsername(jwt)
            val tg = jwtService.extractTelegramId(jwt)
            val roles = jwtService.extractRoles(jwt)

            if (username != null) {
                val principal = UserPrincipal(username, tg, roles)
                val authorities = roles.map { SimpleGrantedAuthority(it) }

                val authentication = UserAuthenticationToken(principal, authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}