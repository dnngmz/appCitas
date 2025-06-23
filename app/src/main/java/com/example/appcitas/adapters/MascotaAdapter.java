package com.example.appcitas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appcitas.R;
import com.example.appcitas.models.Mascota;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private List<Mascota> mascotaList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
    public MascotaAdapter(List<Mascota> mascotaList, Context context, OnItemClickListener listener) {
        this.mascotaList = mascotaList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);

        holder.tvNombre.setText(mascota.getNombre());
        holder.tvEspecieValor.setText(mascota.getEspecie());
        holder.tvRazaValor.setText(mascota.getRaza());
        holder.tvEdadValor.setText(mascota.getEdad());

        // Cargar imagen desde URL con Glide
        Glide.with(context)
                .load(mascota.getImagen())
                .placeholder(R.drawable.ic_launcher_foreground) // imagen temporal
                .error(R.drawable.baseline_error_outline_24) // imagen si falla
                .into(holder.imageMascota);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(position));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEspecieValor, tvRazaValor, tvEdadValor;
        ImageView imageMascota;
        ImageButton btnEdit, btnDelete;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_name);
            tvEspecieValor = itemView.findViewById(R.id.tv_species_value);
            tvRazaValor = itemView.findViewById(R.id.tv_race_value);
            tvEdadValor = itemView.findViewById(R.id.tv_age_value);
            imageMascota = itemView.findViewById(R.id.image_animal);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}