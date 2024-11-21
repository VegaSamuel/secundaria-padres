package com.example.sistema_educativo_padres.ui.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TareasViewModel() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "No hay alumnos que visualizar\nUse el botón para agregar a su hijo y poder ver sus tareas"
    }
    val text: LiveData<String> = _text
}