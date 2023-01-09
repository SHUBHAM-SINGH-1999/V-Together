package com.example.chat_app.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app.R
import com.example.chat_app.clicked
import com.example.chat_app.databinding.ActivityRequestWindowBinding
import com.example.chat_app.model.frndslist
import com.example.chat_app.request_adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class Request_Window : AppCompatActivity(), clicked {

    lateinit var binding: ActivityRequestWindowBinding
    lateinit var userid:String
    lateinit var list:ArrayList<frndslist>
    lateinit var realtime:FirebaseDatabase
    lateinit var dataOnFirestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_window)
        supportActionBar?.title="Requests"
        userid=FirebaseAuth.getInstance().uid.toString()
        realtime= FirebaseDatabase.getInstance()
        list=ArrayList()
        dataOnFirestore=FirebaseFirestore.getInstance()

        datafetch()

        binding.requestWindowContainer.adapter = request_adapter(list,this)
        binding.requestWindowContainer.layoutManager = LinearLayoutManager(this)
    }

    private fun datafetch() {
        list= intent.getParcelableArrayListExtra("list")!!
    }

    override fun delete(Senderid: String,pos:Int) {
        var reciverid=userid
        realtime.getReference().child("Request").child(reciverid).child(Senderid).removeValue()
        list.removeAt(pos)
        binding.requestWindowContainer.adapter?.notifyDataSetChanged()
    }

    override fun confirm(SenderName: String, SenderId: String, pos: Int,Image:String) {
        var reciverid= userid
        realtime.getReference().child("Request").child(reciverid).child(SenderId).removeValue()
        realtime.getReference().child("Request").child(SenderId).get().addOnSuccessListener {
            if(it.exists()) {
                it.child(reciverid).ref.removeValue()
            }
        }
        list.removeAt(pos)
        binding.requestWindowContainer.adapter?.notifyDataSetChanged()
        //Room Creation

        var roomid = "$reciverid&&$SenderId"
        var roomidImage = "$SenderId&&$reciverid"
        Roomcreation(roomid,roomidImage,SenderId)
    }

    private fun Roomcreation(roomid:String,roomidImage:String,SenderId: String) {

            var map1 = mutableMapOf("ReciverId" to SenderId,"message" to "")
            realtime.getReference().child("Rooms").child(userid).child(roomid).setValue(map1)

            var map2 = mutableMapOf("ReciverId" to userid,"message" to "")
            realtime.getReference().child("Rooms").child(SenderId).child(roomidImage).setValue(map2)

    }


}