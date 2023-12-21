package com.utad.ayllonaplicacionideas.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.utad.ayllonaplicacionideas.databinding.FragmentLoginBinding
import com.utad.ayllonaplicacionideas.ui.ideas.IdeasActivity
import com.utad.ayllonaplicacionideas.model.data_store.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLoginVerify()

        binding.btnSingUp.setOnClickListener {
            goToSingUp()
        }

        binding.btnSingIn.setOnClickListener {
                checkLogin()
        }
    }

    private fun userLoginVerify() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getLoginCheck(requireContext()).collect { check ->
                Log.d("Encuentra", "$check")
                if (check) {
                    withContext(Dispatchers.Main) {
                        val intent = Intent(requireContext(), IdeasActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun checkLogin() {

        var userName: String? = binding.etLoginUser.text.toString().trim()
        var userPass: String? = binding.etLoginPassword.text.toString()

        if(!userName.isNullOrEmpty() && !userPass.isNullOrEmpty()) {
            var isNameValid: Boolean? = null
            var isPassValid: Boolean? = null

            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.getUser(requireContext()).collect { user ->
                    isNameValid = user == userName
                    checkCredentials(isNameValid, isPassValid)
                    userName = null
                }
            }

            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.getPassword(requireContext()).collect { pass ->
                    isPassValid = pass == userPass
                    checkCredentials(isNameValid, isPassValid)
                    userPass = null
                }
            }
        }
    }


    private suspend fun checkCredentials(nameValid: Boolean?, passValid: Boolean?) {
        if (nameValid == true && passValid == true) {
            DataStoreManager.setLoginCheckIn(requireContext())
            withContext(Dispatchers.Main) {
                goToPrincipal()
            }
        }
    }

    private fun goToPrincipal() {
        val intent = Intent(requireContext(), IdeasActivity::class.java)
        startActivity(intent)
    }

    private fun goToSingUp() {
        // Fragmento+Directions
        val action : NavDirections = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

}
