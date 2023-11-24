package com.example.petmate.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petmate.R
import com.example.petmate.model.MessageModel
import com.example.petmate.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter(val context: Context, val list: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // Dirección del mensaje de donde viene
    val MSG_TYPE_RIGHT = 0;
    val MSG_TYPE_LEFT = 1;
    inner class  MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text = itemView.findViewById<TextView>(R.id.messageText);
        val image = itemView.findViewById<ImageView>(R.id.senderImage);

    }


    override fun getItemViewType(position: Int): Int {
        Log.d("VISTA MESSAGE", list[position].senderId.toString())
        return if(list[position].senderId == FirebaseAuth.getInstance().currentUser!!.uid){
            MSG_TYPE_RIGHT
        }
        else{
            MSG_TYPE_LEFT
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_receiver_message,parent,false));
        }else{
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message,parent,false));

        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.text.text = list[position].message

        Log.d("MENSAJE", list[position].message!!);

        Log.d("Info",list[position].senderId!!)

        // Referencia de los usuarios
        Log.d("Message Adapter","El token del usuario es" + list[position].senderId)
        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position].senderId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            Log.d("Message Adapter","Existe el Snapshot")
                            // Se obtiene el usuario a través de su UID
                            val data = snapshot.getValue(User::class.java)
                            Log.d("Message Adapter","Datos de usuario"+snapshot.getValue(User::class.java))



                            // Cargar la imagen del usuario
                            Glide.with(context).load(data!!.pet.image).placeholder(R.drawable.shiba).into(holder.image);

                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,"Hubo un error", Toast.LENGTH_SHORT).show()
                    }

                }
            )
    }
}