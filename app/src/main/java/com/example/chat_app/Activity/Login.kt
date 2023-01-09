package com.example.chat_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.example.chat_app.Fragments.LoginFragment
import com.example.chat_app.R
import com.example.chat_app.Fragments.Signupfragment
import com.example.chat_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var binding:ActivityLoginBinding
     var db=FirebaseAuth.getInstance()
    var dbid:String = db.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.title ="V-Together"

        if(db.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment()).commit()
        bottomNavigation()
    }

    fun bottomNavigation(){
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.login ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        LoginFragment()
                    ).commit()
                    true
                }
                R.id.signup ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        Signupfragment()
                    ).commit()
                    true
                }
                else->true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_singup,menu)
        return true
    }
}