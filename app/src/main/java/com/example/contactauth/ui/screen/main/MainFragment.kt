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
import com.example.contactauth.databinding.FragmentMainBinding
import com.example.contactauth.presentation.MainViewModel
import com.example.contactauth.presentation.impl.MainViewModelImpl
import com.example.contactauth.ui.adapter.ContactAdapter


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val adapter: ContactAdapter by lazy { ContactAdapter() }
    private val navController by lazy { findNavController() }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        viewModel.successLiveData.observe(this) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.listLiveData.observe(this) {
            if (it != null) {
                binding.text.visibility = View.GONE
            } else {
                binding.text.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        }
        binding.rv.adapter = adapter

        binding.addBtn.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddFragment())
        }

        adapter.submitEdit {
            val bundle = Bundle()
            bundle.putSerializable("contact", it)
            findNavController().navigate(R.id.action_mainFragment_to_updateFragment, bundle)
        }

        adapter.submitDelete {
            viewModel.delete(it)
        }
        binding.refresh.setOnClickListener {
            viewModel.sync()
        }

        binding.deleteAccount.setOnClickListener {
            viewModel.deleteAccount()
            findNavController().navigate(MainFragmentDirections.actionGlobalLoginFragment())

        }
        binding.logout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(MainFragmentDirections.actionGlobalLoginFragment())
        }
        return binding.root
    }
}