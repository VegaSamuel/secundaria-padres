package com.example.sistema_educativo_padres.data

data class CursoConTarea(
    val id: Int,
    val nombre: String,
    val tareas: List<Tarea>,
    val isExpanded: Boolean
)
