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
import com.example.sistema_educativo_padres.adapters.TareasAdapter
import com.example.sistema_educativo_padres.data.Tarea
import com.example.sistema_educativo_padres.sec.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class PendientesFragment : Fragment() {

    private val client = OkHttpClient()

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
        Log.e("AlumnoId", alumnoId.toString())
        if (alumnoId != null) {
            cargarPendientes(alumnoId)
        }

        return view
    }

    private fun cargarPendientes(alumnoId: String) {
        lifecycleScope.launch {
            val tareasPendientes = getTareasPendientes(alumnoId)
            delay(60000)

            adapter = TareasAdapter(tareasPendientes)
            recyclerPendientes.adapter = adapter
        }
    }

    private suspend fun getTareasPendientes(alumnoId: String): List<Tarea> {
        val token = TokenManager.obtenerToken(requireContext())
        val urlCursos = "http://192.168.0.10:8080/escuelaAlumnosCursos/api/alumnosCursos/alumnos/$alumnoId"

        val cursos = mutableListOf<Int>()
        val tareasPendientes = mutableListOf<Tarea>()

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
                                cursos.add(cursoId)
                                Log.w("Curso añadido", cursoId.toString())
                            }
                        }
                    }
                }
            }catch (e: Exception) {
                Log.e("Error cursos", "Exception: ${e.message}")
            }
        }

        for (cursoId in cursos) {
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
                                val tareas = mutableListOf<Tarea>()
                                for (j in 0 until jsonArray.length()) {
                                    val jsonTarea = jsonArray.getJSONObject(j)
                                    if (jsonTarea.getInt("avaladoPadre") == 0) {
                                        val tarea = Tarea(
                                            id = jsonTarea.getInt("id"),
                                            titulo = jsonTarea.getString("nombre"),
                                            calificacion = jsonTarea.getDouble("calificacion").toFloat(),
                                            fechaEntrega = jsonTarea.optLong("duedate", 0L),
                                            avalada = jsonTarea.getInt("avaladoPadre"),
                                            curso = cursoId
                                        )
                                        tareas.add(tarea)
                                        Log.w("Tarea agregada", tarea.toString())
                                    }
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

        return tareasPendientes
    }
}