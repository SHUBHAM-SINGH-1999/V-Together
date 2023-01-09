package com.example.chat_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat_app.R

class Avatar(var list:ArrayList<Int>): RecyclerView.Adapter<Avatar.Viewholder>() {
    
     open class Viewholder(itemView: View):ViewHolder(itemView){
        var circle_avatar = itemView.findViewById<ImageView>(R.id.av)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        var inf = LayoutInflater.from(parent.context)
        var inflater = inf.inflate(R.layout.avatar_view,parent,false)
        return Viewholder(inflater)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.circle_avatar.setImageResource(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }
}