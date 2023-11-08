package com.example.contactauth.ui.screen.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.contactauth.R
import com.example.contactauth.data.local.room.entity.ContactEntity
import com.example.contactauth.databinding.FragmentAddBinding
import com.example.contactauth.presentation.AddViewModel
import com.example.contactauth.presentation.impl.AddViewModelImpl


class AddFragment : Fragment() {


    private lateinit var binding: FragmentAddBinding
    private val viewModel: AddViewModel by viewModels<AddViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.successLiveData.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.add.setOnClickListener {
            viewModel.add(
                ContactEntity(
                    "0",
                    binding.name.text.toString(),
                    binding.number.text.toString(),
                    0,
                    0,
                    0
                )
            )
        }
        viewModel.openScreenLiveData.observe(this) {
            findNavController().popBackStack()
        }

        return binding.root
    }

}