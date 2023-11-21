package com.example.petmate.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.petmate.MainActivity
import com.example.petmate.R
import com.example.petmate.databinding.ActivityRegisterBinding
import com.example.petmate.model.Pet
import com.example.petmate.model.User
import com.example.petmate.utils.Config
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


class RegisterActivity : AppCompatActivity() {

    // Binding de actividad de registro
    private lateinit var binding: ActivityRegisterBinding;

    // Uri de la imagen
    private var imageUri: Uri? = null

    // Obtener la imagen de la galeria
    private val selectImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        imageUri = it;

        binding.petImage.setImageURI(imageUri);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Poner la vista
        binding = ActivityRegisterBinding.inflate(layoutInflater);
        setContentView(binding.root);

        // Datos
        val email: TextInputEditText = binding.fieldCorreo;
        val password: TextInputEditText = binding.fieldPassword;
        val nombreMascota: TextInputEditText = binding.fieldDog;

        // Mensaje
        val mensaje: TextView = binding.txtImagen;

        // Boton de continuar
        val btnContinuar: Button = binding.continuar;
        val btnImagen: CircleImageView = binding.petImage;

        // Crear un listener para la imagen
        btnImagen.setOnClickListener {
            selectImage.launch("image/*")
        }

        // Pone un mensaje en tiempo real
        nombreMascota.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mensaje.text = "Mi nombre es " + nombreMascota.text.toString();
            }

            override fun afterTextChanged(p0: Editable?) {
                if(nombreMascota.text.toString().isEmpty()){
                    mensaje.text = "Mi nombre es ...";
                }
            }

        })


        // Crear un listener para crear un usuario de autenticación
        btnContinuar.setOnClickListener {
            validateData(email.text.toString(),password.text.toString(),nombreMascota.text.toString());
        }
    }

    private fun validateData(email: String, password:String, petName: String){
        if(email.isEmpty()||password.isEmpty()||petName.isEmpty()){
            Toast.makeText(this, "Llena todas las casillas", Toast.LENGTH_LONG).show()
        }
        else if(!binding.condiciones.isChecked){
            Toast.makeText(this, "Acepta los terminos", Toast.LENGTH_LONG).show()
        }
        else{
            // Crear usuario en Firebase
            signUp(email,password);
            // Subir la imagen del usuario bajo su ID en Firebase
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this);

        // Crea una carpeta para almacenar imagenes del usuario
        val storageRef = LoginActivity.firebaseStorage.getReference("perfiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg");

        //Almcenar la imagen
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this, "No pudimos subir la imagen", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Config.hideDialog()
                Toast.makeText(this, "No pudimos encontrar la image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        val data = User(
            email = binding.fieldCorreo.text.toString(),
            pet = Pet(
                name = binding.fieldDog.text.toString(),
                image = imageUrl.toString(),
            )
        )

        // Almacener los datos
        LoginActivity.firebaseDatabase.getReference("users")
            .child(LoginActivity.firebaseAuth.currentUser!!.uid)
            .setValue(data).addOnCompleteListener{
                Config.hideDialog()
                if(it.isSuccessful){
                    Toast.makeText(this, "Usuario registrado",Toast.LENGTH_SHORT).show()
                    // Redirigir a la página de home
                    startActivity(Intent(this,RegisterPetActivity::class.java))
                }
                else{
                    Toast.makeText(this, "Usuario no pudo ser registrado",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun signUp(email:String,  password:String){
        LoginActivity.firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                // Continuar con el registro de usuario
                Toast.makeText(this, "Usuario creado!", Toast.LENGTH_SHORT).show();
                Log.d("Usuario Creado","Usuario Creado")
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show();
        }
    }
}