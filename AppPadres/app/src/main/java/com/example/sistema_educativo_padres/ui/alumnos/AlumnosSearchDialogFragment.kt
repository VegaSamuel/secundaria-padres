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
import com.example.sistema_educativo_padres.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AlumnosSearchDialogFragment : DialogFragment() {
    private lateinit var searchField: EditText
    private lateinit var alumnosList: RecyclerView
    private val padre = LoginActivity()
    private val alumnosAdapter = AlumnosAdapter { alumno ->
        addAlumnoToDatabase(alumno)
        dismiss()
    }

    private val client = OkHttpClient()

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
        val url =
            "http://192.168.0.10/moodle/webservice/rest/server.php?wstoken=d2ed34a3369de1231f2b8cf8a4bd4059&wsfunction=core_user_search_identity&query=$query&moodlewsrestformat=json"

        lifecycleScope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream =
                            BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val responseText = inputStream.use { it.readText() }
                        JSONObject(responseText)
                    } else {
                        throw Exception("Error en la conexión ${urlConnection.responseCode}")
                    }
                }

                val alumnos = parseAlumnos(respuesta)
                alumnosAdapter.submitList(alumnos)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Error al buscar alumnos ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun parseAlumnos(jsonResponse: JSONObject): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val list = jsonResponse.optJSONArray("list") ?: return alumnos

        val padreId = getPadreId(padre.getCurrentUserEmail())
        val listAlumnosDB = getAlumnosList() as MutableList<Alumno>

        for (i in 0 until list.length()) {
            val user = list.getJSONObject(i)
            val fullname = user.getString("fullname")
            val email = user.optJSONArray("extrafields")?.let { extrafields ->
                extrafields.optJSONObject(0)?.getString("value") ?: ""
            } ?: ""

            val parts = fullname.split(" ")
            val nombre = parts.firstOrNull() ?: fullname
            val apellido = parts.drop(1).joinToString(" ")
            var alumno = Alumno(0, "", "", "", 0)

            if (padreId != null) {
                if (!nombre.equals("Administrador")) {
                    alumno = Alumno(i + 1, nombre, apellido, email, padreId)
                    alumnos.add(alumno)
                }
            }

            for (j in 0 until listAlumnosDB.size) {
                if(alumno.padre == listAlumnosDB[j].padre && alumno.nombre == listAlumnosDB[j].nombre) {
                    alumnos.remove(alumno)
                }
            }
        }

        return alumnos
    }

    private fun addAlumnoToDatabase(alumno: Alumno) {
        val url = "http://192.168.0.10:8080/escuelaAlumnos/api/alumnos"
        val jsonBody = JSONObject()
        jsonBody.put("nombre", alumno.nombre)
        jsonBody.put("apellido", alumno.apellido)
        jsonBody.put("email", alumno.email)
        jsonBody.put("idPadre", alumno.padre)

        val requestBody =
            jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("Registro", "Alumno registrado en la base de datos")
                } else {
                    Log.e("Registro", "Error al registrar al alumno en la base de datos")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Registro", "Fallo en la conexión al servicio REST", e)
            }
        })

        Toast.makeText(requireContext(), "Alumno agregado", Toast.LENGTH_SHORT).show()
    }

    private suspend fun getPadreId(correo: String): Int? {
        val url = "http://192.168.0.10:8080/escuelaPadres/api/padres/correo/$correo"

        return withContext(Dispatchers.IO) {
            try {
                val conexion = URL(url).openConnection() as HttpURLConnection
                conexion.requestMethod = "GET"
                conexion.connect()

                if (conexion.responseCode == HttpURLConnection.HTTP_OK) {
                    val responseText = conexion.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(responseText)

                    jsonResponse.optInt("id", -1).takeIf { it != -1 }
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getAlumnosList(): List<Alumno>? {
        val url = "http://192.168.0.10:8080/escuelaAlumnos/api/alumnos"
        val request = Request.Builder().url(url).get().build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (!responseBody.isNullOrEmpty()) {
                        val jsonArray = JSONArray(responseBody)
                        val alumnos = mutableListOf<Alumno>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonAlumno = jsonArray.getJSONObject(i)
                            val alumno = Alumno(
                                id = jsonAlumno.getInt("id"),
                                nombre = jsonAlumno.getString("nombre"),
                                apellido = jsonAlumno.getString("apellido"),
                                email = jsonAlumno.getString("email"),
                                padre = jsonAlumno.getInt("idPadre")
                            )
                            alumnos.add(alumno)
                        }
                        return@withContext alumnos
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
