package com.example.chat_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat_app.R
import com.example.chat_app.model.dataForChat
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class adapter_chat(var list:ArrayList<dataForChat>):RecyclerView.Adapter<ViewHolder>() {

    var ITEM_SEND = 1
    var ITEM_RECIVE = 0

    override fun getItemViewType(position: Int): Int {
        if(FirebaseAuth.getInstance().uid.toString().equals(list[position].reciverid)){
            return ITEM_SEND
        }
        else return ITEM_RECIVE
    }

    class senderViewholder(itemView: View) :ViewHolder(itemView){
        var Sendername = itemView.findViewById<TextView>(R.id.senderTv)
        var SenderAvatar = itemView.findViewById<CircleImageView>(R.id.SenderAvatar)
    }
    class ReciverViewholder(itemView: View) :ViewHolder(itemView){
        var Recivername = itemView.findViewById<TextView>(R.id.reciverTv)
        var ReciverAvatar = itemView.findViewById<CircleImageView>(R.id.reciveravatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==1){
            var r = LayoutInflater.from(parent.context).inflate(R.layout.senderchat,parent,false)
            return senderViewholder(r)
        }
        else{
            var r = LayoutInflater.from(parent.context).inflate(R.layout.reciverchat,parent,false)
            return ReciverViewholder(r)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder::class.java==senderViewholder::class.java){
            var k = holder as senderViewholder
            k.Sendername.text = list[position].message
            Picasso.get().load(list[position].image).into(k.SenderAvatar)
        }else{
            var k = holder as ReciverViewholder
            k.Recivername.text = list[position].message
            Picasso.get().load(list[position].image).into(k.ReciverAvatar)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}