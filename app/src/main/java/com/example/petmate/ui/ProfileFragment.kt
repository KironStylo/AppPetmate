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
import com.example.petmate.databinding.FragmentProfileBinding
import com.example.petmate.model.User
import com.example.petmate.utils.Config
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    // Se necesita el binding
    private lateinit var binding : FragmentProfileBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        Config.showDialog(requireContext())

        // Se trae los datos
        LoginActivity.firebaseDatabase.getReference("users")
            .child(LoginActivity.firebaseAuth.currentUser!!.uid!!).get()
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
            LoginActivity.firebaseAuth.signOut();
            startActivity(Intent(requireContext(),LoginActivity::class.java));
            // Terminar fragmento
            requireActivity().finish()
        }



        return binding.root;
    }
}