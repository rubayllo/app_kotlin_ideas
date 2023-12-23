package com.utad.ayllonaplicacionideas.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.utad.ayllonaplicacionideas.databinding.FragmentRegisterBinding
import com.utad.ayllonaplicacionideas.model.data_store.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding: FragmentRegisterBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etRegisterUser.doAfterTextChanged {
            buttonEneabled()
        }

        binding.etRegisterPassword.doAfterTextChanged {
            buttonEneabled()
        }

        binding.etRegisterPasswordConfirm.doAfterTextChanged {
            buttonEneabled()
        }

        binding.btnCreateUser.setOnClickListener {
            createUser()
        }
    }

    private fun buttonEneabled() {
        val userName = binding.etRegisterUser.text.trim()
        val userPass = binding.etRegisterPassword.text
        val userPassConfirm = binding.etRegisterPasswordConfirm.text

        binding.btnCreateUser.isEnabled = !userName.isNullOrEmpty() && !userPass.isNullOrEmpty() && !userPassConfirm.isNullOrEmpty()
    }

    private fun createUser() {
        val confirmUserPass = binding.etRegisterPassword.text.toString() == binding.etRegisterPasswordConfirm.text.toString()
        if(confirmUserPass){
            lifecycleScope.launch(Dispatchers.IO) {
                val userName = binding.etRegisterUser.text.toString().trim()
                val userPass = binding.etRegisterPassword.text.toString()
                DataStoreManager.saveUser(requireContext(), userName, userPass)
            }
            Toast.makeText(requireContext(), "Usuario creado", Toast.LENGTH_SHORT).show()

            val action: NavDirections = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)


        } else {
            Toast.makeText(requireContext(), "El password no coincide", Toast.LENGTH_SHORT).show()
        }

    }

}