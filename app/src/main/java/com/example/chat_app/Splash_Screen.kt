package com.example.chat_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.chat_app.Activity.Login
import com.example.chat_app.Activity.MainActivity
import com.example.chat_app.Activity.Userdata
import com.example.chat_app.Fragments.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Splash_Screen : AppCompatActivity() {


     var userid:String?=null
    lateinit var UserdataOnFirestore:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        userid = FirebaseAuth.getInstance().uid.toString()
        UserdataOnFirestore = FirebaseFirestore.getInstance()

        if(userid=="null"){
            Handler().postDelayed({
                startActivity(Intent(this, Login::class.java))
                finish()
            },1500)
        }
        else{
            CheckForUser()
        }
    }

    private fun CheckForUser() {
        UserdataOnFirestore.collection("UserData").document(userid!!).get().addOnSuccessListener {
            if(it.exists()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, Userdata::class.java))
                finish()
            }
        }
    }
}