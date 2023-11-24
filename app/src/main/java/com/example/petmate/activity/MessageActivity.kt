package com.example.petmate.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petmate.adapter.MessageAdapter
import com.example.petmate.databinding.ActivityMessageBinding
import com.example.petmate.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // getData(intent.getStringExtra("chat_id"));

        verifyChatId()

        binding.imageView2.setOnClickListener {
            if (binding.mensaje.text!!.isEmpty()){
                Toast.makeText(this, "Ingresa un mensaje", Toast.LENGTH_SHORT).show()
            }else{
                storeData(binding.mensaje.text.toString())
            }
        }


    }

    private var senderId:String? = null
    private var chatId: String? = null
    private fun verifyChatId() {

        val receiverId = intent.getStringExtra("userId")
        senderId = FirebaseAuth.getInstance().currentUser!!.uid;

        chatId=senderId+receiverId
        val reverseChatId=receiverId+senderId;

        // Mensajes para Devs
        Log.d("Chat iniciado", chatId!!);


        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(chatId!!)){
                    getData(chatId);
                }else if(snapshot.hasChild(reverseChatId)){
                    chatId = reverseChatId;
                    getData(chatId);
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity,"Algo salió mal", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun getData(chatId: String?) {
        // Aqui tenia un chequeo de que si el chatId es vacio
        // Referencia de los usuarios
            Log.d("El número de chatID devuelto", chatId!!)
            FirebaseDatabase.getInstance().getReference("chats")
                .child(chatId!!).addValueEventListener(object:  ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = arrayListOf<MessageModel>();

                        Log.d("CHATS", "SE OBTIENE INFORMACIÓN DE CHATS")

                        for(show in snapshot.children){
                            Log.d("Mensajes", show.getValue(MessageModel::class.java).toString())
                            list.add(show.getValue(MessageModel::class.java)!!)
                        }
                        binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity,list)

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MessageActivity, error.message,Toast.LENGTH_SHORT).show()
                    }

                })

    }


    private fun storeData( msg: String) {

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"]=msg
        map["senderId"]=senderId!!
        map["currentTime"]=currentTime
        map["currentDate"]=currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
            if(it.isSuccessful){
                binding.mensaje.text=null//No se pa que sirve
                Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                // Por si acaso esto se puede quitar
                verifyChatId()
            }else{
                Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
            }
        }
    }
}