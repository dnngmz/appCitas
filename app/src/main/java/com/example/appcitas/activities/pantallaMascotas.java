package com.example.appcitas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;
import com.example.appcitas.adapters.MascotaAdapter;
import com.example.appcitas.databinding.ActivityPantallaMascotasBinding;
import com.example.appcitas.models.Mascota;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pantallaMascotas extends AppCompatActivity {

    ActivityPantallaMascotasBinding binding;
    List<Mascota> listaMascotas;
    MascotaAdapter adapter;
    private ActivityResultLauncher<Intent> agregarMascotaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaMascotasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("VetAppPrefs", MODE_PRIVATE);
        int clienteId = sharedPreferences.getInt("clienteId", -1);

        if (clienteId == -1) {
            Toast.makeText(this, "No se pudo obtener el ID del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        agregarMascotaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        cargarMascotasDelCliente(clienteId, listaMascotas, adapter); // refrescar lista
                    }
                });

        // Eliminar fondo del BottomNavigationView
        binding.bottomNavigationView.setBackground(null);

        // Navegación inferior
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.historial) {
                startActivity(new Intent(this, pantallaHistorial.class));
            }
            return true;
        });

        // Botones flotantes
        binding.AgendarCitas.setOnClickListener(v -> {
            startActivity(new Intent(this, pantallaAgendarCitas.class));
        });

        binding.btnAgregarMascota.setOnClickListener(view -> {
            Intent intent = new Intent(pantallaMascotas.this, pantallaAgregarMascotas.class);
            agregarMascotaLauncher.launch(intent);
        });

        // Configuración del RecyclerView
        listaMascotas = new ArrayList<>();

        adapter = new MascotaAdapter(listaMascotas, pantallaMascotas.this, new MascotaAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Mascota mascota = listaMascotas.get(position);
                Intent intent = new Intent(pantallaMascotas.this, pantallaAgregarMascotas.class);
                intent.putExtra("modo", "editar");
                intent.putExtra("idMascota", mascota.getId());
                intent.putExtra("nombre", mascota.getNombre());
                intent.putExtra("especie", mascota.getEspecie());
                intent.putExtra("raza", mascota.getRaza());
                intent.putExtra("edad", mascota.getEdad());
                agregarMascotaLauncher.launch(intent);  // launcher para manejar el resultado
            }
            @Override
            public void onDeleteClick(int position) {
                Mascota mascota = listaMascotas.get(position);

                new AlertDialog.Builder(pantallaMascotas.this)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás segura de que quieres eliminar a " + mascota.getNombre() + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            eliminarMascota(mascota.getId(), position);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        binding.recyclerMascotas.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMascotas.setAdapter(adapter);

        cargarMascotasDelCliente(clienteId, listaMascotas, adapter);
    }
    private void cargarMascotasDelCliente(int clienteId, List<Mascota> lista, MascotaAdapter adapter) {
        String url = "http://10.0.2.2:5001/api/mascotas/cliente/" + clienteId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    lista.clear();
                    try {
                        JSONArray mascotasArray = response.getJSONArray("data");

                        for (int i = 0; i < mascotasArray.length(); i++) {
                            JSONObject obj = mascotasArray.getJSONObject(i);
                            Mascota mascota = new Mascota(
                                    obj.getInt("idMascota"),
                                    obj.getString("nombre"),
                                    obj.getString("especie"),
                                    obj.getString("raza"),
                                    obj.getString("edad"),
                                    obj.getString("URL")
                            );
                            lista.add(mascota);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error al cargar mascotas: " + error.toString(), Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void eliminarMascota(int idMascota, int position) {
        String url = "http://10.0.2.2:5001/api/mascotas/" + idMascota;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    listaMascotas.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error al eliminar mascota", Toast.LENGTH_LONG).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

}