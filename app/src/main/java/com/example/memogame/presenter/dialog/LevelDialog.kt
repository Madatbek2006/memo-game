package com.example.memogame.presenter.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.memogame.R
import com.example.memogame.data.MyShar
import com.example.memogame.databinding.DialogLevelUpBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LevelDialog:DialogFragment(){
    private var _binding:DialogLevelUpBinding?=null
    private val binding by lazy { _binding!! }
    var onClickHome:(()->Unit)?=null
    var onClickNext:(()->Unit)?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
        _binding=DialogLevelUpBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.home.isClickable=false
        binding.next.isClickable=false
        binding.home.scaleY=0f
        binding.home.scaleX=0f
        binding.next.scaleY=0f
        binding.next.scaleX=0f
        val time=requireArguments().getLong("time")
        val attempt=requireArguments().getInt("attempt")
            binding.time.base=time-SystemClock.elapsedRealtime()
            var media:MediaPlayer?=MediaPlayer.create(requireContext(),R.raw.score)
            media?.start()
            media?.setOnCompletionListener {
                media!!.start()
            }
            animNumber(attempt){
                media?.stop()
                media=null
                lifecycleScope.launch {
                    delay(400)
                    binding.apply {
                        star1.scaleX=0f
                        star1.scaleY=0f
                        star2.scaleX=0f
                        star2.scaleY=0f
                        star3.scaleX=0f
                        star3.scaleY=0f
                        if (MyShar.getMedia()){
                            MediaPlayer.create(requireContext(),R.raw.star_media1).start()
                            animStar(star1){
                                MediaPlayer.create(requireContext(),R.raw.star_media2).start()
                                animStar(star2){
                                    MediaPlayer.create(requireContext(),R.raw.star_media3).start()
                                    animStar(star3){
                                        lottie.playAnimation()
                                        MediaPlayer.create(requireContext(),R.raw.you_win).start()
                                        animScale(home)
                                        animScale(next)
                                    }
                                }
                            }
                        }else{
                            animStar(star1){
                                animStar(star2){
                                    animStar(star3){
                                        lottie.playAnimation()
                                        animScale(home)
                                        animScale(next)
                                    }
                                }
                            }
                        }
                        home.setOnClickListener {
                            onClickHome?.invoke()
                            dismiss()
                        }
                        next.setOnClickListener {
                            onClickNext?.invoke()
                            dismiss()
                        }
                    }
                }
            }





    }

    override fun onStart() {
        super.onStart()
        this.dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun animStar(view:View,b:(()->Unit)){
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .withEndAction {
                lifecycleScope.launch{
                    delay(timeMillis = 100)
                    b()
                }

            }
            .start()

    }
    private fun animNumber( end: Int,b:()->Unit) {
        val valueAnimator = ValueAnimator.ofInt(0, end)

        valueAnimator.duration = 100*end.toLong()
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            binding.attempt.text = "$animatedValue"
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                b()
            }
        })
        valueAnimator.start()
    }

    fun animScale(view:View){
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .withEndAction{
                view.isClickable=true
            }
            .start()

    }

}