package com.example.sistema_educativo_padres.ui.alumnos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Alumno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class AlumnosSearchDialogFragment : DialogFragment() {
    private lateinit var searchField: EditText
    private lateinit var alumnosList: RecyclerView
    private val alumnosAdapter = AlumnosAdapter { alumno ->
        addAlumnoToDatabase(alumno)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_alumnos_search, container, false)

        searchField = view.findViewById(R.id.search_alumno)
        alumnosList = view.findViewById(R.id.alumnos_list)

        alumnosList.layoutManager = LinearLayoutManager(requireContext())
        alumnosList.adapter = alumnosAdapter

        searchField.addTextChangedListener { text ->
            fetchAlumnosFromMoodle(text.toString())
        }

        return view
    }

    private fun fetchAlumnosFromMoodle(query: String) {
        val url = "http://192.168.0.10/moodle/webservice/rest/server.php?wstoken=d2ed34a3369de1231f2b8cf8a4bd4059&wsfunction=core_user_search_identity&query=$query&moodlewsrestformat=json"

        lifecycleScope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO)  {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    if(urlConnection.responseCode ==  HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val responseText = inputStream.use { it.readText() }
                        JSONObject(responseText)
                    }else {
                        throw Exception("Error en la conexión ${urlConnection.responseCode}")
                    }
                }

                val alumnos = parseAlumnos(respuesta)
                alumnosAdapter.submitList(alumnos)
            }catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al buscar alumnos ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseAlumnos(jsonResponse: JSONObject): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val list = jsonResponse.optJSONArray("list") ?: return alumnos

        for (i in 0 until list.length()) {
            val user = list.getJSONObject(i)
            val fullname = user.getString("fullname")
            val email = user.optJSONArray("extrafields")?.let { extrafields ->
                extrafields.optJSONObject(0)?.getString("value") ?: ""
            } ?: ""

            val parts = fullname.split(" ")
            val nombre = parts.firstOrNull() ?: fullname
            val apellido = parts.drop(1).joinToString(" ")

            alumnos.add(Alumno(nombre, apellido, email, 1))
        }

        return alumnos
    }

    private fun addAlumnoToDatabase(alumno: Alumno) {
        Toast.makeText(requireContext(), "Alumno agregado", Toast.LENGTH_SHORT).show()
    }
}
