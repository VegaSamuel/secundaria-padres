package com.example.sistema_educativo_padres.ui.tareas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistema_educativo_padres.data.Tarea

class PendientesViewModel: ViewModel() {
    val tareasPendientes = MutableLiveData<List<Tarea>>()
}