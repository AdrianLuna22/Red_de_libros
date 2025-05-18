package com.example.red_de_libros;

import com.google.firebase.database.IgnoreExtraProperties;

public class Mensaje {
    private String usuarioId;
    private String nombreUsuario;
    private String texto;
    private long timestamp;

    // Constructor vacío requerido por Firebase
    public Mensaje() {}

    public Mensaje(String usuarioId, String nombreUsuario, String texto, long timestamp) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters (opcionales, pero recomendados para Firebase)
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
