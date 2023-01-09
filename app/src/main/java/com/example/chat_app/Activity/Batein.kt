package com.example.chat_app.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.Adapter.adapter_chat
import com.example.chat_app.R
import com.example.chat_app.databinding.ActivityBateinBinding
import com.example.chat_app.model.dataForChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class batein : AppCompatActivity() {

    lateinit var binding: ActivityBateinBinding
    lateinit var reciverlist:ArrayList<dataForChat>
    lateinit var sendername:String
    lateinit var senderprofile:String
    lateinit var senderid:String
    lateinit var firebasefirestore: FirebaseFirestore
    lateinit var userid:String
    lateinit var realtime:FirebaseDatabase
    lateinit var myimage:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_batein)
        supportActionBar?.hide()

        reciverlist = ArrayList()
        sendername = intent.getStringExtra("sendername").toString()
        senderid = intent.getStringExtra("senderid").toString()
        senderprofile = intent.getStringExtra("senderimage").toString()
        myimage = intent.getStringExtra("myimage").toString()

        binding.headingName.text = sendername
        Glide.with(this).load(senderprofile).into(binding.headigAvatar)

        firebasefirestore = FirebaseFirestore.getInstance()
        userid= FirebaseAuth.getInstance().uid.toString()
        realtime = FirebaseDatabase.getInstance()


        binding.textrecycle.adapter = adapter_chat(reciverlist)
        binding.textrecycle.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true
        }
        binding.textrecycle.smoothScrollToPosition(reciverlist.size)

        recivedata()

        binding.sendbutton.setOnClickListener {
            senddata()
        }
    }

    private fun recivedata() {
        var SenderId = senderid
        var reciverid = userid

        var roomid = "$reciverid&&$SenderId"
        var roomidImage = "$SenderId&&$reciverid"

        realtime.getReference().child("Rooms").child(reciverid).child(roomid).child("message").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var message = snapshot.value.toString()
                reciverlist.add(dataForChat(message,senderprofile,SenderId))
                binding.textrecycle.adapter?.notifyDataSetChanged()
                binding.textrecycle.smoothScrollToPosition(reciverlist.size)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun senddata() {
        var SenderId = senderid
        var reciverid = userid

        var roomid = "$reciverid&&$SenderId"
        var roomidImage = "$SenderId&&$reciverid"

        var sendermessage = binding.sendtext.text.toString()

        realtime.getReference().child("Rooms").child(SenderId).child(roomidImage).child("message").setValue(sendermessage)
        reciverlist.add(dataForChat(sendermessage,myimage,reciverid))
        binding.textrecycle.adapter?.notifyDataSetChanged()
        binding.textrecycle.smoothScrollToPosition(reciverlist.size)
        binding.sendtext.text.clear()
    }
}