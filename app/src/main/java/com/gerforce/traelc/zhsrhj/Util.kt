package com.gerforce.traelc.zhsrhj

import android.app.Application

class Util : Application() {
    companion object {
        @JvmStatic
        lateinit var inst: Util
            private set
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
    }

    var interfaceUrl: String = "http://47.100.115.87:10002/api/"
    lateinit var user: User
    lateinit var special1: List<Special1Template>
    lateinit var distinct: List<DistinctTemplate>
}