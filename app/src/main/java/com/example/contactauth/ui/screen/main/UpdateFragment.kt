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
import com.example.contactauth.databinding.FragmentUpdateBinding
import com.example.contactauth.presentation.EditViewModel
import com.example.contactauth.presentation.impl.EditViewModelImpl


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val viewModel: EditViewModel by viewModels<EditViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        val data = arguments?.getSerializable("contact") as ContactEntity

        binding.name.setText(data.name)
        binding.number.setText(data.phone.substring(4))
        viewModel.errorLiveData.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.successLiveData.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.add.setOnClickListener {
            viewModel.update(
                ContactEntity(
                    data.id,
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