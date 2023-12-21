package com.utad.ayllonaplicacionideas.ui.ideas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.utad.ayllonaplicacionideas.databinding.FragmentUserDataBinding
import com.utad.ayllonaplicacionideas.model.data_store.DataStoreManager
import com.utad.ayllonaplicacionideas.ui.login.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataFragment : Fragment() {

    private lateinit var _binding: FragmentUserDataBinding
    private val binding: FragmentUserDataBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataUser()

        binding.btnDeleteUser.setOnClickListener {
            deleteUser()
        }

        binding.btnExit.setOnClickListener {
            exitUser()
        }
    }

    private fun exitUser() {
        lifecycleScope.launch(Dispatchers.IO){
            DataStoreManager.setLoginCheckOut(requireContext())
        }
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun deleteUser() {
        lifecycleScope.launch(Dispatchers.IO){
            DataStoreManager.deleteUser(requireContext())
        }

        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun dataUser() {
        lifecycleScope.launch(Dispatchers.IO){
            DataStoreManager.getAllDataUser(requireContext()).collect{ user ->
                withContext(Dispatchers.Main){
                    binding.tvUserName.text = user.name
                    binding.tvUserPassword.text = "Contraseña: ${user.password}"
                }
            }
        }

    }

}