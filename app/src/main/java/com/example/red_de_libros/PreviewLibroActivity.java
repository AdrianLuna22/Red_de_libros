package com.example.red_de_libros;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ✅ Usa esta Toolbar

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class PreviewLibroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String duenoId;
    private Button btnEnviarMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_libro);

        // Inicializar Firebase
        btnEnviarMensaje = findViewById(R.id.btnEnviarMensaje);
        mAuth = FirebaseAuth.getInstance();

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
        duenoId = getIntent().getStringExtra("duenoId");
        String url = getIntent().getStringExtra("url");

        // Mostrar datos
        tvTitulo.setText(titulo);
        tvAutor.setText("Autor: " + autor);
        tvAnio.setText("Año: " + anio);
        tvDueno.setText("Publicado por: " + dueno);

        btnEnviarMensaje.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                if (mAuth.getCurrentUser().getUid().equals(duenoId)) {
                    Toast.makeText(this, "Eres el dueño de este libro. No puedes enviarte mensajes a ti mismo.", Toast.LENGTH_SHORT).show();
                } else {
                    abrirChatConDueno(duenoId, dueno, titulo);
                }
            } else {
                Toast.makeText(this, "Debes iniciar sesión para enviar mensajes", Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivPortada);
    }

    private void abrirChatConDueno(String duenoId, String nombreDueno, String tituloLibro) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("duenoId", duenoId);
        intent.putExtra("nombreDueno", nombreDueno);
        intent.putExtra("tituloLibro", tituloLibro);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Regresa a la pantalla anterior
        return true;
    }
}
