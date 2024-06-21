package com.example.memogame.domain

interface AppRepository {
    fun getEasyCard():MutableList<Int>
    fun getMediumCard():MutableList<Int>
    fun getHardCard():MutableList<Int>
}