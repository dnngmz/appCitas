package com.example.appcitas;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcitas.databinding.ActivityPantallaAgendarCitasBinding;

public class pantallaAgendarCitas extends AppCompatActivity {

    ActivityPantallaAgendarCitasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPantallaAgendarCitasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
}