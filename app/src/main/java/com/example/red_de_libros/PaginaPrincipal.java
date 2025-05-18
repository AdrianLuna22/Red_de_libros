package com.example.red_de_libros;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import com.example.red_de_libros.Libro;
import android.text.Editable;
import android.text.TextWatcher;


public class PaginaPrincipal extends AppCompatActivity {
    private RecyclerView rvLibros;
    private LibroAdapter adapter;
    private FirebaseFirestore db;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Libro> listaOriginal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_pagina);

        // Inicializa vistas
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rvLibros = findViewById(R.id.rvLibros);
        searchView = findViewById(R.id.search_view);
        db = FirebaseFirestore.getInstance();

        // Configura RecyclerView
        rvLibros.setLayoutManager(new GridLayoutManager(this, 2));

        // Swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this::cargarLibros);

        // Búsqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarLibros(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarLibros(newText);
                return true;
            }
        });

        // Carga inicial
        cargarLibros();
    }

    private void cargarLibros() {
        db.collection("libros")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaOriginal.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Libro libro = doc.toObject(Libro.class);
                        if (libro != null) {
                            listaOriginal.add(libro);
                        }
                    }
                    if (adapter == null) {
                        adapter = new LibroAdapter(listaOriginal);
                        rvLibros.setAdapter(adapter);
                    } else {
                        adapter.actualizarLista(listaOriginal);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar libros: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void filtrarLibros(String texto) {
        List<Libro> filtrados = new ArrayList<>();
        for (Libro libro : listaOriginal) {
            if (libro.getTitulo().toLowerCase().contains(texto.toLowerCase()) ||
                    libro.getAutor().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(libro);
            }
        }
        adapter.actualizarLista(filtrados);
    }
}