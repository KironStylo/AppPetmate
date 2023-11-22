package com.example.petmate.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.petmate.R
import com.example.petmate.auth.LoginActivity
import com.example.petmate.auth.RegisterActivity
import com.example.petmate.auth.RegisterPetActivity
import com.example.petmate.databinding.FragmentProfileBinding
import com.example.petmate.model.User
import com.example.petmate.utils.Config
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    // Se necesita el binding
    private lateinit var binding : FragmentProfileBinding;

    // Variables de Firebase
    private lateinit var firebaseDatabase : FirebaseDatabase;
    private lateinit var firebaseAuth : FirebaseAuth;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        // Se inicializan las variables de Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = Firebase.auth;

        Config.showDialog(requireContext())

        // Se trae los datos
        firebaseDatabase.getReference("users")
            .child(firebaseAuth.currentUser!!.uid!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(User::class.java);

                    // Se coloca el correo del usuario
                    binding.correo.setText(data!!.email.toString());
                    // Se coloca el nombre de la mascota
                    binding.nombre.setText(data!!.pet.name.toString());
                    // Se coloca la edad de la mascota
                    binding.edad.setText(data!!.pet.age.toString());

                    val img = data.pet.image;
                    // Cargar la imagen del perro
                    Glide.with(requireContext()).load(img)
                        .placeholder(R.drawable.shiba).into(binding.userImage);

                    // Para mostrar que se está cargando los datos
                    Config.hideDialog();

                }
            }

        binding.logout.setOnClickListener{
            // Salir de la cuenta
            firebaseAuth.signOut();
            startActivity(Intent(requireContext(),LoginActivity::class.java));
            // Terminar fragmento
            requireActivity().finish()
        }

        binding.edit.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterPetActivity::class.java));
        }



        return binding.root;
    }
}