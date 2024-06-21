package com.example.memogame.presenter.screen.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.memogame.R
import com.example.memogame.databinding.ScreenPlayBinding
import com.example.memogame.utils.setStatusBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayScreen:Fragment(R.layout.screen_play) {
    private var _binding:ScreenPlayBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=ScreenPlayBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setStatusBar(binding.spase)
        binding.apply {
            play.setOnClickListener {
                findNavController().navigate(PlayScreenDirections.actionPlayScreenToHomeScreen())
            }
            info.setOnClickListener {
                findNavController().navigate(PlayScreenDirections.actionPlayScreenToInfoScreen())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        anim()

    }


    fun anim()=binding.apply{
        root.post {
            val oldInfoPosX=info.x
            val oldPlayPosY=play.y
            info.x=(root.width+info.width).toFloat()
            play.y=(root.height+play.height).toFloat()
            gameIcon.scaleX=0f
            gameIcon.scaleY=0f
            gameIcon.animate()
                .setDuration(500)
                .scaleX(1f)
                .scaleY(1f)
                .start()

            info.animate()
                .setDuration(500)
                .x(oldInfoPosX)
                .start()
            play.animate()
                .setDuration(500)
                .y(oldPlayPosY)
                .start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
