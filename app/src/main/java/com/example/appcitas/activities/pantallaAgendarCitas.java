package com.example.appcitas.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;
import com.example.appcitas.databinding.ActivityPantallaAgendarCitasBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class pantallaAgendarCitas extends AppCompatActivity {

    ActivityPantallaAgendarCitasBinding binding;
    Map<String, Integer> mapaMascotas = new HashMap<>();
    Map<String, Integer> mapaVeterinarios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaAgendarCitasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("VetAppPrefs", MODE_PRIVATE);
        int clienteId = sharedPreferences.getInt("clienteId", -1);

        if (clienteId == -1) {
            Toast.makeText(this, "No se pudo obtener el ID del cliente", Toast.LENGTH_SHORT).show();
            return;
        }
        cargarMascotas(clienteId);
        cargarVeterinarios();

        // Configuración del datepicker y timepicker
        binding.etFecha.setFocusable(false);
        binding.etFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                binding.etFecha.setText(fecha);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        binding.etHora.setFocusable(false);
        binding.etHora.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String hora = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                binding.etHora.setText(hora);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.show();
        });

        // Configuración del botón para confirmar la cita
        binding.btnConfirmarCita.setOnClickListener(v -> {
            String motivo = binding.etMotivo.getText().toString().trim();
            String fecha = binding.etFecha.getText().toString().trim();
            String hora = binding.etHora.getText().toString().trim(); // Opcional, si la guardas como string aparte
            String fechaCompleta = fecha; // Puedes concatenar fecha y hora si deseas

            int posMascota = binding.spinnerMascotas.getSelectedItemPosition();
            int posVeterinario = binding.spinnerVeterinarios.getSelectedItemPosition();

            if (posMascota == AdapterView.INVALID_POSITION || posVeterinario == AdapterView.INVALID_POSITION ||
                    motivo.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int idDoctor = obtenerIdDoctorPorNombre(binding.spinnerVeterinarios.getSelectedItem().toString());
            int idMascota = obtenerIdMascotaPorNombre(binding.spinnerMascotas.getSelectedItem().toString());
            Log.e("ID del doctor", String.valueOf(idDoctor));
            Log.e("ID de la mascota", String.valueOf(idMascota));

            JSONObject citaJson = new JSONObject();
            try {
                citaJson.put("fecha", fechaCompleta);
                citaJson.put("motivo", motivo);
                citaJson.put("idDoctor", idDoctor);
                citaJson.put("idMascota", idMascota);
                citaJson.put("estado", true); // se puede mantener así, si aún no usas strings
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            String url = "http://10.0.2.2:5001/api/citas";

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    citaJson,
                    response -> {
                        Toast.makeText(this, "Cita registrada correctamente", Toast.LENGTH_LONG).show();
                        finish();
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Log.e("CitaError", errorMsg);
                            Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                        } else {
                            error.printStackTrace();
                            Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_LONG).show();
                        }
                    });

            Volley.newRequestQueue(this).add(request);
        });


        // Configuración del bottom navbar
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(pantallaAgendarCitas.this, pantallaMascotas.class);
                startActivity(intent);
            } else if (itemId == R.id.historial) {
                Intent intent = new Intent(pantallaAgendarCitas.this, pantallaHistorial.class);
                startActivity(intent);
            }
            return true;
        });
    }
    private int obtenerIdMascotaPorNombre(String nombre) {
        return mapaMascotas.getOrDefault(nombre, -1);
    }

    private int obtenerIdDoctorPorNombre(String nombre) {
        return mapaVeterinarios.getOrDefault(nombre, -1);
    }
    private void cargarMascotas(int clienteId) {
        String url = "http://10.0.2.2:5001/api/mascotas/cliente/" + clienteId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<String> nombresMascotas = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            String nombre = obj.getString("nombre");
                            int id = obj.getInt("idMascota");
                            nombresMascotas.add(nombre);
                            mapaMascotas.put(nombre, id);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, nombresMascotas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerMascotas.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar mascotas", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e("MascotasError", errorMsg);
                        Toast.makeText(this, "Error al cargar mascotas: " + errorMsg, Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void cargarVeterinarios() {
        String url = "http://10.0.2.2:5001/api/doctores";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<String> nombresVeterinarios = new ArrayList<>();
                        List<String> especialidades = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            nombresVeterinarios.add(obj.getString("nombre"));
                            especialidades.add(obj.getString("especialidad"));
                            int id = obj.getInt("idDoctor");
                            String nombre = obj.getString("nombre");
                            mapaVeterinarios.put(nombre, id);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, nombresVeterinarios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerVeterinarios.setAdapter(adapter);

                        binding.spinnerVeterinarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                binding.etEspecialidad.setText(especialidades.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar doctores", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e("DoctoresError", errorMsg);
                        Toast.makeText(this, "Error al cargar doctores: " + errorMsg, Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}