package com.example.chat_app.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.example.chat_app.Adapter.Avatar
import com.example.chat_app.Chat_adapter
import com.example.chat_app.Listner
import com.example.chat_app.R
import com.example.chat_app.Splash_Screen
import com.example.chat_app.databinding.ActivityMainBinding
import com.example.chat_app.model.frndslist
import com.example.chat_app.model.maindata
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), Listner {

    lateinit var binding:ActivityMainBinding
    lateinit var chatlist:ArrayList<maindata>
    lateinit var UserdataOnFirestore:FirebaseFirestore
    lateinit var realtime:FirebaseDatabase
    lateinit var imageStorage: FirebaseStorage
    lateinit var userid:String
    lateinit var inten:Intent
    lateinit var intentToFindFrnds:Intent
    lateinit var intenttorequstWindow:Intent
    lateinit var intenttochatwindow:Intent
    lateinit var listpasstoRequestWinow:ArrayList<frndslist>
    lateinit var shimmerFrameLayout: ShimmerFrameLayout


    override fun onRestart() {
        super.onRestart()
        GlobalScope.launch {
            loaddata()
            datasendtoAboutMe()
            datasendtofindfrnds()
            datasendtoRequestWindow()
        }
    }

    private fun datasendtofindfrnds() {
        UserdataOnFirestore.collection("UserData").document(userid).get().addOnSuccessListener{
            var name = it.data!!["name"].toString()
            var tag = it.data!!["tag"].toString()
            var image = it.data!!["image"].toString()
            intentToFindFrnds.putExtra("nametof",name)
            intentToFindFrnds.putExtra("imagetof",image)
        }
    }

    private fun datasendtoAboutMe() {
        UserdataOnFirestore.collection("UserData").document(userid).get().addOnSuccessListener{
            var name = it.data!!["name"].toString()
            var tag = it.data!!["tag"].toString()
            var image = it.data!!["image"].toString()
            inten.putExtra("name",name)
            inten.putExtra("tag",tag)
            inten.putExtra("image",image)
            intenttochatwindow.putExtra("myimage",image)
        }
    }

    private fun datasendtoRequestWindow() {
        listpasstoRequestWinow.clear()
        realtime.getReference().child("Request").child(userid).get().addOnSuccessListener {
            if(it.exists()) {
                it.children.forEach {
                    var name = it.child("name").value.toString()
                    var recevierid = it.child("recevierid").value.toString()
                    var image = it.child("image").value.toString()
                    listpasstoRequestWinow.add(frndslist(name,image,recevierid))
                }
                intenttorequstWindow.putParcelableArrayListExtra("list",listpasstoRequestWinow)
            }else{
                intenttorequstWindow.putParcelableArrayListExtra("list",listpasstoRequestWinow)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.title = "V-Together"

        shimmerFrameLayout = findViewById(R.id.shimmer)
        UserdataOnFirestore= FirebaseFirestore.getInstance()
        imageStorage = FirebaseStorage.getInstance()
        userid = FirebaseAuth.getInstance().uid.toString()
        realtime= FirebaseDatabase.getInstance()
        chatlist= ArrayList()
        listpasstoRequestWinow= ArrayList()

        inten = Intent(this,AboutMe::class.java)
        intentToFindFrnds = Intent(this, FindFrnds::class.java)
        intenttorequstWindow = Intent(this,Request_Window::class.java)
        intenttochatwindow = Intent(this,batein::class.java)

        shimmerFrameLayout.startShimmer()


        GlobalScope.launch {
            loaddata()
            datasendtoAboutMe()
            datasendtofindfrnds()
            datasendtoRequestWindow()
        }




        binding.ChatView.adapter = Chat_adapter(chatlist,this)
        binding.ChatView.layoutManager = LinearLayoutManager(this)

        Handler().postDelayed({
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
            binding.addfriends.visibility = View.VISIBLE
        },3000)

    }



    private fun loaddata() {
        realtime.getReference().child("Rooms").child(userid).get().addOnSuccessListener {
            chatlist.clear()
            it.children.forEach {
                var id = it.child("ReciverId").value.toString()
                UserdataOnFirestore.collection("UserData").document(id).get().addOnSuccessListener {
                    var name = it.data!!["name"].toString()
                    var image = it.data!!["image"].toString()
                    var tag = it.data!!["tag"].toString()
                    chatlist.add(maindata(id,name,image,tag))
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    binding.ChatView.visibility = View.VISIBLE
                    binding.ChatView.adapter?.notifyDataSetChanged()
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainacitivity,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id== R.id.logoutMenu){
            Login().db.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        if(id== R.id.findfrnds){
            startActivity(intentToFindFrnds)
        }

        if(id== R.id.aboutme){
            startActivity(inten)
        }

        if(id== R.id.friendsrequest){
            startActivity(intenttorequstWindow)
        }
        return true
    }

    override fun click(name:String,Reciverid:String,image:String) {
        intenttochatwindow.putExtra("sendername",name)
        intenttochatwindow.putExtra("senderid",Reciverid)
        intenttochatwindow.putExtra("senderimage",image)
        startActivity(intenttochatwindow)
    }

    override fun delete(Reciverid: String,pos:Int,name:String) {
        AlertdialogBox(Reciverid,pos,name)
    }

    private fun AlertdialogBox(Reciverid: String,pos:Int,name:String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("DeleteChat")
        //set message for alert dialog
        builder.setMessage("Do you want to delete $name!!")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            deleteuser(Reciverid,pos,name)
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(applicationContext,"User not deleted",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun deleteuser(Reciverid: String,pos:Int,name:String){
        var reciverid = userid
        var SenderId = Reciverid

        var roomid = "$reciverid&&$SenderId"
        var roomidImage = "$SenderId&&$reciverid"

        chatlist.removeAt(pos)
        binding.ChatView.adapter?.notifyDataSetChanged()

        GlobalScope.launch{
            //Delete from own room
            realtime.getReference().child("Rooms").child(userid).child(roomid).removeValue()

            //Delete from sender room
            realtime.getReference().child("Rooms").child(SenderId).child(roomidImage).removeValue()
        }
    }

}
