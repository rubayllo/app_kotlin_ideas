package com.utad.ayllonaplicacionideas.ui.ideas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utad.ayllonaplicacionideas.databinding.FragmentInfoIdeaDataBinding
import com.utad.ayllonaplicacionideas.model.room_data_base.DetailListAdapter
import com.utad.ayllonaplicacionideas.model.room_data_base.MyAplicationRoom
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Detail
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.IdeaWithDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoIdeaDataFragment : Fragment() {

    private lateinit var _binding: FragmentInfoIdeaDataBinding
    private val binding: FragmentInfoIdeaDataBinding get() = _binding

    private val navigationArgs: InfoIdeaDataFragmentArgs by navArgs()
    private var ideaId: Int? = null

    private val adapter = DetailListAdapter({ detail, position -> deleteDetail(detail, position) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInfoIdeaDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recogemos el id que nos envía el fragmento previo
        ideaId = navigationArgs.ideaId
        setRecyclerView()

        loadDataBaseInfo()

        binding.btnAddNewDetail.setOnClickListener {
            if (!binding.etNewDetail.text.isNullOrBlank()) {
                saveDetailInDataBase()
                loadDataBaseInfo()
            }
        }

        binding.rbLow.setOnClickListener { updateIdeaInDataBase() }
        binding.rbMiddle.setOnClickListener { updateIdeaInDataBase() }
        binding.rbHigh.setOnClickListener { updateIdeaInDataBase() }
        binding.rbStatusPendiente.setOnClickListener { updateIdeaInDataBase() }
        binding.rbStatusEnProgreso.setOnClickListener { updateIdeaInDataBase() }
        binding.rbStatusTerminado.setOnClickListener { updateIdeaInDataBase() }

    }

    private fun setRecyclerView() {
        // Asigno el layautManager y el adaptador a nuestro RecyclerView
        binding.rvDetailInfo.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvDetailInfo.adapter = adapter
    }

    private fun loadDataBaseInfo() {
        val application = requireContext().applicationContext as MyAplicationRoom

        lifecycleScope.launch(Dispatchers.IO) {
            // Hago la petición de la idea y sus detalles
            val ideaDataInfo =
                application.room.daoDataBase().getIdeaWithDetailById(ideaId!!)
            //Mostramos los datos del empleado en la interfaz
            withContext(Dispatchers.Main) {
                setIdeaDataInfoScreen(ideaDataInfo)
            }
        }
    }

    // Visualiza la info de la relación Idea-Detail de la base de datos por pantalla
    private suspend fun setIdeaDataInfoScreen(ideaDataInfo: Flow<IdeaWithDetail>) {
        ideaDataInfo.collect { info ->
            binding.ivIdeaImageInfo.setImageBitmap(info.idea.image)

            binding.tvTittleIdeaInfo.text = info.idea.nombre
            binding.tvDescripIdeaInfo.text = info.idea.descripcion
            binding.tvTittleIdeaInfo.text = info.idea.nombre

            binding.rbLow.isChecked = (binding.rbLow.text.toString() == info.idea.prioridad)
            binding.rbMiddle.isChecked = (binding.rbMiddle.text.toString() == info.idea.prioridad)
            binding.rbHigh.isChecked = (binding.rbHigh.text.toString() == info.idea.prioridad)

            binding.rbStatusPendiente.isChecked =
                (binding.rbStatusPendiente.text.toString() == info.idea.estado)
            binding.rbStatusEnProgreso.isChecked =
                (binding.rbStatusEnProgreso.text.toString() == info.idea.estado)
            binding.rbStatusTerminado.isChecked =
                (binding.rbStatusTerminado.text.toString() == info.idea.estado)

            //Añadimos a la recyclerview la lista de detalles de nuestra idea
            adapter.submitList(info.details)
        }
    }

    private fun saveDetailInDataBase() {
        val detail = binding.etNewDetail.text.toString().trim()

        val application = requireContext().applicationContext as MyAplicationRoom

        lifecycleScope.launch(Dispatchers.IO) {
            // id = 0 porque es autogenerate
            val detail = Detail(0, detail, ideaId!!)

            //Accedemos a la clase Application y le hacemos un cast al tipo MyAplicationRoom
            // que tiene nuestra base de datos
            application
                .room //Accedemos a la variable pública de nuestra base de datos
                .daoDataBase() //Accedemos al dao para poder interactuar con la base de datos
                .saveDetailList(detail) //Llamamos al @Insert que guarda nuestra idea nueva
        }
        binding.etNewDetail.text.clear()
    }

    private fun updateIdeaInDataBase() {
        val priority = when {
            binding.rbLow.isChecked -> binding.rbLow.text.toString()
            binding.rbMiddle.isChecked -> binding.rbMiddle.text.toString()
            binding.rbHigh.isChecked -> binding.rbHigh.text.toString()
            else -> {
                "null"
            }
        }
        val status = when {
            binding.rbStatusPendiente.isChecked -> binding.rbStatusPendiente.text.toString()
            binding.rbStatusEnProgreso.isChecked -> binding.rbStatusEnProgreso.text.toString()
            binding.rbStatusTerminado.isChecked -> binding.rbStatusTerminado.text.toString()
            else -> {
                "null"
            }
        }
        val application = requireContext().applicationContext as MyAplicationRoom

        lifecycleScope.launch(Dispatchers.IO) {
            //Accedemos a la clase Application y le hacemos un cast al tipo MyAplicationRoom
            // que tiene nuestra base de datos
            application
                .room //Accedemos a la variable pública de nuestra base de datos
                .daoDataBase() //Accedemos al dao para poder interactuar con la base de datos
                .updateIdeaPriorityAndStatus(
                    priority,
                    status,
                    ideaId!!
                ) //Llamamos al @Update que guarda nuestra idea actualizada
        }

    }

    private fun deleteDetail(detail: Detail, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Borrar detalle")
            .setMessage("¿Realmente quieres borrar este detalle?")
            .setPositiveButton("Okay") { dialog, which ->
                /** Accedo a la aplicacion a través de la activity y le hago un cast a nuestra clase que contiene la BD. */
                val application = requireContext().applicationContext as MyAplicationRoom
                lifecycleScope.launch(Dispatchers.IO) {
                    /** Através del Dao elimino el item que he recibido por parámetro en esta función*/
                    application.room.daoDataBase().deleteDetail(detail)

                    //Eliminamos la idea de la lista de la RyclerView copiando la lista y eliminando la idea seleccionada
                    withContext(Dispatchers.Main) {
                        val newList = mutableListOf<Detail>()
                        newList.addAll(adapter.currentList)
                        newList.removeAt(position)
                        adapter.submitList(newList) //refrescamos la lista
                    }
                }
            }
            .setNegativeButton("Denegar") { dialog, which ->
                // Lo dejo en blanco puesto que no quiero hacer nada
            }
            .show()
    }

}