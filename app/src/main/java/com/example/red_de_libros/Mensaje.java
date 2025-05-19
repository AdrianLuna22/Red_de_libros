package com.example.red_de_libros;

import java.util.Date;

public class Mensaje {
    private String emisorId;
    private String receptorId;
    private String texto;
    private Date timestamp;

    // Constructor vacío necesario para Firestore
    public Mensaje() {}

    // Getters y setters
    public String getEmisorId() { return emisorId; }
    public String getReceptorId() { return receptorId; }
    public String getTexto() { return texto; }
    public Date getTimestamp() { return timestamp; }

    public void setEmisorId(String emisorId) { this.emisorId = emisorId; }
    public void setReceptorId(String receptorId) { this.receptorId = receptorId; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}