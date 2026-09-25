package com.zinebbouakkiz.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.zinebbouakkiz.chatapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AuthentificationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var tvRegister: TextView
    lateinit var textInputLayoutEmail: TextInputLayout
    lateinit var textInputLayoutPassword: TextInputLayout
    lateinit var btnConnect: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)

        auth = Firebase.auth

        tvRegister = findViewById(R.id.tvRegister)
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail)
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword)
        btnConnect = findViewById(R.id.btnConnect)

    }

    override fun onStart() {
        super.onStart()

        tvRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        btnConnect.setOnClickListener {

            textInputLayoutEmail.isErrorEnabled = false
            textInputLayoutPassword.isErrorEnabled = false

            val email = textInputLayoutEmail.editText?.text.toString()
            val password = textInputLayoutPassword.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                if (email.isEmpty()){
                    textInputLayoutEmail.error = "Email is required!"
                    textInputLayoutEmail.isErrorEnabled = true
                }
                if (password.isEmpty()){
                    textInputLayoutPassword.error = "Password is required!"
                    textInputLayoutPassword.isErrorEnabled = true
                }
            }else{
                signIn(email,password)
            }

        }

    }

    fun signIn(email: String, password: String){
        Log.d("signIn","signIn user....")

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            } else {
                textInputLayoutEmail.error = " "
                textInputLayoutEmail.isErrorEnabled = true
                textInputLayoutPassword.error = "Authentification failed!"
                textInputLayoutPassword.isErrorEnabled = true
            }
        }

    }
}