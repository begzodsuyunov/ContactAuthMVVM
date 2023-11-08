package com.example.contactauth.ui.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactauth.R
import com.example.contactauth.databinding.FragmentSplashBinding
import com.example.contactauth.presentation.SplashViewModel
import com.example.contactauth.presentation.impl.SplashViewModelImpl
import com.example.contactauth.utils.toast


class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)
    private val navController by lazy { findNavController() }
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.openLoginScreenLiveData.observe(this) {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
        viewModel.openMainScreenLiveData.observe(this) {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                toast("Loading Start")
            } else {
                toast("Loading End")
            }
        }

    }

}