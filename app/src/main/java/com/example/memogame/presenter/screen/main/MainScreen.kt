package com.example.memogame.presenter.screen.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.memogame.R
import com.example.memogame.data.MyShar
import com.example.memogame.databinding.ScreenMainBinding
import com.example.memogame.enams.DifficultyEnum
import com.example.memogame.presenter.dialog.LevelDialog
import com.example.memogame.utils.createDialog
import com.example.memogame.utils.dpToPx
import com.example.memogame.utils.myLog
import com.example.memogame.utils.setStatusBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainScreen : Fragment() {
    private var isRefresh: Boolean=true
    private var _binding: ScreenMainBinding? = null
    private val binding by lazy { _binding!! }
    private val viewModel: MainVm by viewModels<MainVMImpl>()
    private var x = 0
    private var y = 0
    private var cardWidth = 0f
    private var cardHeight = 0f
    private val viewList = ArrayList<ImageView>()
    private lateinit var images: MutableList<Int>
    private var count = 0
    private lateinit var difficultyEnum: DifficultyEnum
    private var oldIndex = -1
    private var attempt=0
    private var success=0
    private var level=1
    var isMedia=false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setStatusBar(binding.spase)
        initLivedata()
        setXY()
        onBackPress()
        binding.apply {
            isMedia=MyShar.getMedia()
            if (isMedia){
                media.setImageResource(R.drawable.button_media)
            }else{
                media.setImageResource(R.drawable.botton_media2)
            }
            time.start()
            container.post {
                val containerY = binding.space.y
                val containerX = 8.dpToPx()
                cardHeight = container.height.toFloat() / y
                cardWidth = container.width.toFloat() / x
                for (i in 0..<x) {
                    for (j in 0..<y) {
                        val im = ImageView(requireContext())

                        im.x = containerX.toFloat()
                        im.y = containerY
                        root.addView(im)
                        viewList.add(im)
                        val params=ConstraintLayout.LayoutParams(
                            cardWidth.toInt()-cardWidth.toInt()/16,cardHeight.toInt()-cardWidth.toInt()/16
                        )
                        im.layoutParams=params
                        im.setBackgroundResource(R.drawable.bg_card)
                        im.setImageResource(R.drawable.question_mark)
                    }
                }
                setAnimations()
                viewModel.loadCards(difficultyEnum)
            }

            media.setOnClickListener {
                if (isMedia){
                    isMedia=false
                    media.setImageResource(R.drawable.botton_media2)
                }else{
                    isMedia=true
                    media.setImageResource(R.drawable.button_media)
                }
                MyShar.setMedia(isMedia)
            }
            home.setOnClickListener {
                val time=binding.time.base-SystemClock.elapsedRealtime()
                binding.time.stop()
                val dialog = requireActivity().createDialog(R.layout.dialog_continue, R.style.TransparentDialog)
                dialog.findViewById<View>(R.id.exit).setOnClickListener {
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
                dialog.findViewById<View>(R.id.check).setOnClickListener {
                    dialog.dismiss()
                    binding.time.base=time+SystemClock.elapsedRealtime()
                    binding.time.start()
                }
            }
        }
        binding.reload.setOnClickListener {
            if (!isRefresh)return@setOnClickListener
            viewModel.loadCards(difficultyEnum)
            binding.time.base=SystemClock.elapsedRealtime()
            binding.time.start()
            success=0
        }
    }

    private fun onBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val time=binding.time.base-SystemClock.elapsedRealtime()
                binding.time.stop()
                val dialog = requireActivity().createDialog(R.layout.dialog_continue, R.style.TransparentDialog)
                dialog.findViewById<View>(R.id.exit).setOnClickListener {
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
                dialog.findViewById<View>(R.id.check).setOnClickListener {
                    dialog.dismiss()
                    binding.time.base=time+SystemClock.elapsedRealtime()
                    binding.time.start()
                }
//                parentFragmentManager.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setXY() {
        val difficulty = requireArguments().getInt("difficulty")
        when (difficulty) {
            1 -> {
                x = 3
                y = 4
                difficultyEnum = DifficultyEnum.EASY
            }

            2 -> {
                x = 4
                y = 6
                difficultyEnum = DifficultyEnum.MEDIUM
            }

            3 -> {
                x = 6
                y = 8
                difficultyEnum = DifficultyEnum.HARD
            }
        }
    }
    val mediaOpen by lazy{MediaPlayer.create(requireContext(),R.raw.cart1)}
    val mediaBack by lazy{MediaPlayer.create(requireContext(),R.raw.cart2)}
    private fun setAnimations() {
        for (i in 0..<viewList.size) {
            viewList[i].setOnClickListener {
//                media.stop()
//                media.release()
                if (count == 2||oldIndex==i) return@setOnClickListener
                if (isMedia){
                    mediaOpen.seekTo(0)
                    mediaOpen.start()
                }
                clickCard(i)
                when (count) {
                    0 -> {
                        count++
                        oldIndex = i
                    }
                    1 -> {
                        isRefresh=false
                        count++
                        attempt++
                        binding.attempt.text=attempt.toString()
                        if (images[i] == images[oldIndex]) {
                            success++
                            lifecycleScope.launch {
                                delay(1000)
                                correctCards(i, oldIndex)
                                delay(500)
                                count = 0
                                if (success*2==x*y)viewModel.openDialog()
                            }
                        } else {
                            lifecycleScope.launch {
                                viewList[i].isClickable = false
                                viewList[oldIndex].isClickable = false
                                delay(1000)
                                backCard(i)
                                backCard(oldIndex)
                                delay(500)
                                count = 0
                            }

                        }
                    }
                }

            }
        }
    }

    fun initLivedata() {
        viewModel.cardLivedata.observe(viewLifecycleOwner) {list->
            isRefresh=true
            "salom".myLog()
            images = list
            var i=0
            var j=-1
            binding.container.post {
                val containerY = binding.space.y
                val containerX = 8.dpToPx()
                lifecycleScope.launch {

                    for (k in 0 until viewList.size) {
                        val im = viewList[k]
                        im.isClickable = true
                        oldIndex = -1
                        attempt = 0
                        binding.attempt.text = "0"
                        count = 0
                        im.alpha = 1f
                        im.setBackgroundResource(R.drawable.bg_card)
                        im.setImageResource(R.drawable.question_mark)
                        im.x = containerX.toFloat()
                        im.y = containerY

                        j++
                        if (j >= y) {
                            j = 0
                            i++
                        }
                        im.animate()
                            .setDuration(500)
                            .y(cardHeight * j + containerY)
                            .x(cardWidth * i + containerX)
                            .start()
//                        delay(100)
                    }
                }
            }
        }
        viewModel.openDialogFlow.onEach {
            val time=binding.time.base+SystemClock.elapsedRealtime()
            binding.time.stop()
            val dialog=LevelDialog()
            dialog.onClickHome={
                findNavController().navigateUp()
            }
            dialog.onClickNext={
                viewModel.loadCards(difficultyEnum)
                binding.time.base=SystemClock.elapsedRealtime()
                binding.time.start()
                success=0
                level++
                binding.level.text="$level"
            }
            dialog.apply {
                arguments= bundleOf(Pair("time",time),Pair("attempt",attempt))
            }
            dialog.show(requireActivity().supportFragmentManager,"")
            dialog.isCancelable=false
        }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun backCard(index: Int) {
        if (isMedia){
        mediaBack.start()
        }
        viewList[index].animate()
            .setDuration(250)
            .rotationY(-89F)
            .withEndAction {
                viewList[index].rotationY = 89f
                viewList[index].setImageResource(R.drawable.empty_image)
                viewList[index].setBackgroundResource(R.drawable.bg_card)
                viewList[index].animate()
                    .setDuration(250)
                    .rotationY(0f)
                    .start()
                viewList[index].isClickable = true
                isRefresh=true
            }
            .start()
    }

    private fun correctCards(index1: Int, index2: Int) {
        viewList[index1].animate()
            .setDuration(500)
            .alpha(0.2f)
            .start()
        viewList[index2].animate()
            .setDuration(500)
            .alpha(0.2f)
            .start()
        viewList[index1].isClickable = false
        viewList[index2].isClickable = false
    }

    private fun clickCard(index: Int) {
        viewList[index].isClickable = false
        viewList[index].animate()
            .setDuration(250)
            .rotationY(89F)
            .withEndAction {
                viewList[index].rotationY = -89f
                viewList[index].setImageResource(images[index])
                viewList[index].setBackgroundResource(R.drawable.bg_card2)
                viewList[index].animate()
                    .setDuration(250)
                    .rotationY(0f)
                    .start()
                viewList[index].isClickable = true
            }
            .start()
    }
    fun getRoundedCornerBitmap(context: Context, input: Bitmap, cornersRadius: Int): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = RectF(0f, 0f, input.width.toFloat(), input.height.toFloat())
        val roundedCorners = context.resources.displayMetrics.density * cornersRadius

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = 0xFFFFFFFF.toInt()
        canvas.drawRoundRect(rect, roundedCorners, roundedCorners, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(input, 0f, 0f, paint)

        return output
    }

    override fun onStart() {
        super.onStart()
        binding.level.text="${MyShar.getLvl(difficultyEnum.name)}"
    }
    override fun onStop() {
        super.onStop()
        MyShar.saveLVL(difficultyEnum.name,level)
    }
}