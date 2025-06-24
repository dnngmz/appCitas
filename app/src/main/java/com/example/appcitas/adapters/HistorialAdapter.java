package com.example.appcitas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcitas.R;
import com.example.appcitas.models.Cita;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.CitaViewHolder> {

    private List<Cita> listaCitas;

    public HistorialAdapter(List<Cita> listaCitas) {
        this.listaCitas = listaCitas;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);
        holder.tvFecha.setText("Fecha: " + cita.getFecha());
        holder.tvMascota.setText(cita.getMascota().getNombre());
        holder.tvVeterinario.setText(cita.getDoctor().getNombre());
        holder.tvMotivo.setText(cita.getMotivo());
        holder.tvEstado.setText(cita.isEstado() ? "Pendiente" : "Completado");
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMascota, tvVeterinario, tvMotivo, tvEstado;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvMascota = itemView.findViewById(R.id.tvMascota);
            tvVeterinario = itemView.findViewById(R.id.tvVeterinario);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}