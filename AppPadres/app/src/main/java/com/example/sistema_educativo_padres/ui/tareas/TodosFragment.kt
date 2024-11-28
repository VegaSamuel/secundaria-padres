package com.example.sistema_educativo_padres.ui.tareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.adapters.CursosAdapter
import com.example.sistema_educativo_padres.data.CursoConTarea
import com.example.sistema_educativo_padres.data.Tarea

class TodosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CursosAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todos, container, false)
        recyclerView = view.findViewById(R.id.tareasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val cursos = listOf(
            CursoConTarea(1, "Matemáticas", listOf(Tarea(1, "Tarea1", 0, 0.0.toFloat(), 0, 1)), false)
        )

        adapter = CursosAdapter(cursos)
        recyclerView.adapter = adapter

        return view
    }
}