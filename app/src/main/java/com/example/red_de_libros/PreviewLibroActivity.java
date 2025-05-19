package com.example.red_de_libros;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ✅ Usa esta Toolbar

import com.bumptech.glide.Glide;

public class PreviewLibroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_libro);

        // Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Vista previa");
        }

        // Referencias de vistas
        ImageView ivPortada = findViewById(R.id.ivPreviewPortada);
        TextView tvTitulo = findViewById(R.id.tvPreviewTitulo);
        TextView tvAutor = findViewById(R.id.tvPreviewAutor);
        TextView tvAnio = findViewById(R.id.tvPreviewAnio);
        TextView tvDueno = findViewById(R.id.tvPreviewDueno);

        // Obtener datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String autor = getIntent().getStringExtra("autor");
        int anio = getIntent().getIntExtra("anio", 0);
        String dueno = getIntent().getStringExtra("dueno");
        String url = getIntent().getStringExtra("url");

        // Mostrar datos
        tvTitulo.setText(titulo);
        tvAutor.setText("Autor: " + autor);
        tvAnio.setText("Año: " + anio);
        tvDueno.setText("Publicado por: " + dueno);

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivPortada);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Regresa a la pantalla anterior
        return true;
    }
}
