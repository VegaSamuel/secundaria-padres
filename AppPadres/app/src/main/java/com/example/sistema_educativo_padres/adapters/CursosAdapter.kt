package com.example.sistema_educativo_padres.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.CursoConTarea

class CursosAdapter(private val cursos: List<CursoConTarea>) : RecyclerView.Adapter<CursosAdapter.CursoViewHolder>() {

    inner class CursoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val cursoCard: CardView = itemView.findViewById(R.id.cursoCard)
        val cursoTitulo: TextView = itemView.findViewById(R.id.cursoTitulo)
        val tareasContainer: LinearLayout = itemView.findViewById(R.id.tareas_container)
        val recyclerTareas: RecyclerView = itemView.findViewById(R.id.recyclerTodosTareas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_curso_con_tareas, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        Log.e("Cursos", cursos.toString())

        holder.cursoTitulo.text = curso.nombre
        holder.tareasContainer.visibility = if (curso.isExpanded) View.VISIBLE else View.GONE

        holder.recyclerTareas.layoutManager = LinearLayoutManager(holder.itemView.context)
        val tareasAdapter = TareasAdapter(curso.tareas)
        holder.recyclerTareas.adapter = tareasAdapter

        holder.cursoCard.setOnClickListener {
            curso.isExpanded = !curso.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = cursos.size

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarDatos(cursos: List<CursoConTarea>) {
        (cursos as MutableList).clear()
        cursos.addAll(cursos)
        notifyDataSetChanged()
    }
}