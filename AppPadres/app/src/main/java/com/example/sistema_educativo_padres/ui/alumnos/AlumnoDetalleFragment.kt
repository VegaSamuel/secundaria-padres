package com.example.sistema_educativo_padres.ui.alumnos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.ui.tareas.PendientesFragment
import com.example.sistema_educativo_padres.ui.tareas.TodosFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.IllegalStateException

class AlumnoDetalleFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_alumno_detalle, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alumnoId = arguments?.getString("alumnoId") ?: ""
        val adapter = TareasPagerAdapter(this, alumnoId)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Pendientes"
                1 -> "Todos"
                else -> null
            }
        }.attach()
    }
}

class TareasPagerAdapter(fragment: Fragment, private val alumnoId: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PendientesFragment().apply {
                arguments = Bundle().apply {
                    putString("alumnoId", alumnoId)
                }
            }
            1 -> TodosFragment().apply {
                arguments = Bundle().apply {
                    putString("alumnoId", alumnoId)
                }
            }
            else -> throw IllegalStateException("Posición invalida")
        }
    }
}