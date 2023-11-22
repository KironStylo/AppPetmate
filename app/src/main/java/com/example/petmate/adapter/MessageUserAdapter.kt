package com.example.petmate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petmate.databinding.User2ItemLayoutBinding
import com.example.petmate.model.User

class MessageUserAdapter(val context: Context, val list:ArrayList<User>) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding: User2ItemLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(User2ItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }


    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        // Cargar la imagen del usuario en la lista de los contactos
        Glide.with(context).load(list[position].pet.image).into(holder.binding.userImage);

        // Se  coloca el nombre del usuaroi
        holder.binding.userName.text  = list[position].pet.name;
    }
}