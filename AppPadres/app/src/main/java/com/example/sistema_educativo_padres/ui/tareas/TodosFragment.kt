package com.example.sistema_educativo_padres.ui.tareas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.adapters.CursosAdapter
import com.example.sistema_educativo_padres.data.CursoConTarea
import com.example.sistema_educativo_padres.data.Tarea
import com.example.sistema_educativo_padres.sec.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class TodosFragment : Fragment() {
    private val client = OkHttpClient()

    private lateinit var recyclerTodos: RecyclerView
    private lateinit var adapter: CursosAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todos, container, false)

        recyclerTodos = view.findViewById(R.id.recyclerTodos)
        recyclerTodos.layoutManager = LinearLayoutManager(requireContext())

        val alummoId = arguments?.getString("alumnoId")
        if (alummoId != null) {
            cargarCursos(alummoId)
        }

        return view
    }

    private fun cargarCursos(alumnoId: String) {
        lifecycleScope.launch {
            getCursosConTareas(alumnoId)
        }
    }

    private fun actualizarCursos(cursos: List<CursoConTarea>) {
        if (::adapter.isInitialized) {
            adapter.actualizarDatos(cursos)
        }else {
            adapter = CursosAdapter(cursos.toMutableList())
            recyclerTodos.adapter = adapter
        }
    }

    private suspend fun getCursosConTareas(alumnoId: String) {
        val token = TokenManager.obtenerToken(requireContext())

        val urlCursos = "http://192.168.0.10:8080/escuelaAlumnosCursos/api/alumnosCursos/alumnos/$alumnoId"
        val cursos = mutableListOf<Int>()
        val cursosAlumno = mutableListOf<Int>()

        var nombre = ""
        val tareas = mutableListOf<Tarea>()
        val cursosTareas = mutableListOf<CursoConTarea>()

        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(urlCursos).header("Authorization", "Bearer $token").get().build()
                val response = client.newCall(request).execute()
                response.use {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if(!responseBody.isNullOrEmpty()) {
                            val jsonArray = JSONArray(responseBody)
                            for (i in 0 until jsonArray.length()) {
                                val jsonCurso = jsonArray.getJSONObject(i)
                                val cursoId = jsonCurso.getInt("id")
                                val curso = jsonCurso.getInt("idCurso")
                                cursosAlumno.add(cursoId)
                                cursos.add(curso)
                            }
                        }
                    }
                }
            }catch (e: Exception) {
                Log.e("Error cursos", "Exception: ${e.message}")
            }
        }

        for (curso in cursos) {
            val urlCurso = "http://192.168.0.10:8080/escuelaCursos/api/cursos/$curso"

            withContext(Dispatchers.IO) {
                try {
                    val request = Request.Builder().url(urlCurso).header("Authorization", "Bearer $token").get().build()
                    val response = client.newCall(request).execute()
                    response.use {
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            if(!responseBody.isNullOrEmpty()) {
                                val jsonCurso = JSONObject(responseBody)
                                val nombreCurso = jsonCurso.getString("nombre")
                                nombre = nombreCurso
                            }
                        }
                    }
                }catch (e: Exception) {
                    Log.e("Error cursos", "Exception: ${e.message}")
                }
            }

            for (cursoId in cursosAlumno) {
                val urlTareas = "http://192.168.0.10:8080/escuelaTareas/api/tareas/curso/$cursoId"

                withContext(Dispatchers.IO) {
                    try {
                        val request = Request.Builder().url(urlTareas).header("Authorization", "Bearer $token").get().build()
                        val response = client.newCall(request).execute()
                        response.use {
                            if (response.isSuccessful) {
                                val responseBody = response.body?.string()
                                if(!responseBody.isNullOrEmpty()) {
                                    val jsonArray = JSONArray(responseBody)
                                    for (i in 0 until jsonArray.length()) {
                                        val jsonTarea = jsonArray.getJSONObject(i)
                                        val tarea = Tarea(
                                            id = jsonTarea.getInt("id"),
                                            titulo = jsonTarea.getString("nombre"),
                                            calificacion = jsonTarea.getDouble("calificacion").toFloat(),
                                            fechaEntrega = jsonTarea.optLong("duedate", 0L),
                                            avalada = jsonTarea.getInt("avaladoPadre"),
                                            curso = cursoId
                                        )
                                        tareas.add(tarea)
                                    }
                                }else {
                                    Log.e("Error tareas", response.message)
                                }
                            }
                        }
                    }catch (e: Exception) {
                        Log.e("Error tareas", "Exception: ${e.message}")
                    }
                }
            }

            cursosTareas.add(CursoConTarea(curso, nombre, tareas, false))
            Log.w("Cursos agregados", cursosTareas.toString())
        }

        actualizarCursos(cursosTareas)
    }
}