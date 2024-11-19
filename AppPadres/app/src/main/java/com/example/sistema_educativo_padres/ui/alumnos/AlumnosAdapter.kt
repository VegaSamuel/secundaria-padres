package com.example.sistema_educativo_padres.ui.alumnos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Alumno

class AlumnosAdapter(
    private val onAddAlumno: (Alumno) -> Unit
) : ListAdapter<Alumno, AlumnosAdapter.AlumnoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = getItem(position)
        holder.bind(alumno)
    }

    inner class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val alumnoName: TextView = itemView.findViewById(R.id.alumno_name)
        private val addButton: Button = itemView.findViewById(R.id.add_button)

        fun bind(alumno: Alumno) {
            alumnoName.text = "${alumno.nombre} ${alumno.apellido}"

            addButton.setOnClickListener {
                onAddAlumno(alumno)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Alumno>() {
            override fun areItemsTheSame(oldItem: Alumno, newItem: Alumno) = oldItem.email == newItem.email
            override fun areContentsTheSame(oldItem: Alumno, newItem: Alumno) = oldItem == newItem
        }
    }
}