package com.example.sistema_educativo_padres.ui.alumnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class AlumnosSearchDialogFragment : DialogFragment() {
    private lateinit var searchField: EditText
    private lateinit var alumnosList: RecyclerView
    private val alumnosAdapter = AlumnosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_alumnos_search, container, false)

        searchField = view.findViewById(R.id.search_alumno)
        alumnosList = view.findViewById(R.id.alumnos_list)
        alumnosList.adapter = alumnosAdapter

        searchField.addTextChangedListener { text ->
            fetchAlumnosFromMoodle(text.toString())
        }

        return view
    }

    private fun fetchAlumnosFromMoodle(query: String) {
        val url = "http://192.168.0.10/moodle/webservice/rest/server.php?wstoken=c4e5c7315a4f3d9fdca0ed1b04f3777a&wsfunction=core_user_get_users&criteria[0][key]=firstname&criteria[0][value]=%$query%&moodlewsrestformat=json"

        Thread {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuffer()
                var inputLine: String?

                while (inputStream.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }

                val jsonResponse = JSONObject(response.toString())

                val users = jsonResponse.getJSONArray("users")
                for (i in 0 until users.length()) {
                    val user = users.getJSONObject(i)
                    val nombre = user.getString("firstname")
                    val apellido = user.getString("lastname")
                    val email = user.getString("email")
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
