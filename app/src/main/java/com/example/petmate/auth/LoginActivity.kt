package com.example.petmate.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myloadingbutton.MyLoadingButton
import com.example.petmate.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
    // Autenticadores de Firebase
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var authStateListener: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater);
        setContentView(binding.root);

        // Botones
        val btnLogin : Button = binding.loginBtn;
        val btnSignUp: Button = binding.signBtn;
        val txtEmail : TextView = binding.fieldCorreo;
        val txtPass : TextView = binding.fieldPassword;

        // Inicializar la variable de firebase
        firebaseAuth = Firebase.auth;

        // Iniciar sesión
        btnLogin.setOnClickListener()
        {
            Log.d("LoginActivity",txtEmail.text.toString()+" "+txtPass.text.toString())
            signIn(txtEmail.text.toString(),txtPass.text.toString());
        }
        // Crear una cuenta
        btnSignUp.setOnClickListener {
            Log.d("LoginActivity", "Crear una cuenta")
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Función para autenticar los datos
    private fun signIn(email: String, password: String)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password).
        addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val user = firebaseAuth.currentUser
                Toast.makeText(baseContext,user?.uid.toString(),Toast.LENGTH_SHORT).show()
                // Ir a la página de Main
            }
            else{
                Toast.makeText(baseContext,"Error de Email y/o Contraseña",Toast.LENGTH_SHORT).show()
            }
        }
    }
}