package com.example.appcitas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;
import com.example.appcitas.adapters.HistorialAdapter;
import com.example.appcitas.databinding.ActivityPantallaAgendarCitasBinding;
import com.example.appcitas.databinding.ActivityPantallaHistorialBinding;
import com.example.appcitas.models.Cita;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pantallaHistorial extends AppCompatActivity {

    ActivityPantallaHistorialBinding binding;
    List<Cita> listaCitas = new ArrayList<>();
    HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaHistorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (listaCitas.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvSinCitas.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvSinCitas.setVisibility(View.GONE);
        }

        // Configuración del RecyclerView
        adapter = new HistorialAdapter(listaCitas);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Obtener clienteId desde SharedPreferences
        int clienteId = getSharedPreferences("VetAppPrefs", MODE_PRIVATE).getInt("clienteId", -1);
        if (clienteId != -1) {
            cargarHistorial(clienteId);
        } else {
            Toast.makeText(this, "No se encontró el ID del cliente", Toast.LENGTH_SHORT).show();
        }

        // Configuración del BottomNavigationView
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(this, pantallaMascotas.class));
            }
            return true;
        });

        // Botón para ir a pantallaAgendarCitas
        binding.AgendarCitas.setOnClickListener(v -> {
            Intent intent = new Intent(pantallaHistorial.this, pantallaAgendarCitas.class);
            startActivity(intent);
        });
    }

    private void cargarHistorial(int clienteId) {
        String url = "http://10.0.2.2:5001/api/citas/cliente/" + clienteId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray citasArray = response.getJSONArray("data");
                        listaCitas.clear(); // Limpiar antes de agregar nuevas
                        for (int i = 0; i < citasArray.length(); i++) {
                            JSONObject obj = citasArray.getJSONObject(i);
                            Cita cita = new Gson().fromJson(obj.toString(), Cita.class);
                            listaCitas.add(cita);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar historial", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error al cargar historial", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}