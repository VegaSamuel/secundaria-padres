package com.example.sistema_educativo_padres.data

data class Alumno(
    val nombre: String,
    val apellido: String,
    val email: String,
    var padre: Int
)
