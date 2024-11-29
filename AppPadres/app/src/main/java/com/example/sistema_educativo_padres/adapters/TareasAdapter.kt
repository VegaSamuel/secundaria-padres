package com.example.sistema_educativo_padres.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Tarea

class TareasAdapter(private val tareas: List<Tarea>) : RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {
    private var onItemClickListener: ((Tarea) -> Unit)? = null

    inner class TareaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tareaTitulo)
        val fecha: TextView = itemView.findViewById(R.id.tareaFecha)
        val curso: TextView = itemView.findViewById(R.id.tareaCurso)

        fun bind(tarea: Tarea) {
            titulo.text = tarea.titulo
            fecha.text = tarea.fechaEntrega.toString()
            curso.text = tarea.nombreCurso
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.bind(tarea)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(tarea)
        }
    }

    override fun getItemCount(): Int = tareas.size

    fun setOnItemClickListener(listener: (Tarea) -> Unit) {
        onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarDatos(tareas: List<Tarea>) {
        (tareas as MutableList).clear()
        tareas.addAll(tareas)
        notifyDataSetChanged()
    }
}