package com.example.appcitas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    Button loginButton;
    RequestQueue requestQueue;
    TextView resultadoText;
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
        //resultadoText = findViewById(R.id.textResultado);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            realizarLogin(email, password);
        });

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
                            Toast.makeText(getApplicationContext(), "Login exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí puedes redirigir a la siguiente pantalla
                        } else {
                            String msg = response.getString("message");
                            Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al leer respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("LOGIN_ERROR", "Error en la solicitud: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(request); // Asegúrate de tener inicializado `requestQueue`
    }
}
