package com.example.sistema_educativo_padres.ui.homework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_educativo_padres.data.Alumno
import com.example.sistema_educativo_padres.databinding.FragmentTareasBinding
import com.example.sistema_educativo_padres.ui.alumnos.AlumnoTareasAdapter
import com.example.sistema_educativo_padres.ui.login.LoginActivity

class TareasFragment : Fragment() {

    private var _binding: FragmentTareasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val padre = LoginActivity()
    private lateinit var tareasViewModel: TareasViewModel
    private lateinit var alumnosAdapter: AlumnoTareasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tareasViewModel = ViewModelProvider(requireActivity())[TareasViewModel::class.java]

        _binding = FragmentTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewAlumnos
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        alumnosAdapter = AlumnoTareasAdapter(emptyList()) { alumno ->
            onAlumnoSelected(alumno)
        }
        recyclerView.adapter = alumnosAdapter

        tareasViewModel.alumnos.observe(viewLifecycleOwner) {alumnos ->
            if(alumnos.isNullOrEmpty()) {
                binding.textHome.text = "No hay alumnos que visualizar\nUse el botón para agregar a su hijo"
                recyclerView.visibility = View.GONE
            } else {
                binding.textHome.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                alumnosAdapter.updateAlumnos(alumnos)
            }
        }

        val currentEmail = padre.getCurrentUserEmail()
        tareasViewModel.cargarPadreYAlumnos(currentEmail)

        return root
    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.w("Tareas", "onViewCreated ejecutado, registrando listener")

        setFragmentResultListener("alumno_added") {_, bundle ->
            Log.w("Tareas", "Resultado recibido")
            val status = bundle.getString("status")
            if(status == "alumno_agregado") {
                Log.w("Tareas", "Alumno agregado, recargando alumnos")
                val currentEmail = padre.getCurrentUserEmail()
                tareasViewModel.recargarAlumnos(currentEmail)
            }
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onAlumnoSelected(alumno: Alumno) {

    }
}