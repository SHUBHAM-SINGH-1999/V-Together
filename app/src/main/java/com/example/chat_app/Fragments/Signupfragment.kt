package com.example.chat_app.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chat_app.Activity.Login
import com.example.chat_app.R
import com.example.chat_app.databinding.FragmentSignupfragmentBinding


class Signupfragment : Fragment() {

    private var _binding:FragmentSignupfragmentBinding? =null
    private val binding get() = _binding!!
    lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentSignupfragmentBinding.inflate(inflater,container,false)

        dialog = ProgressDialog(context)
        dialog.setMessage("Loading")
        dialog.setCancelable(false)


        binding.signupBtn.setOnClickListener {
            dialog.show()
                go_to_login()
        }

        return binding.root
    }

    private fun go_to_login() {
        val name = binding.signupName.text.toString()
        val pass = binding.signupPass.text.toString()
        if(name.isNotEmpty()&&pass.isNotEmpty()){
            Login().db.createUserWithEmailAndPassword(name,pass).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(activity,"User created successfully",Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                    fragmentManager?.beginTransaction()?.replace(R.id.container, LoginFragment())?.commit()
                }
            }.addOnFailureListener {
                dialog.cancel()
                Toast.makeText(activity,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

}