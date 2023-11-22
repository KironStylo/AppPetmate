package com.example.petmate.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petmate.R
import com.example.petmate.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener {
            if (binding.mensaje.text!!.isEmpty()){
                Toast.makeText(this, "Ingresa un mensaje", Toast.LENGTH_SHORT).show()
            }else{
                sendMessage(binding.mensaje.text.toString())
            }
        }


    }

    private fun sendMessage(msg: String) {
        val receiverId = intent.getStringExtra("userid")
        val senderid = FirebaseAuth.getInstance().currentUser!!.uid;
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val chatId=senderid+receiverId

        // Mensajes para Devs
        Log.d("Chat iniciado", chatId);

        val map = hashMapOf<String, String>()
        map["mensaje"]=msg
        map["senderid"]=senderid!!
        map["currentTime"]=currentTime
        map["currentDate"]=currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId)
        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
                if(it.isSuccessful){
                    binding.mensaje.text=null//No se pa que sirve
                    Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                }
            }
    }
}