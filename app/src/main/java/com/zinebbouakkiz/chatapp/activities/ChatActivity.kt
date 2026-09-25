package com.zinebbouakkiz.chatapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinebbouakkiz.chatapp.R
import com.zinebbouakkiz.chatapp.adapters.ChatRecyclerAdapter
import com.zinebbouakkiz.chatapp.models.Friend
import com.zinebbouakkiz.chatapp.models.Message
import com.zinebbouakkiz.chatapp.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView

    lateinit var chatRecyclerAdapter: ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser

        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChatList)

        val userUuid = intent.getStringExtra("friend")!!
        db.collection("users").document(userUuid).get().addOnSuccessListener {result ->
            if(result!= null){
                var user = result.toObject(User::class.java)
                user?.let {
                    user.uuid = userUuid // fichak (c'est regle)
                    setUserData(user)
                }
            }
        }.addOnFailureListener {
            Log.e("ChatActivity","error getting user",it)
        }
    }

    private fun setUserData(user: User) {
        supportActionBar?.title = user.fullname ?: "ChatApp"

        chatRecyclerAdapter = ChatRecyclerAdapter()

//        Message("zineb","android","salut karim",123423462, false),
        val messages = mutableListOf<Message>()

        rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatRecyclerAdapter
        }

        // ajout de message a la base d edonnes lorsque on clik sur envoyer
        fabSendMessage.setOnClickListener {
            val message = editMessage.text.toString()
            if(message.isNotEmpty()){
                val message = Message(currentUser!!.uid, user.uuid, message, System.currentTimeMillis(), false)
                editMessage.setText("")

                db.collection("messages").add(message).addOnSuccessListener {
                    rvChatList.scrollToPosition(messages.size - 1)
                }.addOnFailureListener {
                    Log.e("ChatActivity", "error adding message", it)
                }

                val friend = Friend("", user.fullname, message.text, user.image ?: "", System.currentTimeMillis())

                db.collection("users")
                    .document(currentUser!!.uid)
                    .collection("friends")
                    .document(user.uuid)
                    .set(friend)
                    .addOnSuccessListener {
                        Log.d("ChatActivity", "friend adding")
                    }.addOnFailureListener {
                        Log.e("ChatActivity", "error adding friend", it)
                    }

                // hide keyboard
//                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(editMessage.windowToken,0)
            }
        }

        // njibo les messages man les collection (bdd) wn7atohom f la liste bch ytafichaw f l'ecran

        val sentQuery = db.collection("messages")
            .whereEqualTo("sender",currentUser!!.uid)
            .whereEqualTo("receiver",user.uuid)
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val receiveQuery = db.collection("messages")
            .whereEqualTo("sender",user.uuid)
            .whereEqualTo("receiver",currentUser!!.uid)
            .orderBy("timestamp", Query.Direction.ASCENDING)

        sentQuery.addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Log.e("chatActivity","error getting message", exception)
                return@addSnapshotListener
            }
            for (document in snapshot!!.documents){
                var message = document.toObject(Message::class.java)
                message?.let {
                    message.isReceived = false
                    if(!messages.contains(message)){
                        messages.add(message)
                    }
                }
            }
            if(messages.isNotEmpty()){
                chatRecyclerAdapter.items = messages.sortedBy { it.timestamp } as MutableList<Message>
                rvChatList.scrollToPosition(messages.size - 1)
            }
        }

        receiveQuery.addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Log.e("chatActivity","error getting message", exception)
                return@addSnapshotListener
            }
            for (document in snapshot!!.documents){
                var message = document.toObject(Message::class.java)
                message?.let {
                    message.isReceived = true
                    if(!messages.contains(message)){
                        messages.add(message)
                    }
                }
            }
            if(messages.isNotEmpty()){
                chatRecyclerAdapter.items = messages.sortedBy { it.timestamp } as MutableList<Message>
                rvChatList.scrollToPosition(messages.size - 1)
            }
        }
    }
}