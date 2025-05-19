package com.example.red_de_libros;

import java.util.List;

public class Chat {
    private String id;
    private List<String> participantes;
    private String nombreOtroUsuario;
    private String tituloLibro;
    private String ultimoMensaje;

    // Constructor vacío necesario para Firestore
    public Chat() {}

    // Constructor completo
    public Chat(String id, List<String> participantes, String nombreOtroUsuario,
                String tituloLibro, String ultimoMensaje) {
        this.id = id;
        this.participantes = participantes;
        this.nombreOtroUsuario = nombreOtroUsuario;
        this.tituloLibro = tituloLibro;
        this.ultimoMensaje = ultimoMensaje;
    }

    // Getters y setters
    public String getId() { return id; }
    public List<String> getParticipantes() { return participantes; }
    public String getNombreOtroUsuario() { return nombreOtroUsuario; }
    public String getTituloLibro() { return tituloLibro; }
    public String getUltimoMensaje() { return ultimoMensaje; }

    public void setId(String id) { this.id = id; }
    public void setParticipantes(List<String> participantes) { this.participantes = participantes; }
    public void setNombreOtroUsuario(String nombreOtroUsuario) { this.nombreOtroUsuario = nombreOtroUsuario; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }
    public void setUltimoMensaje(String ultimoMensaje) { this.ultimoMensaje = ultimoMensaje; }
}