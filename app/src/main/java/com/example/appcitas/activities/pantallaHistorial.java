package com.example.appcitas.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcitas.R;
import com.example.appcitas.databinding.ActivityPantallaAgendarCitasBinding;
import com.example.appcitas.databinding.ActivityPantallaHistorialBinding;

public class pantallaHistorial extends AppCompatActivity {

    ActivityPantallaHistorialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaHistorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(pantallaHistorial.this, pantallaMascotas.class);
                startActivity(intent);
            }
            return true;
        });

        binding.AgendarCitas.setOnClickListener(Intent -> {;
            Intent intent = new Intent(pantallaHistorial.this, pantallaAgendarCitas.class);
            startActivity(intent);
        });

    }
}