package com.example.contactauth.ui.screen.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.contactauth.databinding.FragmentRegisterBinding
import com.example.contactauth.presentation.RegisterViewModel
import com.example.contactauth.presentation.impl.RegisterViewModelImpl


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.errorMessage.observe(this) {

            if (it == null) {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.successLiveData.observe(this) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            Log.d("TTT", it)
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            viewModel.createAccount(
                AuthData(
                    binding.login.text.toString(),
                    binding.password.text.toString()
                )
            )
        }

        viewModel.loadingLiveData.observe(this) {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        }
    }
}