package com.example.petmate.model

data class User (
    val email: String? = "",
    val password: String?= "",
    val pet: Pet = Pet()
)
