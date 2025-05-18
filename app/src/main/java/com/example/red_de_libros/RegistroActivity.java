package com.example.red_de_libros;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText nombreField, emailField, passwordField;
    private Button btnCrearCuenta;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        nombreField = findViewById(R.id.nombre);
        emailField = findViewById(R.id.email_registro);
        passwordField = findViewById(R.id.password_registro);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = nombreField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Guardar datos adicionales en Firestore
                        String uid = mAuth.getCurrentUser().getUid();

                        Map<String, Object> datosUsuario = new HashMap<>();
                        datosUsuario.put("nombre", nombre);
                        datosUsuario.put("correo", email);

                        firestore.collection("usuarios").document(uid)
                                .set(datosUsuario)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                                    // Redirigir al menú principal o a login
                                    startActivity(new Intent(this, PaginaPrincipal.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al guardar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Error al crear cuenta: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


