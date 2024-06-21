package com.example.memogame.presenter.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.memogame.R
import com.example.memogame.databinding.ScreenHomeBinding
import com.example.memogame.utils.setStatusBar

class HomeScreen:Fragment() {
    private var _binding:ScreenHomeBinding?=null
    private val binding by lazy { _binding!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding=ScreenHomeBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setStatusBar(binding.spase)
        binding.root.post {
            val oldYEasy=binding.easy.y
            val oldYMedium=binding.medium.y
            val oldYHard=binding.hard.y
            binding.easy.y=0F
            binding.medium.y=0F
            binding.hard.y=0F
            binding.easy.visibility=View.INVISIBLE
            binding.medium.visibility=View.INVISIBLE
            binding.hard.animate()
                .setDuration(500)
                .setInterpolator(BounceInterpolator())
                .y(oldYHard)
                .withEndAction {
                    binding.medium.isVisible=true
                    binding.medium.animate()
                        .setDuration(500)
                        .setInterpolator(BounceInterpolator())
                        .y(oldYMedium)
                        .withEndAction {
                            binding.easy.isVisible=true
                            binding.easy.animate()
                                .setDuration(500)
                                .setInterpolator(BounceInterpolator())
                                .y(oldYEasy)
                                .start()
                        }
                        .start()
                }
                .start()

            binding.easy.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreen_to_mainScreen,bundleOf(Pair("difficulty",1)))
            }
            binding.medium.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreen_to_mainScreen,bundleOf(Pair("difficulty",2)))
            }
            binding.hard.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreen_to_mainScreen, bundleOf(Pair("difficulty",3)))
            }
            binding.back.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }
}