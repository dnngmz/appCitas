package com.example.appcitas.models;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String created_at;
    private String updated_at;

    public Cliente() {
        // Constructor vacío requerido para deserialización
    }

    public Cliente(int idCliente, String nombre, String email, String password, String telefono, String created_at, String updated_at) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters y Setters

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}