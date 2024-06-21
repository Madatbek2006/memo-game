package com.example.memogame.presenter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memogame.R
import com.example.memogame.data.MyShar
import com.example.memogame.utils.statusBarTRANSPARENT

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyShar.init(this)
        statusBarTRANSPARENT()
    }
}