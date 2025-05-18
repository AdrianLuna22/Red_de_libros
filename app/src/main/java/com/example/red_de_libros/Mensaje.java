package com.example.red_de_libros;

public class Mensaje {
    private String usuarioId;
    private String nombreUsuario;
    private String texto;
    private long timestamp;

    // Constructor vacío necesario para Firebase
    public Mensaje() {}

    public Mensaje(String usuarioId, String nombreUsuario, String texto, long timestamp) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.texto = texto;
        this.timestamp = timestamp;
    }

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
}
