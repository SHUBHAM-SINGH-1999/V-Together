package com.example.chat_app.Activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.chat_app.databinding.ActivityAboutMeBinding
import com.example.chat_app.model.data_User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.example.chat_app.R


class AboutMe : AppCompatActivity() {

    lateinit var binding:ActivityAboutMeBinding
    lateinit var userid:String
    lateinit var UserdataOnFirestore:FirebaseFirestore
    lateinit var imageStorage: FirebaseStorage
    lateinit var seletfromgallary:Uri
    lateinit var clip:ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_me)
        supportActionBar?.title = "AboutMe"

        userid=FirebaseAuth.getInstance().uid.toString()
        UserdataOnFirestore= FirebaseFirestore.getInstance()
        imageStorage = FirebaseStorage.getInstance()

        clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager


        loadData()


        binding.removeProfileBtn.setOnClickListener {
            Toast.makeText(this,"Profile photo Removed",Toast.LENGTH_SHORT).show()
            removeProfile()
        }

        binding.myimage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.changedata.setOnClickListener {
            changedata()
        }

        binding.uniqueId.setOnClickListener {
            copy()
        }
    }

    private fun copy() {
        clip.setPrimaryClip(ClipData.newPlainText("copied",binding.uniqueId.text.toString()))
        Toast.makeText(this,"Copied",Toast.LENGTH_SHORT).show()
    }

    private fun changedata() {
        var username = binding.myname.text.toString()
        var usernickname =  binding.mytag.text.toString()
        if(username.isEmpty() ||usernickname.isEmpty()){
            Toast.makeText(this,"Enter the details",Toast.LENGTH_SHORT).show()
        }else{
                imageStorage.getReference("images/ProfileImage/$userid").putFile(seletfromgallary)
                imageStorage.reference.child("images/ProfileImage/$userid").downloadUrl.addOnSuccessListener {
                    var uri = it.toString()
                    val userdata = data_User(username, usernickname,uri)
                    UserdataOnFirestore.collection("UserData").document(userid).set(userdata)
                    Toast.makeText(this,"Profile Changed",Toast.LENGTH_SHORT).show()
        }
        }
    }

    private fun removeProfile() {

        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + resources.getResourcePackageName(R.drawable.img_4)
                    + '/' + resources.getResourceTypeName(R.drawable.img_4) + '/' + resources.getResourceEntryName(
                R.drawable.img_4)
        )
        Picasso.get().load(imageUri).into(binding.myimage)

        Log.d("shu", imageUri.toString())

        imageStorage.getReference().child("images/ProfileImage/default.png").downloadUrl.addOnSuccessListener{
            var uri = it.toString()
            UserdataOnFirestore.collection("UserData").document(userid).update("image",uri)

        }
        seletfromgallary=imageUri
    }

    private fun loadData() {
        var name = intent.getStringExtra("name").toString()
        var tag = intent.getStringExtra("tag").toString()
        var image = intent.getStringExtra("image").toString()

        Picasso.get().load(image).into(binding.myimage)
        binding.myname.setText(name)
        binding.mytag.setText(tag)
        binding.uniqueId.setText(userid)

        seletfromgallary= Uri.parse(image)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null) {
            seletfromgallary = data.data!!
            binding.myimage.setImageURI(seletfromgallary)

        }
    }
}