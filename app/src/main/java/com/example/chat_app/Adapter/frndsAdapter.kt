package com.example.chat_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chat_app.model.frndslist

class frndsAdapter(var list:ArrayList<frndslist>,var listner:click):RecyclerView.Adapter<frndsAdapter.Viewholder>() {
    class Viewholder(itemView: View):ViewHolder(itemView){
        var avatar = itemView.findViewById<ImageView>(R.id.frndsavatar)
        var user_name = itemView.findViewById<TextView>(R.id.frndsusername)
        var button = itemView.findViewById<Button>(R.id.frndsbutton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        var inf = LayoutInflater.from(parent.context)
        var inflate = inf.inflate(R.layout.frndsitem,parent,false)
        var s = Viewholder(inflate)
        s.button.setOnClickListener {
            listner.clicked(list[s.adapterPosition].recevierid)
        }
        return s
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.user_name.text = list[position].name
        Glide.with(holder.itemView.context).load(list[position].image).into(holder.avatar)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface click{
    fun clicked( reciverid:String)
}