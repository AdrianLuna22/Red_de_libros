package com.example.red_de_libros;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMensajes;
    private EditText etMensaje;
    private ImageButton btnEnviar;
    private MensajeAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference mensajesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mensajesRef = database.getReference("mensajes");

        // Configurar RecyclerView
        rvMensajes = findViewById(R.id.rvMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMensajes.setLayoutManager(layoutManager);

        adapter = new MensajeAdapter();
        rvMensajes.setAdapter(adapter);

        // Escuchar mensajes en tiempo real
        mensajesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje mensaje = snapshot.getValue(Mensaje.class);
                if(mensaje != null) {
                    adapter.addMensaje(mensaje);
                    rvMensajes.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
            //... otros métodos del ChildEventListener
        });

        // Enviar mensaje
        btnEnviar.setOnClickListener(v -> {
            String texto = etMensaje.getText().toString().trim();
            if(!texto.isEmpty()) {
                enviarMensaje(texto);
                etMensaje.setText("");
            }
        });
    }

    private void enviarMensaje(String texto) {
        FirebaseUser usuario = mAuth.getCurrentUser();
        if(usuario != null) {
            Mensaje mensaje = new Mensaje(
                    usuario.getUid(),
                    usuario.getDisplayName(),
                    texto,
                    new Date().getTime()
            );

            mensajesRef.push().setValue(mensaje);
        }
    }
}
