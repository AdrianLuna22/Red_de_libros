package com.example.red_de_libros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import com.example.red_de_libros.Libro;
import android.text.Editable;
import android.text.TextWatcher;


public class PaginaPrincipal extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView rvLibros;
    private LibroAdapter adapter;
    private FirebaseFirestore db;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Libro> listaOriginal = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_pagina);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Maneja los clicks en el menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_agregar_libro) {
                startActivity(new Intent(this, AgregarLibroActivity.class));
            } else if (id == R.id.menu_chat) {
                startActivity(new Intent(this, ChatActivity.class));
            } else if (id == R.id.menu_cerrar_sesion) {
                cerrarSesion();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_principal, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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