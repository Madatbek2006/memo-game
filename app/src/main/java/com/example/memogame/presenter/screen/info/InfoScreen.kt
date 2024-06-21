package com.example.memogame.presenter.screen.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.memogame.R
import com.example.memogame.databinding.ScreenInfoBinding
import com.example.memogame.utils.setStatusBar

class InfoScreen:Fragment(R.layout.screen_info) {
    private var _binding:ScreenInfoBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=ScreenInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().setStatusBar(binding.spase)
        binding.back.setOnClickListener {

            findNavController().navigateUp()
        }
    }
}