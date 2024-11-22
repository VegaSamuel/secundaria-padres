package com.example.sistema_educativo_padres.ui.alumnos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Alumno

class AlumnoTareasAdapter(
    private var alumnos: List<Alumno>,
    private val onAlumnoClick: (Alumno) -> Unit
) : RecyclerView.Adapter<AlumnoTareasAdapter.AlumnoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlumnos(newAlumnos: List<Alumno>) {
        alumnos = newAlumnos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnos[position]
        holder.bind(alumno, onAlumnoClick)
    }

    override fun getItemCount(): Int = alumnos.size

    class AlumnoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombreAlumno)

        fun bind(alumno: Alumno, onClick: (Alumno) -> Unit) {
            nombreTextView.text = "${alumno.nombre} ${alumno.apellido}"
            itemView.setOnClickListener { onClick(alumno) }
        }
    }
}