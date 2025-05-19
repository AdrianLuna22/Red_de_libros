package com.example.red_de_libros;

public class Libro {
    private String titulo;
    private String autor;
    private String duenoNombre;
    private String duenoId;
    private int año;
    private String portadaUrl; // URL de Imgur

    public Libro() {} // Requerido para Firestore

    // Getters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getDuenoNombre() { return duenoNombre; }
    public String getDuenoId() { return duenoId; }
    public int getAño() { return año; }
    public String getPortadaUrl() { return portadaUrl; }
}
