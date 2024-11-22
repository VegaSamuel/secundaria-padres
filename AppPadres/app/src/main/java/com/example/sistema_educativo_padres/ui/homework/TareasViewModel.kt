package com.example.sistema_educativo_padres.ui.homework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistema_educativo_padres.data.Alumno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class TareasViewModel : ViewModel() {

    private val client = OkHttpClient()

    private val _alumnos = MutableLiveData<List<Alumno>>()
    val alumnos: LiveData<List<Alumno>> = _alumnos

    private val _text = MutableLiveData<String>().apply {
        value = "No hay alumnos que visualizar\nUse el botón para agregar a su hijo y poder ver sus tareas"
    }
    val text: LiveData<String> = _text

    fun cargarPadreYAlumnos(correo: String) {
        viewModelScope.launch {
            val padreId = getPadreId(correo)
            if (padreId != null) {
                val alumnoList = getAlumnosList(padreId)
                _alumnos.postValue(alumnoList ?: emptyList())
            } else {
                _alumnos.postValue(emptyList())
            }
        }
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

    private suspend fun getAlumnosList(idPadre: Int): List<Alumno>? {
        val url = "http://192.168.0.10:8080/escuelaAlumnos/api/alumnos/padre/$idPadre"
        val request = Request.Builder().url(url).get().build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                response.use {
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
                }
                null
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}