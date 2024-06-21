package com.example.memogame.presenter.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memogame.domain.AppRepository
import com.example.memogame.domain.AppRepositoryImpl
import com.example.memogame.enams.DifficultyEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class MainVMImpl() :ViewModel(),MainVm {
    private var openDialog:(()->Unit)?=null
    override val openDialogFlow: Flow<Unit> = channelFlow {
        openDialog={trySend(Unit)}
        awaitClose { openDialog=null }
    }
    private val appRepository: AppRepository=AppRepositoryImpl()
    override val cardLivedata=MutableLiveData<MutableList<Int>>()

    override fun loadCards(difficultyEnum: DifficultyEnum){
        cardLivedata.value=when(difficultyEnum){
            DifficultyEnum.EASY->appRepository.getEasyCard()
            DifficultyEnum.MEDIUM->appRepository.getMediumCard()
            DifficultyEnum.HARD->appRepository.getHardCard()
        }
    }

    override fun openDialog() {
        openDialog?.invoke()
    }
}