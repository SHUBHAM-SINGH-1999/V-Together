package com.example.chat_app.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chat_app.Activity.Login
import com.example.chat_app.Activity.Userdata
import com.example.chat_app.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? =null
    private val binding get() = _binding!!
    lateinit var dialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.lgbtn.setOnClickListener {
             dialog = ProgressDialog(context)
            dialog.setMessage("Loading")
            dialog.setCancelable(false)
            dialog.show()

            login_fun()
        }


        return binding.root
    }

    private fun login_fun() {
        val login_name = binding.LoginName.text.toString()
        val pass_name = binding.LoginPass.text.toString()
        if(login_name.isNotEmpty() && pass_name.isNotEmpty()){
            Login().db.signInWithEmailAndPassword(login_name,pass_name).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(activity,"User login successfully",Toast.LENGTH_SHORT).show()
                    val intent =Intent(activity, Userdata::class.java)
                    dialog.cancel()
                    startActivity(intent)
                    activity?.finish()
                }
            }.addOnFailureListener {
                dialog.cancel()
                Toast.makeText(activity, it.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
            }
        }
        else{
            dialog.cancel()
            Toast.makeText(activity,"Fill data",Toast.LENGTH_SHORT).show()
        }
    }

}