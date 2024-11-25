package com.example.sistema_educativo_padres.ui.tareas

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.adapters.CursosAdapter
import com.example.sistema_educativo_padres.adapters.TareasAdapter
import com.example.sistema_educativo_padres.data.Tarea

class PendientesFragment : Fragment() {

    private lateinit var recyclerPendientes: RecyclerView
    private lateinit var adapter: TareasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pendientes, container, false)

        recyclerPendientes = view.findViewById(R.id.recyclerPendientes)
        recyclerPendientes.layoutManager = LinearLayoutManager(requireContext())

        val alumnoId = arguments?.getString("alumnoId")
        if (alumnoId != null) {
            cargarPendientes(alumnoId)
        }

        return view
    }

    private fun cargarPendientes(alumnoId: String) {
        val tareasPendientes = getTareasPendientes(alumnoId)

        adapter = TareasAdapter(tareasPendientes)
        recyclerPendientes.adapter = adapter
    }

    private fun getTareasPendientes(alumnoId: String): List<Tarea> {
        return listOf()
    }
}