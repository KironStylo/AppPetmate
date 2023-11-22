package com.example.petmate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petmate.databinding.ItemUserLayoutBinding
import com.example.petmate.model.User

class DatingAdapter(val context: Context, val list: ArrayList<User>): RecyclerView.Adapter<DatingAdapter.DatingViewHolder>(){
    inner class DatingViewHolder(val binding:ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context),
            parent, false));
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        // Nombre de la mascota
        holder.binding.textView4.text = list[position].pet.name;
        // Correo del usuario
        holder.binding.textView3.text = list[position].pet.breed;
        // Se carga la imagen del perro
        Glide.with(context).load(list[position].pet.image).into(holder.binding.userImage)
        // Para iniciar el chat con el otro usuario
        holder.binding.chat.setOnClickListener {
            val inte = Intent(context, com.example.petmate.activity.MessageActivity::class.java)
            inte.putExtra("userid", list[position].uid)
            context.startActivity(inte)
        }
    };
}