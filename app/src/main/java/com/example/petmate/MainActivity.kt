package com.example.petmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.petmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Binding
    private lateinit var binding  : ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val navController = findNavController(R.id.fragment)

        // Se establce un controlador para el navegador
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }
}