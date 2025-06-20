package com.example.appcitas.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcitas.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    Button loginButton;
    RequestQueue requestQueue;
    TextView tvRegistrarse, tvWelcome;
    ImageView ivTogglePassword, logo;
    String loginUrl = "http://10.0.2.2:5001/api/clientes/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        tvWelcome = findViewById(R.id.tvWelcomeBack);
        logo = findViewById(R.id.logoImageView);

        // Evento del botón de inicio de sesión
        loginButton.setOnClickListener(v -> {
            emailInput.setError(null);
            passwordInput.setError(null);

            // Obtener los valores de email y contraseña
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            boolean hasError = false;

            // Validación de contraseña vacía
            if (password.isEmpty()) {
                passwordInput.setError("Ingresa tu contraseña");
                passwordInput.startAnimation(shake);
                hasError = true;
            }

            // Validación de email vacío
            if (email.isEmpty()) {
                emailInput.setError("Ingresa tu correo");
                emailInput.startAnimation(shake);
                hasError = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Correo inválido");
                emailInput.startAnimation(shake);
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Corrige los errores", Toast.LENGTH_SHORT).show();
                return;
            }

            realizarLogin(email, password);
        });

        // Configurar evento del botón de registro
        tvRegistrarse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, pantallaRegistro.class);
            startActivity(intent);
            //overridePendingTransition(android.R.anim.accelerate_interpolator, android.R.anim.accelerate_decelerate_interpolator);
        });

        // Configurar evento del icono de mostrar/ocultar contraseña
        ivTogglePassword.setOnClickListener(v -> {
            int selection = passwordInput.getSelectionEnd();  // Para mantener el cursor

            if (passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Mostrar contraseña
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_hide);
            } else {
                // Ocultar contraseña
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_show);
            }

            passwordInput.setTypeface(Typeface.DEFAULT);
            passwordInput.setSelection(selection);
        });

        // Animación del logo
        //Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        //logo.startAnimation(slideDown);

        // Animación de bienvenida
        //tvWelcome.startAnimation(slideDown);
        //Animation slideFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_fade_in);
        //logo.startAnimation(slideFadeIn);

        // Animación de bienvenida
        //tvWelcome.startAnimation(slideFadeIn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void realizarLogin(String email, String password) {
        // Crear objeto JSON con los datos del login
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("email", email);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                loginData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");

                        if (success) {
                            Snackbar.make(findViewById(android.R.id.content), "Inicio de sesión exitoso", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(this, pantallaMascotas.class));
                        } else {
                            String message = response.getString("message");
                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar.make(findViewById(android.R.id.content), "Error al procesar respuesta", Snackbar.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorJson = new JSONObject(errorBody);
                            String message = errorJson.optString("message", "Credenciales inválidas");

                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                                    .setAction("Reintentar", v -> realizarLogin(email, password))
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), "Error desconocido", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        error.printStackTrace();
                        Snackbar.make(findViewById(android.R.id.content), "Sin conexión con el servidor", Snackbar.LENGTH_LONG)
                                .setAction("Reintentar", v -> realizarLogin(email, password))
                                .show();
                    }
                }
        );

        requestQueue.add(request);
    }
}
