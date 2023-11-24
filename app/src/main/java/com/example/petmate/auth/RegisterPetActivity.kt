package com.example.petmate.auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petmate.MainActivity
import com.example.petmate.R
import com.example.petmate.databinding.ActivityRegisterPetBinding
import com.example.petmate.model.User
import com.example.petmate.utils.Config
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class RegisterPetActivity : AppCompatActivity() {

    // Binding para la actividad de registro de mascota
    private lateinit var binding: ActivityRegisterPetBinding;

    private lateinit var dialog :  Dialog;

    private lateinit var razas : List<String>;

    private var selectedText:String ="" ;

    // Variables de Firebase
    private lateinit var firebaseDatabase: FirebaseDatabase;
    private lateinit var firebaseAuth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Poner la vista

        binding = ActivityRegisterPetBinding.inflate(layoutInflater);
        setContentView(binding.root);

        // Instanciar variables de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = Firebase.auth;


        // Vista de texto
        val razasTxt : TextView = binding.raza;



        // Leer el archivo de JSON para inicializar la lista
        read_json();

        // Poner accion
        razasTxt.setOnClickListener{
            dialog = Dialog(this);

            // Poner un dialogo personalizado
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // Una altura y anchura personalizada
            dialog.window!!.setLayout(650,800);

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            // Mostrar el dialogo
            dialog.show();

            // Inicializar  y asignar las variables
            val editText: EditText = dialog.findViewById(R.id.edit_text);
            val listView : ListView = dialog.findViewById(R.id.list_view);

            // Inicializar el adapter de listas
            val adapter : ArrayAdapter<*> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1,razas);

            // Registrar mascota boton
            val registrarMascota : Button = binding.continuar;

            // Establecer el adaptador
            listView.adapter = adapter;


            // Buscador en vivo
            editText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Se va filtrando el arreglo a medida que escriban
                    adapter.filter.filter(p0);
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            listView.setOnItemClickListener(){parent,view,position,id->
                // Elegir la raza encontrada
                razasTxt.text = parent.getItemAtPosition(position).toString();
                // Terminar dialogo
                dialog.dismiss();
            }

            // Listener para el radio button
            val radioGroup: RadioGroup = binding.radioGroup;

            radioGroup.setOnCheckedChangeListener{group,checkId->
                val radioButton: RadioButton = findViewById(checkId)

                // Seleccionar el texto del botón
                 selectedText = radioButton.text.toString();

            }

            // Continuar a la página principal
            registrarMascota.setOnClickListener {
                validateData();
            }


        }

    }

    private fun validateData(){
        if(binding.raza.text.toString().isEmpty() ||
            binding.txtEdad.text.toString().isEmpty() ||
            binding.txtPeso.text.toString().isEmpty() ||
            selectedText.toString().isEmpty()
            ){
            Toast.makeText(this,"Por favor llena tus datos",Toast.LENGTH_LONG).show();
        }else{
            saveData()
        }

    }

    private fun saveData() {
        Config.showDialog(this)

        val reference = firebaseDatabase.getReference("users").child(firebaseAuth.currentUser!!.uid)

        // Se colocan los nuevos datos del usuario a su mascota
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Obtener el usuario existente
                    val existingUser = snapshot.getValue(User::class.java)

                    // Edad y peso
                    var edad = binding.txtEdad.text.toString().toIntOrNull()
                    var peso = binding.txtPeso.text.toString().toIntOrNull()

                    // Raza
                    var raza = binding.raza.text.toString()

                    existingUser?.let {
                        it.pet.breed = raza
                        it.pet.age = edad ?: 0 // Handle null case, set default value if needed
                        it.pet.weight = peso ?: 0 // Handle null case, set default value if needed
                        it.pet.gender = selectedText
                    }

                    reference.setValue(existingUser).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this@RegisterPetActivity,"Usuarios enviados",Toast.LENGTH_SHORT).show();
                            startActivity(Intent(this@RegisterPetActivity,MainActivity::class.java))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading user data", error.toException())
            }
        })
    }

    private fun read_json() {
        var json: String? = null;
        // Manejar las excepciones

        try{
            // Abrir el archivo json
            val inputStream: InputStream = assets.open("dogs.json");
            json  = inputStream.bufferedReader().use{it.readText()};

            // Obtener el texto de json
            Log.d("Texto de JSON", json);

            // Parsear el JSON string
            val jsonData = JSONObject(json);

            // Obtener nombre de razas
            val razasArray = jsonData.getJSONArray("dogs");

            // Convertir el arreglo de json a una lista
            razas = List(razasArray.length()){index->razasArray.getString(index)};

            Log.d("Razas","$razas");

        }catch(e : IOException){

        }
    }
}