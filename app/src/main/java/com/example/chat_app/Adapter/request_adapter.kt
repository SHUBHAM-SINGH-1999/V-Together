package com.example.chat_app

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
import com.squareup.picasso.Picasso

class request_adapter(var list:ArrayList<frndslist>,var listner:clicked):RecyclerView.Adapter<request_adapter.viewholder>() {

    class viewholder(itemView: View):ViewHolder(itemView){
        var name=itemView.findViewById<TextView>(R.id.name1)
        var decline_btn = itemView.findViewById<Button>(R.id.decline)
        var confirm_btn = itemView.findViewById<Button>(R.id.confirm)
        var image = itemView.findViewById<ImageView>(R.id.image1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        var inf = LayoutInflater.from(parent.context)
        var inflate = inf.inflate(R.layout.requstwindow_items,parent,false)
        var v= viewholder(inflate)

        v.decline_btn.setOnClickListener {
            var pos = v.adapterPosition
            listner.delete(list[pos].recevierid,pos)
        }
        v.confirm_btn.setOnClickListener {
            var pos = v.adapterPosition
            listner.confirm(list[pos].name,list[pos].recevierid,pos,list[pos].image)
        }
        return v
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.name.text=list[position].name
        Glide.with(holder.itemView.context).load(list[position].image).into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface clicked{
    fun delete(reciverid:String,pos:Int)
    fun confirm(SenderName:String,SenderId:String, pos:Int,Image:String)
}