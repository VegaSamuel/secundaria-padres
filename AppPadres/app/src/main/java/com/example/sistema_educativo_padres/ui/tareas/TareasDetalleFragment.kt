package com.example.sistema_educativo_padres.ui.tareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sistema_educativo_padres.R

class TareasDetalleFragment : Fragment() {

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

        val tituloView = view.findViewById<TextView>(R.id.cursoTareaTitutloDetalle)
        val cursoView = view.findViewById<TextView>(R.id.cursoTituloDetalle)
        val fechaEntregaView = view.findViewById<TextView>(R.id.tareaFechaDetalle)
        val calificacionView = view.findViewById<TextView>(R.id.cursoTareaCalificacion)

        tituloView.text = titulo
        cursoView.text = curso
        fechaEntregaView.text = fechaEntrega
        calificacionView.text = getString(R.string.calificacion_format, calificacion)
    }
}