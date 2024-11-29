package com.example.sistema_educativo_padres.ui.tareas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Tarea
import com.example.sistema_educativo_padres.sec.TokenManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class TareasDetalleFragment : Fragment() {
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tareas_detalle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titulo = arguments?.getString("titulo")
        val curso = arguments?.getString("curso")
        val fechaEntrega = arguments?.getString("fechaEntrega")
        val calificacion = arguments?.getFloat("calificacion")
        val tareaId = arguments?.getString("tareaId")
        val cursoId = arguments?.getString("cursoId")
        Log.d("ID tarea", tareaId.toString())

        val tituloView = view.findViewById<TextView>(R.id.cursoTareaTitutloDetalle)
        val cursoView = view.findViewById<TextView>(R.id.cursoTituloDetalle)
        val fechaEntregaView = view.findViewById<TextView>(R.id.tareaFechaDetalle)
        val calificacionView = view.findViewById<TextView>(R.id.cursoTareaCalificacion)

        tituloView.text = titulo
        cursoView.text = curso
        fechaEntregaView.text = fechaEntrega
        calificacionView.text = getString(R.string.calificacion_format, calificacion)

        val avalarBtn = view.findViewById<Button>(R.id.avalar_btn)
        val tarea = Tarea(tareaId!!.toInt(), titulo!!, fechaEntrega!!.toLong(), calificacion!!, 1, cursoId!!.toInt())

        avalarBtn.setOnClickListener {
            avalarTarea(tarea)
            avalarBtn.visibility = View.GONE
        }
    }

    private fun avalarTarea(tarea: Tarea) {
        val token = TokenManager.obtenerToken(requireContext())
        val url = "http://192.168.0.10:8080/escuelaTareas/api/tareas/${tarea.id}"

        val jsonBody = JSONObject()
        jsonBody.put("id", tarea.id)
        jsonBody.put("nombre", tarea.titulo)
        jsonBody.put("calificacion", tarea.calificacion)
        jsonBody.put("avaladoPadre", tarea.avalada)
        jsonBody.put("idCurso", tarea.curso)

        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).header("Authorization", "Bearer $token").put(requestBody).build()

        Log.e("REQUEST", request.toString())

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Tarea actualizada", "Tarea actualizada en la base de datos")
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Tarea avalada con éxito", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Log.e("Tarea actualizada", "Error al actualizar la tarea ${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Tarea actualizada", "fallo en la conexión al servicio REST")
            }
        })
    }
}