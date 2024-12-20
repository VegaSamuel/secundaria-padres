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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.adapters.AddAlumnosAdapter
import com.example.sistema_educativo_padres.data.Alumno
import com.example.sistema_educativo_padres.data.Curso
import com.example.sistema_educativo_padres.data.Tarea
import com.example.sistema_educativo_padres.sec.TokenManager
import com.example.sistema_educativo_padres.ui.homework.TareasViewModel
import com.example.sistema_educativo_padres.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val tareas: TareasViewModel by lazy {
        ViewModelProvider(requireActivity())[TareasViewModel::class.java]
    }
    private val padre = LoginActivity()
    private val addAlumnosAdapter = AddAlumnosAdapter { alumno ->
        lifecycleScope.launch {
            addAlumnoToDatabase(alumno)
            getCursosFromMoodle(alumno)
            tareas.recargarAlumnos(padre.getCurrentUserEmail(), requireContext())
            delay(2500)
            dismiss()
        }
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
        alumnosList.adapter = addAlumnosAdapter

        searchField.addTextChangedListener { text ->
            fetchAlumnosFromMoodle(text.toString())
        }

        return view
    }

    private fun fetchAlumnosFromMoodle(query: String) {
        val url =
            "http://192.168.0.10/moodle/webservice/rest/server.php?" +
                    "wstoken=d2ed34a3369de1231f2b8cf8a4bd4059" +
                    "&wsfunction=core_user_search_identity" +
                    "&query=$query" +
                    "&moodlewsrestformat=json"

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
                addAlumnosAdapter.submitList(alumnos)
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
            val id = user.getInt("id")
            val fullname = user.getString("fullname")
            val email = user.optJSONArray("extrafields")?.let { extrafields ->
                extrafields.optJSONObject(0)?.getString("value") ?: ""
            } ?: ""

            val parts = fullname.split(" ")
            val nombre = parts.firstOrNull() ?: fullname
            val apellido = parts.drop(1).joinToString(" ")

            if (padreId != null) {
                if (!nombre.equals("Administrador")) {
                    val alumno = Alumno(id, nombre, apellido, email, padreId)

                    if(listAlumnosDB.none {it.nombre == alumno.nombre && it.apellido == alumno.apellido}) {
                        alumnos.add(alumno)
                    }
                }
            }
        }

        return alumnos
    }

    private fun getCursosFromMoodle(alumno: Alumno) {
        val url = "http://192.168.0.10/moodle/webservice/rest/server.php?" +
                "wstoken=d2ed34a3369de1231f2b8cf8a4bd4059&" +
                "wsfunction=core_enrol_get_users_courses&" +
                "userid=${alumno.id}&" +
                "moodlewsrestformat=json"

        lifecycleScope.launch {
            try {
                val cursos = withContext(Dispatchers.IO) {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val responseText = inputStream.use { it.readText() }

                        val cursosJSONArray = JSONArray(responseText)
                        val cursosList = mutableListOf<Curso>()

                        for (i in 0 until cursosJSONArray.length()) {
                            val cursoJson = cursosJSONArray.getJSONObject(i)
                            cursosList.add(
                                Curso(
                                    id = cursoJson.getInt("id"),
                                    nombre = cursoJson.getString("fullname")
                                )
                            )
                        }

                        cursosList
                    }else {
                        throw Exception("Error en la respuesta: ${urlConnection.responseCode}")
                    }
                }

                for (curso in cursos) {
                    addCursosToDatabase(curso, alumno)
                    getTareasFromMoodle(curso, alumno)
                }
            }catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Error al buscar los cursos del alumno ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getTareasFromMoodle(curso: Curso, alumno: Alumno) {
        val url = "http://192.168.0.10/moodle/webservice/rest/server.php?" +
                "wstoken=d2ed34a3369de1231f2b8cf8a4bd4059&" +
                "wsfunction=mod_assign_get_assignments&" +
                "courseids[0]=${curso.id}&" +
                "moodlewsrestformat=json"

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val responseText = inputStream.use { it.readText() }

                        val responseJson = JSONObject(responseText)
                        val cursosArray = responseJson.getJSONArray("courses")

                        for (i in 0 until cursosArray.length()) {
                            val cursoJson = cursosArray.getJSONObject(i)
                            val tareasArray = cursoJson.getJSONArray("assignments")

                            for (j in 0 until tareasArray.length()) {
                                val tareaJson = tareasArray.getJSONObject(j)
                                addTareaToDatabase(
                                    Tarea(
                                        id = tareaJson.getInt("id"),
                                        titulo = tareaJson.getString("name"),
                                        fechaEntrega = tareaJson.optLong("duedate", 0L),
                                        calificacion = 0.0f,
                                        avalada = 0,
                                        curso = getAlumnoCursosId(curso, alumno)!!
                                    )
                                )
                            }
                        }
                    }else {
                        throw Exception("Error en la respuesta: ${urlConnection.responseCode}")
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Error al buscar las tareas del alumno ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun addAlumnoToDatabase(alumno: Alumno) {
        val url = "http://192.168.0.10:8080/escuelaAlumnos/api/alumnos"
        val jsonBody = JSONObject()
        jsonBody.put("id", alumno.id)
        jsonBody.put("nombre", alumno.nombre)
        jsonBody.put("apellido", alumno.apellido)
        jsonBody.put("email", alumno.email)
        jsonBody.put("idPadre", alumno.padre)

        val token = TokenManager.obtenerToken(requireContext())
        val requestBody =
            jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).header("Authorization", "Bearer $token").post(requestBody).build()

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

    private suspend fun addCursosToDatabase(curso: Curso, alumno: Alumno) {
        var url = "http://192.168.0.10:8080/escuelaCursos/api/cursos"
        var jsonBody = JSONObject()
        jsonBody.put("id", curso.id)
        jsonBody.put("nombre", curso.nombre)

        val token = TokenManager.obtenerToken(requireContext())
        var requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        var request = Request.Builder().url(url).header("Authorization", "Bearer $token").post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Registro cursos", "Cursos registrados en la base de datos")
                }else {
                    Log.e("Registro cursos", "Error al registrar los cursos del alumno en la base de datos")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Registro cursos", "Fallo en la conexión al servicio REST", e)
            }
        })

        url = "http://192.168.0.10:8080/escuelaAlumnosCursos/api/alumnosCursos"
        jsonBody = JSONObject()
        jsonBody.put("idAlumno", alumno.id)
        jsonBody.put("idCurso", curso.id)
        jsonBody.put("calificacion", 0)

        requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        request = Request.Builder().url(url).header("Authorization", "Bearer $token").post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Relación alumnno - cursos", "Cursos relacionados con los alumnos en la base de datos")
                }else {
                    Log.e("Relación alumnno - cursos", "Error al relacionar los cursos con su respectivo alumno en la base de datos")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Relación alumnno - cursos", "Fallo en la conexión al servicio REST", e)
            }
        })
    }

    private suspend fun addTareaToDatabase(tarea: Tarea) {
        val url = "http://192.168.0.10:8080/escuelaTareas/api/tareas"
        val jsonBody = JSONObject()
        jsonBody.put("id", tarea.id)
        jsonBody.put("nombre", tarea.titulo)
        jsonBody.put("fecha_entrega", tarea.fechaEntrega)
        jsonBody.put("calificacion", tarea.calificacion)
        jsonBody.put("avalado_padre", tarea.avalada)
        jsonBody.put("idCurso", tarea.curso)

        val token = TokenManager.obtenerToken(requireContext())
        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).header("Authorization", "Bearer $token").post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Registro tarea", "Tareas registradas en la base de datos")
                }else {
                    Log.e("Registro tarea", "Error al registrar las tareas del alumno en la base de datos")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Registro tarea", "Fallo en la conexión al servicio REST")
            }
        })
    }

    private suspend fun getPadreId(correo: String): Int? {
        val token = TokenManager.obtenerToken(requireContext())
        val url = "http://192.168.0.10:8080/escuelaPadres/api/padres/correo/$correo"

        return withContext(Dispatchers.IO) {
            try {
                val conexion = URL(url).openConnection() as HttpURLConnection
                conexion.requestMethod = "GET"
                conexion.setRequestProperty("Authorization", "Bearer $token")
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

    private suspend fun getAlumnoCursosId(curso: Curso, alumno: Alumno): Int? {
        val token = TokenManager.obtenerToken(requireContext())
        val url = "http://192.168.0.10:8080/escuelaAlumnosCursos/api/alumnosCursos/${curso.id}/alumnos/${alumno.id}"

        return withContext(Dispatchers.IO) {
            try {
                val conexion = URL(url).openConnection() as HttpURLConnection
                conexion.requestMethod = "GET"
                conexion.setRequestProperty("Authorization", "Bearer $token")
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
        val token = TokenManager.obtenerToken(requireContext().applicationContext)
        val url = "http://192.168.0.10:8080/escuelaAlumnos/api/alumnos"
        val request = Request.Builder().url(url).header("Authorization", "Bearer $token").get().build()

        Log.e("URL API", request.toString())

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
