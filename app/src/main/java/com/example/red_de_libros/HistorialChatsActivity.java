package com.example.red_de_libros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistorialChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_chats);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis chats");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rvChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatsAdapter(new ArrayList<>(), this::abrirChat);
        recyclerView.setAdapter(adapter);

        cargarChats();
    }

    private void cargarChats() {
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("mensajes")
                .whereArrayContains("participantes", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Chat> chats = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("FirestoreData", "Document data: " + document.getData());

                            Chat chat = document.toObject(Chat.class);
                            chat.setId(document.getId()); // Asegurar que el ID se establece

                            // Obtener el nombre del otro usuario si no está en el documento
                            if (chat.getNombreOtroUsuario() == null || chat.getNombreOtroUsuario().isEmpty()) {
                                chat.setNombreOtroUsuario("Usuario");
                            }

                            chats.add(chat);
                        }
                        adapter.setChats(chats);
                        Log.d("ChatsCount", "Número de chats cargados: " + chats.size());
                    } else {
                        Toast.makeText(this, "Error al cargar chats: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Error al cargar chats", task.getException());
                    }
                });
    }

    private void abrirChat(Chat chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chat.getId());
        intent.putExtra("nombreDueno", chat.getNombreOtroUsuario());
        intent.putExtra("tituloLibro", chat.getTituloLibro());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}