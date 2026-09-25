package com.zinebbouakkiz.chatapp.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.zinebbouakkiz.chatapp.R
import com.zinebbouakkiz.chatapp.models.Data
import com.zinebbouakkiz.chatapp.models.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    lateinit var ivUser: ImageView
    lateinit var tilName: TextInputLayout
    lateinit var tilEmail: TextInputLayout
    lateinit var btnSave: MaterialButton
    var isImageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser

        ivUser = findViewById(R.id.ivUser)
        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        btnSave = findViewById(R.id.btnSave)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            it?.let {
                Glide.with(this).load(it).placeholder(R.drawable.photo_user).into(ivUser)
                isImageChanged = true
            }
        }

        ivUser.setOnClickListener {
            pickImage.launch("image/*")
        }

        if(currentUser != null){
            // njibo les donnes ta3 user m database wndirohom f settings
            db.collection("users").document(currentUser!!.uid).get().addOnSuccessListener {result ->
                if(result!= null){
                    val user = result.toObject(User::class.java)
                    user?.let {
                        user.uuid = currentUser!!.uid
                        setUserData(user)
                    }
                }
            }
        }else{
            Log.d("SettingsActivity","No User Found!")
        }
    }

    private fun setUserData(user: User) {

        // initialisaton des champs par les donnes qui exist dans la base de donnees
        tilName.editText?.setText(user.fullname)
        tilEmail.editText?.setText(user.email)
        user.image?.let {
            Glide.with(this).load(it).placeholder(R.drawable.photo_user).into(ivUser)
        }
        Data(2)
        btnSave.setOnClickListener {
            tilName.isErrorEnabled = false

            // la donnes qui est dans le champ durant le click sur enregistrer
            val fullname = tilName.editText?.text.toString()
            if (fullname.isEmpty()){
                tilName.error = "Name is required!"
                tilName.isErrorEnabled = true
            }else{
                if(isImageChanged){
                    // upload image et le nom car cette methode va appeler updateUserData
                    uploadImageToFirebaseStorage(user)
                }else if(tilName.editText?.text.toString() != user.fullname){
                    // upload le nom
                    updateUserData(user)
                }else{ // ca va rien changer
                    Toast.makeText(this, "Your informations are up to date", Toast.LENGTH_LONG).show()
                    tilName.clearFocus()
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage(user: User) {
        // Create a storage reference from our app
        val storageRef = Firebase.storage.reference

        // Create a reference to 'images/..'
        val imageRef = storageRef.child("images/${user.uuid}")

        // l'image en mode bitmap
        val bitmap = (ivUser.drawable as BitmapDrawable).bitmap

        // le mode byteArray
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // upload the byte array to firebase storage
        val uploadTask = imageRef.putBytes(data)
        // si l'image est bien uploadi dans la base de donnees
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {uri ->
                user.image = uri.toString()
                updateUserData(user)
            }
        }
    }

    private fun updateUserData(user: User) {
        val updatedUser = hashMapOf<String, Any>(
            "fullname" to tilName.editText?.text.toString(),
            "image" to (user.image ?: "")
        )

        // mettre a jour les donnees
        db.collection("users").document(user.uuid).update(updatedUser).addOnCompleteListener {
            Toast.makeText(this, "Your informations are up to date", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            tilName.error = "Error, please try again!"
            tilName.isErrorEnabled = true
        }
    }
}