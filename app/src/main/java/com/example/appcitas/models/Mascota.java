package com.example.appcitas.models;

public class Mascota {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String edad;
    private String imagen; // URL de la imagen

    public Mascota(int id, String nombre, String especie, String raza, String edad, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.imagen = imagen;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public String getEdad() { return edad; }
    public String getImagen() { return imagen; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza) { this.raza = raza; }
    public void setEdad(String edad) { this.edad = edad; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}