package ru.alex0d.investmentservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(
        Base64.getEncoder().encode(jwtSecret.toByteArray())
    )

    fun extractUsername(token: String): String? {
        return try {
            val claims = extractAllClaims(token)
            print(claims)
            claims.subject.toString()
        } catch (e: Exception) {
            null
        }
    }

    fun extractTelegramId(token: String): String? {
        return try {
            extractAllClaims(token).get("tg", String::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun extractRoles(token: String): List<String> {
        return try {
            extractAllClaims(token).get("roles", List::class.java) as List<String>
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}