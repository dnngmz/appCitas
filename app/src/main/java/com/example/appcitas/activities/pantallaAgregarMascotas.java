package com.example.appcitas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;
import com.example.appcitas.databinding.ActivityPantallaAgregarMascotasBinding;
import com.example.appcitas.databinding.ActivityPantallaHistorialBinding;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class pantallaAgregarMascotas extends AppCompatActivity {
    ActivityPantallaAgregarMascotasBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaAgregarMascotasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("VetAppPrefs", MODE_PRIVATE);
        int clienteId = sharedPreferences.getInt("clienteId", -1);

        Intent intent = getIntent();
        if (intent != null && "editar".equals(intent.getStringExtra("modo"))) {
            int idMascota = intent.getIntExtra("idMascota", -1);
            String nombre = intent.getStringExtra("nombre");
            String especie = intent.getStringExtra("especie");
            String raza = intent.getStringExtra("raza");
            String edad = intent.getStringExtra("edad");

            // Mostrar los valores en los EditText
            binding.etNombre.setText(nombre);
            binding.etEspecie.setText(especie);
            binding.etRaza.setText(raza);
            binding.etEdad.setText(edad);

            // Cambiar texto del botón
            binding.btnRegistrarMascota.setText("Actualizar mascota");

            // Cambiar el título de la actividad
            binding.tvTitulo.setText("Editar mascota");

            // Cambiar lógica del botón para editar
            binding.btnRegistrarMascota.setOnClickListener(v -> {
                String nuevoNombre = binding.etNombre.getText().toString().trim();
                String nuevaEspecie = binding.etEspecie.getText().toString().trim();
                String nuevaRaza = binding.etRaza.getText().toString().trim();
                String nuevaEdad = binding.etEdad.getText().toString().trim();

                if (nuevoNombre.isEmpty() || nuevaEspecie.isEmpty() || nuevaRaza.isEmpty() || nuevaEdad.isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nombre", nuevoNombre);
                    jsonBody.put("especie", nuevaEspecie);
                    jsonBody.put("raza", nuevaRaza);
                    jsonBody.put("edad", nuevaEdad);
                    jsonBody.put("imagen", ""); // por ahora sin imagen
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                String url = "http://10.0.2.2:5001/api/mascotas/" + idMascota;

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.PUT,
                        url,
                        jsonBody,
                        response -> {
                            Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Indica que fue exitoso
                            finish();
                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(this, "Error al actualizar mascota", Toast.LENGTH_LONG).show();
                        }
                );

                Volley.newRequestQueue(this).add(request);
            });

        } else {
            // Listener para el botón de registrar mascota
            binding.btnRegistrarMascota.setOnClickListener(v -> {
                String nombre = binding.etNombre.getText().toString().trim();
                String especie = binding.etEspecie.getText().toString().trim();
                String raza = binding.etRaza.getText().toString().trim();
                String edad = binding.etEdad.getText().toString().trim();

                if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edad.isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nombre", nombre);
                    jsonBody.put("especie", especie);
                    jsonBody.put("raza", raza);
                    jsonBody.put("edad", edad);
                    jsonBody.put("idCliente", clienteId);
                    jsonBody.put("imagen", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                String url = "http://10.0.2.2:5001/api/mascotas";

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,
                        response -> {
                            Toast.makeText(this, "Mascota registrada correctamente", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Indica que fue exitoso
                            finish();
                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(this, "Error al registrar mascota", Toast.LENGTH_LONG).show();
                        }
                );

                Volley.newRequestQueue(this).add(request);
            });
        }

        binding.tvCancelar.setOnClickListener(v -> {
            finish();
        });

    }
}