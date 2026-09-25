package com.zinebbouakkiz.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.zinebbouakkiz.chatapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // on ajoute un temp d'attent avant entrer dans homeActivity (2ms)
        Handler(Looper.getMainLooper()).postDelayed({

            val auth = Firebase.auth
            val currentUser = auth.currentUser

            if(currentUser != null){
                Intent(this, HomeActivity::class.java).also{
                    startActivity(it)
                }
            }else{
                Intent(this, AuthentificationActivity::class.java).also{
                    startActivity(it)
                }
            }
            finish()
        },2000)


    }
}