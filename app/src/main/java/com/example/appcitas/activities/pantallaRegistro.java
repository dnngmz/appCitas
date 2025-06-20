package com.example.appcitas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;

import org.json.JSONException;
import org.json.JSONObject;

public class pantallaRegistro extends AppCompatActivity {

    Button buttonRegistrar;
    EditText editTextNombre, editTextEmail, editTextTelefono, editTextPassword, editTextConfirmarPassword;
    TextView tvIniciarSesion;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:5001/api/clientes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_registro);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmarPassword = findViewById(R.id.editTextConfirmarPassword);

        // Configurar evento del botón de iniciar sesión
        tvIniciarSesion.setOnClickListener(view -> {
            Intent intent = new Intent(pantallaRegistro.this, MainActivity.class);
            startActivity(intent);
        });

        buttonRegistrar.setOnClickListener(v -> {
            // Limpiar errores previos
            editTextNombre.setError(null);
            editTextEmail.setError(null);
            editTextTelefono.setError(null);
            editTextPassword.setError(null);
            editTextConfirmarPassword.setError(null);

            // Obtener valores
            String nombre = editTextNombre.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String telefono = editTextTelefono.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmarPassword = editTextConfirmarPassword.getText().toString().trim();

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            boolean hasError = false;

            // Validaciones con animación y errores
            if (nombre.isEmpty()) {
                editTextNombre.setError("Ingresa tu nombre");
                editTextNombre.startAnimation(shake);
                hasError = true;
            }

            if (email.isEmpty()) {
                editTextEmail.setError("Ingresa tu correo");
                editTextEmail.startAnimation(shake);
                hasError = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Correo inválido");
                editTextEmail.startAnimation(shake);
                hasError = true;
            }

            if (telefono.isEmpty()) {
                editTextTelefono.setError("Ingresa tu número");
                editTextTelefono.startAnimation(shake);
                hasError = true;
            } else if (telefono.length() < 7) {
                editTextTelefono.setError("Número muy corto");
                editTextTelefono.startAnimation(shake);
                hasError = true;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Ingresa una contraseña");
                editTextPassword.startAnimation(shake);
                hasError = true;
            } else if (password.length() < 6) {
                editTextPassword.setError("Mínimo 6 caracteres");
                editTextPassword.startAnimation(shake);
                hasError = true;
            }

            if (confirmarPassword.isEmpty()) {
                editTextConfirmarPassword.setError("Confirma la contraseña");
                editTextConfirmarPassword.startAnimation(shake);
                hasError = true;
            } else if (!password.equals(confirmarPassword)) {
                editTextConfirmarPassword.setError("Las contraseñas no coinciden");
                editTextConfirmarPassword.startAnimation(shake);
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Corrige los errores", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si todo está bien, realizar registro
            realizarRegistro(nombre, email, telefono, password);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void realizarRegistro (String nombre, String email, String telefono, String password) {
        // Crear objeto JSON con los datos
        JSONObject datosRegistro = new JSONObject();
        try {
            datosRegistro.put("nombre", nombre);
            datosRegistro.put("email", email);
            datosRegistro.put("telefono", telefono);
            datosRegistro.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear datos JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enviar solicitud
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                datosRegistro,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                        } else {
                            String msg = response.getString("message");
                            Toast.makeText(this, "Error: " + msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e("RegistroError", errorMsg);
                        Toast.makeText(getApplicationContext(), "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(request); // Asegúrate de haber inicializado requestQueue
    }
}