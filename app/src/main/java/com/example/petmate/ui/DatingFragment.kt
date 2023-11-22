package com.example.petmate.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.petmate.R
import com.example.petmate.adapter.DatingAdapter
import com.example.petmate.auth.LoginActivity
import com.example.petmate.databinding.FragmentDatingBinding
import com.example.petmate.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding;
    // Manejador de las cartas
    private lateinit var manager : CardStackLayoutManager;
    // Lista de usuarios en la base de datos
    private lateinit var lista:ArrayList<User>;
    //  Variables de Firebase
    private lateinit var firebaseDatabase: FirebaseDatabase;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDatingBinding.inflate(layoutInflater);

        // Se instancia primero la base de datos
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Obtenemos los datos que hay registrados en la base de datos
        getData()

        return binding.root;
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object:CardStackListener{
            // Todos los métodos para las cartas
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            // Arrastar la carta
            override fun onCardSwiped(direction: Direction?) {
                if(manager!!.topPosition == lista.size){
                    Toast.makeText(requireContext(), "Ultimo carta",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })
        // Propiedades de la carta
        manager.setVisibleCount(3);
        manager.setTranslationInterval(0.6f);
        manager.setScaleInterval(0.8f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
    }


    private fun getData() {
       firebaseDatabase.getReference("users")
           .addValueEventListener(object: ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   // Devuelve toda la información de la base de datos
                   Log.d("SHUBH","onDataChange: ${snapshot.toString()}")
                   if(snapshot.exists()){
                       // Lista de los usuarios
                       lista  = arrayListOf<User>()
                       val key = snapshot.key;
                       Log.d("Llave", key.toString());
                       for(data in snapshot.children){

                           val usuario = data.getValue(User::class.java)

                           // Se agrega el usuario a la lista
                           lista.add(usuario!!)
                       }
                       // Inicializar las cartas
                       init()

                       binding.cardStackView.layoutManager = manager;
                       binding.cardStackView.itemAnimator = DefaultItemAnimator();
                       binding.cardStackView.adapter = DatingAdapter(requireContext(),lista);


                       // Para que presente los usuarios en orden aleatorio
                       lista.shuffle()

                       binding.cardStackView.adapter = DatingAdapter(requireContext(),lista)
                   }else{
                       Toast.makeText(requireContext(),"Algo salió mal",Toast.LENGTH_SHORT).show()
                       // Devolver a Usuario a pantalla de Login
                       startActivity(Intent(requireContext(),LoginActivity::class.java));
                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
               }

           })

    }
}