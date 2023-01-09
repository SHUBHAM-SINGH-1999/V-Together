package com.example.chat_app.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chat_app.R
import com.example.chat_app.databinding.ActivityUserdataBinding
import com.example.chat_app.model.data_User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Userdata : AppCompatActivity() {

    lateinit var binding: ActivityUserdataBinding
     var selectedimage: Uri? = null
    lateinit var userdb:FirebaseDatabase
    lateinit var imageUpload : FirebaseStorage
    lateinit var userid:String
    lateinit var userdataOnFirestore:FirebaseFirestore
    lateinit var dialog:AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_userdata)
        supportActionBar?.title="AboutYou"

        userdb = FirebaseDatabase.getInstance()
        imageUpload = FirebaseStorage.getInstance()
        userid = FirebaseAuth.getInstance().uid.toString()
        userdataOnFirestore = FirebaseFirestore.getInstance()

        dialog = ProgressDialog(this)
        dialog.setMessage("Submitting")
        dialog.setCancelable(false)

        binding.circleImageView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.SubmitButton.setOnClickListener {
                dataset()
        }
    }



    private  fun dataset() {
        var username = binding.editTextPersonName.text.toString()
        var usernickname =  binding.tag.text.toString()
        if(username.isEmpty() ||usernickname.isEmpty()){
            Toast.makeText(this,"Enter the details",Toast.LENGTH_SHORT).show()
        }else {
                dialog.show()
                if (selectedimage != null) {
                    imageUpload.getReference("images/ProfileImage/$userid").putFile(selectedimage!!).addOnSuccessListener {
                        imageUpload.getReference().child("images/ProfileImage/$userid").downloadUrl.addOnSuccessListener {
                            var uri = it.toString()
                            val userdata = data_User(username, usernickname, uri)
                            userdataOnFirestore.collection("UserData").document(userid).set(userdata)
                            dialog.cancel()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    imageUpload.reference.child("images/ProfileImage/default.png").downloadUrl.addOnSuccessListener {
                        var uri = it.toString()
                        val userdata = data_User(username, usernickname, uri)
                        userdataOnFirestore.collection("UserData").document(userid).set(userdata)
                        dialog.cancel()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null && data.data!=null){
                selectedimage = data.data!!
                binding.circleImageView.setImageURI(selectedimage)

        }

    }
}