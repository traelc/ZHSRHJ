package com.gerforce.traelc.zhsrhj

import android.app.Application

class Util : Application() {
    companion object {
        @JvmStatic lateinit var inst: Util
            private set
    }
    override fun onCreate() {
        super.onCreate()
        inst = this
    }
}