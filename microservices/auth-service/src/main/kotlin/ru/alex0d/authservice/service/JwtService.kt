package ru.alex0d.authservice.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.alex0d.authservice.model.User
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(
        Base64.getEncoder().encode(jwtSecret.toByteArray())
    )
    private val validityInMs = 3600000L // 1 час

    fun generateToken(user: User): String {
        val now = Date()
        val validity = Date(now.time + validityInMs)

        return Jwts.builder()
            .setSubject(user.username)
            .claim("roles", user.roles)
            .claim("tg", user.tg)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey)
            .compact()
    }
}