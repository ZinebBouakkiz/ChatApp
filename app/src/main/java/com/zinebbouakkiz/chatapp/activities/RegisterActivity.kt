package com.zinebbouakkiz.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zinebbouakkiz.chatapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var tilName: TextInputLayout
    lateinit var tilEmail: TextInputLayout
    lateinit var tilPassword: TextInputLayout
    lateinit var tilConfirmPassword: TextInputLayout
    lateinit var btnSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {

            initErrors()

            val name = tilName.editText?.text.toString()
            val email = tilEmail.editText?.text.toString()
            val password = tilPassword.editText?.text.toString()
            val confirmPassword = tilConfirmPassword.editText?.text.toString()

            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                if (name.isEmpty()){
                    tilName.error = "Name is required!"
                    tilName.isErrorEnabled = true
                }
                if (email.isEmpty()){
                    tilEmail.error = "Email is required!"
                    tilEmail.isErrorEnabled = true
                }
                if (password.isEmpty()){
                    tilPassword.error = "Password is required!"
                    tilPassword.isErrorEnabled = true
                }
                if (confirmPassword.isEmpty()){
                    tilConfirmPassword.error = "Confirm Password is required!"
                    tilConfirmPassword.isErrorEnabled = true
                }
            } else{
                if(password != confirmPassword){
                    tilPassword.error = " "
                    tilPassword.isErrorEnabled = true
                    tilConfirmPassword.error = "Password did not match!"
                    tilConfirmPassword.isErrorEnabled = true
                } else{
                    // creation d'un utilisaeur dans le module authentification de firebase
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->

                        if(task.isSuccessful){
                            // Create a new user with a first and last name
                            val user = hashMapOf(
                                "fullname" to name,
                                "email" to email
                            )

                            // creation d'un utilisaeur dans le module firestore
                            val db = Firebase.firestore
                            val currentUser = auth.currentUser
                            // Add a new document with a generated ID
                            // la on veut pas generer un nouveau id car il est deja creer, on veut juste relier le nouveau document avec le user deja creer avant (avec email et password)
                            db.collection("users").document(currentUser!!.uid)
                                .set(user)
                                .addOnSuccessListener { documentReference ->
                                    Intent(this, HomeActivity::class.java).also {
                                        startActivity(it)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    tilName.error = " "
                                    tilName.isErrorEnabled = true
                                    tilEmail.error = " "
                                    tilEmail.isErrorEnabled = true
                                    tilPassword.error = " "
                                    tilPassword.isErrorEnabled = true
                                    tilConfirmPassword.error = "Error occurred please try again!"
                                    tilConfirmPassword.isErrorEnabled = true
                                }
                        }else{
                            tilName.error = " "
                            tilName.isErrorEnabled = true
                            tilEmail.error = " "
                            tilEmail.isErrorEnabled = true
                            tilPassword.error = " "
                            tilPassword.isErrorEnabled = true
                            tilConfirmPassword.error = "Error occurred please try again!"
                            tilConfirmPassword.isErrorEnabled = true
                        }
                    }
                }
            }

        }

    }

    private fun initErrors() {
        tilName.isErrorEnabled = false
        tilEmail.isErrorEnabled = false
        tilPassword.isErrorEnabled = false
        tilConfirmPassword.isErrorEnabled = false
    }
}