package com.example.memogame.presenter.screen.main

import androidx.lifecycle.LiveData
import com.example.memogame.enams.DifficultyEnum
import kotlinx.coroutines.flow.Flow

interface MainVm {
    val  cardLivedata:LiveData<MutableList<Int>>
    val openDialogFlow:Flow<Unit>
    fun loadCards(difficultyEnum: DifficultyEnum)
    fun openDialog()
}