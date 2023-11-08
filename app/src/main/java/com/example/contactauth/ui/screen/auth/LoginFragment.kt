package com.example.contactauth.ui.screen.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactauth.R
import com.example.contactauth.data.local.model.AuthData
import com.example.contactauth.databinding.FragmentLoginBinding
import com.example.contactauth.presentation.LoginViewModel
import com.example.contactauth.presentation.impl.LoginViewModelImpl


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()
    private val navController by lazy { findNavController() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.errorMessage.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.successLiveData.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                AuthData(
                    binding.login.text.toString(),
                    binding.password.text.toString()
                )
            )
        }
        binding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}