package com.example.red_de_libros;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String tituloLibro;
    private String duenoId;
    private String nombreDueno;
    private RecyclerView rvMensajes;
    private EditText etMensaje;
    private ImageButton btnEnviar;
    private MensajeAdapter adapter;
    private Toolbar toolbar;
    private List<Mensaje> listaMensajes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializar Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Obtener datos del intent
        duenoId = getIntent().getStringExtra("duenoId");
        nombreDueno = getIntent().getStringExtra("nombreDueno");
        tituloLibro = getIntent().getStringExtra("tituloLibro");

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chat con " + nombreDueno);
            getSupportActionBar().setSubtitle("Libro: " + tituloLibro);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Configurar RecyclerView
        rvMensajes = findViewById(R.id.rvMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMensajes.setLayoutManager(layoutManager);

        adapter = new MensajeAdapter(new ArrayList<>()); // Inicializa con lista vacía
        rvMensajes.setAdapter(adapter);

        // Escuchar mensajes en tiempo real
        db.collection("mensajes")
                .whereEqualTo("libroTitulo", tituloLibro)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(ChatActivity.this, "Error al cargar mensajes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Mensaje> nuevosMensajes = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Mensaje mensaje = doc.toObject(Mensaje.class);
                        if (mensaje != null) {
                            nuevosMensajes.add(mensaje);
                        }
                    }
                    adapter.setMensajes(nuevosMensajes);
                    rvMensajes.smoothScrollToPosition(nuevosMensajes.size() - 1);
                });

        // Enviar mensaje
        btnEnviar.setOnClickListener(v -> {
            String texto = etMensaje.getText().toString().trim();
            if (!texto.isEmpty()) {
                enviarMensaje(texto);
                etMensaje.setText("");
            }
        });
    }

    private void enviarMensaje(String textoMensaje) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        // Crear estructura del mensaje
        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("emisorId", currentUser.getUid());
        mensaje.put("receptorId", duenoId);
        mensaje.put("texto", textoMensaje);
        mensaje.put("libroTitulo", tituloLibro);
        mensaje.put("timestamp", FieldValue.serverTimestamp());

        // Agregar mensaje a Firestore
        db.collection("mensajes")
                .add(mensaje)
                .addOnSuccessListener(documentReference -> {
                    // Mensaje enviado con éxito
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al enviar mensaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}