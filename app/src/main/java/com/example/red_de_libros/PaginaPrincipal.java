package com.example.red_de_libros;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class PaginaPrincipal extends AppCompatActivity {
    private RecyclerView rvLibros;
    private LibroAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContetnView(R.layout.principal_pagina);

        db = FirebaseFirestore.getInstance();
        rvLibros = findViewById(R.id.rvLibros);
        rvLibros.setLayoutManager(new GridLayoutManager(this, 2));

        cargarLibros();
    }

    private void cargarLibros() {
        db.collection("libros")
                .get()
                .addOnSuccesListener(queryDocumentSnapshots -> {
                    List<Libro> libros = new ArrayList <>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Libro libro = doc.toObject(Libro.class);
                        libros.add(libro);
                    }
                    adapter = new LibroAdapter(libros);
                    rvLibros.setAdapter(adapter);
                });
    }
}
