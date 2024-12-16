package ru.alex0d.authservice.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alex0d.authservice.dto.AuthResponse
import ru.alex0d.authservice.dto.LoginRequest
import ru.alex0d.authservice.dto.RegisterRequest
import ru.alex0d.authservice.model.User
import ru.alex0d.authservice.repository.UserRepository

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            tg = request.tg,
            roles = setOf("ROLE_USER")
        )

        userRepository.save(user)
        return AuthResponse(jwtService.generateToken(user))
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("User not found")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        return AuthResponse(jwtService.generateToken(user))
    }
}