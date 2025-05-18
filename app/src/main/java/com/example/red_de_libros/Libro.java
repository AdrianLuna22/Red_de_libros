package com.example.red_de_libros;

public class Libro {
    private String titulo, autor, duenoId;
    private int año;

    // Constructor
    public  Libro(String titulo) {
        this.titulo = titulo;
    }

    // Getter para el titulo (IMPORTANTE)
    public String getTitulo() {
        return titulo;
    }

    // Setter (opcional)
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
