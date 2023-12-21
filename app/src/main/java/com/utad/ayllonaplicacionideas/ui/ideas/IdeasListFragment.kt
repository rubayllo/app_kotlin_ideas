package com.utad.ayllonaplicacionideas.ui.ideas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.ayllonaplicacionideas.databinding.FragmentIdeasListBinding
import com.utad.ayllonaplicacionideas.model.room_data_base.IdeasListAdapter
import com.utad.ayllonaplicacionideas.model.room_data_base.MyAplicationRoom
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Idea
import com.utad.ayllonaplicacionideas.ui.addidea.AddIdeaActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdeasListFragment : Fragment() {

    private lateinit var _binding: FragmentIdeasListBinding
    private val binding: FragmentIdeasListBinding get() = _binding

    private val adapter = IdeasListAdapter(
        { ideaId -> goToInfoIdea(ideaId)},
        { idea, position -> removeIdea(idea, position)})


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIdeasListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fbAddIdea.setOnClickListener {
            val intent = Intent(requireContext(), AddIdeaActivity::class.java)
            startActivity(intent)
        }

        setRecyclerView()
    }

    // onResume para que se actualice la lista al volver a este fragment
    override fun onResume() {
        super.onResume()
        getIdeaFromDataBase()
    }

    private fun getIdeaFromDataBase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val application = (requireActivity().application as MyAplicationRoom)
            val newList = application.room.daoDataBase().getAllIdeaList()
            withContext(Dispatchers.Main) {
                adapter.submitList(newList)
            }
        }
    }


    private fun setRecyclerView() {
        // Asigno el layautManager y el adaptador a nuestro RecyclerView
        binding.rvIdeaList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvIdeaList.adapter = adapter
    }

    private fun removeIdea(idea: Idea, position: Int) {
        val application = requireContext().applicationContext as MyAplicationRoom

        lifecycleScope.launch(Dispatchers.IO) {
            application.room.daoDataBase().deleteIdea(idea)
            application.room.daoDataBase().deleteIdeaWithDetail(idea.id)

            //Eliminamos la idea de la lista de la RyclerView copiando la lista y eliminando la idea seleccionada
            withContext(Dispatchers.Main) {
                val newList = mutableListOf<Idea>()
                newList.addAll(adapter.currentList)
                newList.removeAt(position)
                adapter.submitList(newList) //refrescamos la lista
            }
        }
    }

    private fun goToInfoIdea(ideaId: Int) {
        val action: NavDirections = IdeasListFragmentDirections.actionIdeasDetailsFragmentToInfoIdeaDataFragment(ideaId)
        findNavController().navigate(action)
    }
}