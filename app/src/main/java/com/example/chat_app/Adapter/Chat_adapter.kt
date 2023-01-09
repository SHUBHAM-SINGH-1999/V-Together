package com.example.chat_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chat_app.model.maindata

class Chat_adapter(var list:ArrayList<maindata>,var listner:Listner): RecyclerView.Adapter<Chat_adapter.viewholder>() {

    class viewholder(itemView: View): ViewHolder(itemView){
        var avater = itemView.findViewById<ImageView>(R.id.avatar)
        var name = itemView.findViewById<TextView>(R.id.name)
        var tag = itemView.findViewById<TextView>(R.id.tag)
        var deletebutton = itemView.findViewById<ImageView>(R.id.deleteView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        var inf = LayoutInflater.from(parent.context)
        var inflate = inf.inflate(R.layout.chat_card,parent,false)
        var vie = viewholder(inflate)
        inflate.setOnClickListener {
            var pos = vie.adapterPosition
            listner.click(list[pos].name,list[pos].reciverid,list[pos].image)
        }
        vie.deletebutton.setOnClickListener {
            var pos = vie.adapterPosition
            listner.delete(list[pos].reciverid,pos,list[pos].name)
        }
        return  vie
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.name.text = list[position].name
        holder.tag.text = list[position].tag
        Glide.with(holder.itemView.context).load(list[position].image).into(holder.avater)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface Listner{
    fun click(name:String,Reciverid:String,image:String)
    fun delete(Reciverid:String,pos:Int,name:String)
}

