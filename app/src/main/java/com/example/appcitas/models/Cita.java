package com.example.appcitas.models;

public class Cita {
    private int idCita;
    private String fecha;
    private String motivo;
    private boolean estado;
    private Doctor doctor;
    private Mascota mascota;

    // Getters y setters

    public int getIdCita() { return idCita; }
    public String getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public boolean isEstado() { return estado; }
    public Doctor getDoctor() { return doctor; }
    public Mascota getMascota() { return mascota; }

    public static class Doctor {
        private String nombre;
        private String especialidad;

        public String getNombre() { return nombre; }
        public String getEspecialidad() { return especialidad; }
    }

    public static class Mascota {
        private String nombre;
        public String getNombre() { return nombre; }
    }
}