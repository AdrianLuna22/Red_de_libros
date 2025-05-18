package com.example.red_de_libros;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipal extends AppCompatActivity {
    private RecyclerView rvLibros;
    private LibroAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_pagina); // Corregido el nombre del método

        db = FirebaseFirestore.getInstance();
        rvLibros = findViewById(R.id.rvLibros);
        rvLibros.setLayoutManager(new GridLayoutManager(this, 2));

        cargarLibros();
    }

    private void cargarLibros() {
        db.collection("libros")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Libro> libros = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Libro libro = doc.toObject(Libro.class);
                        libros.add(libro);
                    }
                    adapter = new LibroAdapter(libros);
                    rvLibros.setAdapter(adapter);
                });
    }
}
