package com.example.sistema_educativo_padres.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.CursoConTarea

class CursosAdapter(private val cursos: List<CursoConTarea>) : RecyclerView.Adapter<CursosAdapter.CursoViewHolder>() {

    inner class CursoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val cursoTitulo: TextView = itemView.findViewById(R.id.cursoTitulo)
        val tareasContainer: LinearLayout = itemView.findViewById(R.id.tareas_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_todos, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]

        holder.cursoTitulo.text = curso.nombre
        holder.tareasContainer.visibility = if (curso.isExpanded) View.VISIBLE else View.GONE

        holder.tareasContainer.removeAllViews()
        curso.tareas.forEach { tarea ->
            val tareaView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_tarea, holder.tareasContainer, false)
            tareaView.findViewById<TextView>(R.id.tareaTitulo).text = tarea.titulo
            holder.tareasContainer.addView(tareaView)
        }
    }

    override fun getItemCount(): Int = cursos.size
}