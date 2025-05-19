package com.example.red_de_libros;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarLibroActivity extends AppCompatActivity {

    private EditText etTitulo, etAutor, etAnio, etPortadaUrl;
    private Button btnSubirLibro;
    private ImageView ivPreview;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_libro);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Vincular vistas
        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        etAnio = findViewById(R.id.etAnio);
        etPortadaUrl = findViewById(R.id.etPortadaUrl);
        btnSubirLibro = findViewById(R.id.btnSubirLibro);
        ivPreview = findViewById(R.id.ivPreview);

        // Configurar ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo libro...");
        progressDialog.setCancelable(false);

        // Preview de imagen al cambiar URL
        etPortadaUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cargarPreviewImagen(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Subir libro a Firebase
        btnSubirLibro.setOnClickListener(v -> subirLibro());
    }

    private void cargarPreviewImagen(String url) {
        if (!url.isEmpty()) {
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_error_image)
                    .into(ivPreview);
        } else {
            ivPreview.setVisibility(View.GONE);
        }
    }

    private void subirLibro() {
        String titulo = etTitulo.getText().toString().trim();
        String autor = etAutor.getText().toString().trim();
        String añoStr = etAnio.getText().toString().trim();
        String portadaUrl = etPortadaUrl.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("Ingrese el título");
            return;
        }
        if (autor.isEmpty()) {
            etAutor.setError("Ingrese el autor");
            return;
        }
        if (añoStr.isEmpty()) {
            etAnio.setError("Ingrese el año");
            return;
        }

        int año;
        try {
            año = Integer.parseInt(añoStr);
        } catch (NumberFormatException e) {
            etAnio.setError("Año inválido");
            return;
        }

        progressDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        // Buscar el nombre del usuario en Firestore usando el UID
        db.collection("usuarios").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombreDueno = documentSnapshot.getString("nombre");

                        Map<String, Object> libro = new HashMap<>();
                        libro.put("titulo", titulo);
                        libro.put("autor", autor);
                        libro.put("año", año);
                        libro.put("duenoId", uid);
                        libro.put("duenoNombre", nombreDueno);
                        libro.put("portadaUrl", portadaUrl);

                        db.collection("libros").add(libro)
                                .addOnSuccessListener(documentReference -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Libro subido exitosamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Error al subir libro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "No se encontró información del usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
