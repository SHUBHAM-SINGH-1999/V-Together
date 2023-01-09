package com.example.chat_app.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app.R
import com.example.chat_app.click
import com.example.chat_app.databinding.ActivityFindFrndsBinding
import com.example.chat_app.frndsAdapter
import com.example.chat_app.model.frndslist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class FindFrnds : AppCompatActivity(), click {

    lateinit var binding: ActivityFindFrndsBinding
    lateinit var userDataOnFirestore: FirebaseFirestore
    lateinit var userid:String
    lateinit var frendslist:ArrayList<frndslist>
    lateinit var username:String
    lateinit var userImage:String
    lateinit var realtime:FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_frnds)
        supportActionBar?.title="Find Friends"

        userid = FirebaseAuth.getInstance().uid.toString()
        userDataOnFirestore = FirebaseFirestore.getInstance()
        frendslist = ArrayList()
        realtime = FirebaseDatabase.getInstance()

        username = intent.getStringExtra("nametof").toString()
        userImage = intent.getStringExtra("imagetof").toString()

        binding.frndsRecycleView.adapter = frndsAdapter(frendslist,this)
        binding.frndsRecycleView.layoutManager = LinearLayoutManager(this)

        search()
    }

    private fun search() {
        binding.friendsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                frendslist.clear()
                data(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    private fun data(id: String?) {
        var senderid = userid
        var recevierid = id.toString()
        if (recevierid != senderid){
            userDataOnFirestore.collection("UserData").document(recevierid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        var name = it.data!!["name"].toString()
                        var image = it.data!!["image"].toString()
                        frendslist.add(frndslist(name, image, recevierid))
                        binding.frndsRecycleView.adapter?.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { Toast.makeText(this, "error", Toast.LENGTH_SHORT).show() }
        }else{
            Toast.makeText(this, "You cannot sent request to yourself", Toast.LENGTH_SHORT).show()
        }
    }


    override fun clicked(reciverid: String) {
        var senderid=userid
        var reciverid = reciverid
        Toast.makeText(this, "Requset Send", Toast.LENGTH_SHORT).show()

        var s = frndslist(username,userImage,senderid)

        realtime.getReference().child("Request").child(reciverid).child(senderid).setValue(s)

    }

}