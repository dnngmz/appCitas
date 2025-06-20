package com.example.appcitas.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appcitas.R;
import com.example.appcitas.databinding.ActivityPantallaMascotasBinding;

public class pantallaMascotas extends AppCompatActivity {

    ActivityPantallaMascotasBinding binding;
//    FloatingActionButton agregarMascota = binding.agregarMascota;
//    agregarMascota.setImageTintList(ContextCompat.getColorStateList(this, R.color.my_secondary));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaMascotasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(pantallaMascotas.this, pantallaHistorial.class);
                startActivity(intent);
            }
            return true;
        });

        binding.AgendarCitas.setOnClickListener(Intent -> {;
            Intent intent = new Intent(pantallaMascotas.this, pantallaAgendarCitas.class);
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}