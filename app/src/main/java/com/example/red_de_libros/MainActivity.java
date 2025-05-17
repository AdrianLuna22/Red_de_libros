package com.example.red_de_libros;

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

public class MainActivity extends AppCompatActivity {

    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // campos de texto
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Metodo para iniciar sesión al hacer click en el boton "entrar"
    public void iniciarSesion(View view){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        String correctEmail = "admin@admin.com";
        String correctPassword = "1234"

        // validaciones sencillas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        } else if (email == correctEmail && password == correctPassword) {
            Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }
}