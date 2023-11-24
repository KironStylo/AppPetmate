package com.example.petmate.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petmate.activity.MessageActivity
import com.example.petmate.databinding.User2ItemLayoutBinding
import com.example.petmate.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageUserAdapter(val context: Context, val list:ArrayList<String>, val chatKey: List<String>) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding: User2ItemLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(User2ItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }


    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {

        // Referencia de los usuarios

        Log.d("Se obtiene el UID", list[position]);
        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            Log.d("CHAT USUARIO", snapshot.getValue(User::class.java).toString())

                            // Se obtiene el usuario a través de su UID
                            val data = snapshot.getValue(User::class.java)

                            // Cargar la imagen del usuario en la lista de los contactos
                            Glide.with(context).load(data!!.pet.image).into(holder.binding.userImage);

                            // Se  coloca el nombre del perro
                            holder.binding.userName.text  = data.pet.name;
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,"Hubo un error", Toast.LENGTH_SHORT).show()
                    }

                }
            )

        holder.itemView.setOnClickListener {
            val inte = Intent(context, MessageActivity::class.java);
            inte.putExtra("chat_id", chatKey[position]);
            inte.putExtra("userId", list[position]);

            context.startActivity(inte);
        }

    }
}