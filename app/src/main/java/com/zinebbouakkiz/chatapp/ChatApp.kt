package com.zinebbouakkiz.chatapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class ChatApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}