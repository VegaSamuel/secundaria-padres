package com.example.sistema_educativo_padres.data

data class Tarea(
    val id: Int,
    val titulo: String,
    val fechaEntrega: String,
    val calificacion: Float,
    val avalada: Int,
    val curso: Int
)
