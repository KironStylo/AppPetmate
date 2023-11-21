package com.example.petmate.model

data class User (
    var email: String? = "",
    var password: String?= "",
    var ciudad: String?="",
    var pet: Pet = Pet()
)
