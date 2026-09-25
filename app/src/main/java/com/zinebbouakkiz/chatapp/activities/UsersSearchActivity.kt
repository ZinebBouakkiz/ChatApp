package com.zinebbouakkiz.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinebbouakkiz.chatapp.R
import com.zinebbouakkiz.chatapp.adapters.UsersRecyclerAdapter
import com.zinebbouakkiz.chatapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class UsersSearchActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    lateinit var rvUsers: RecyclerView
    lateinit var editSearch: EditText
    lateinit var usersRecyclerAdapter: UsersRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_search)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser

        rvUsers = findViewById(R.id.rvUsers)
        editSearch = findViewById(R.id.editSearch)
        usersRecyclerAdapter = UsersRecyclerAdapter()

        rvUsers.apply {
            layoutManager = LinearLayoutManager(this@UsersSearchActivity)
            adapter = usersRecyclerAdapter
        }

//       petit cmnt
        val users = mutableListOf<User>()

        db.collection("users")
            .whereNotEqualTo("email",currentUser?.email)
            .get().addOnSuccessListener {result ->
            for (document in result){
                val uuid = document.id
                val email = document.getString("email")
                val fullname = document.getString("fullname")
                users.add(User(uuid, email ?: "", fullname ?: "", null ))
            }
            usersRecyclerAdapter.items = users
        }.addOnFailureListener {
            Log.e("UsersSearchActivity","error getting users", it)
        }

        editSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usersRecyclerAdapter.filter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }
}