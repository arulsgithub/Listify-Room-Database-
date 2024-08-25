package com.example.listify

import android.app.Application

class ListifyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.create(this)

    }
}