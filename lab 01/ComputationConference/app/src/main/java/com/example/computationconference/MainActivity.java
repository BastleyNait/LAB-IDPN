package com.example.computationconference;

import android.content.Intent;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtGrupoSanguineo;
    private Button btnSubmit, btnVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        initViews();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        edtNombres = findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtGrupoSanguineo = findViewById(R.id.edtGrupoSanguineo);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnVer = findViewById(R.id.btnVer);

        // Limpiar texto por defecto
        edtNombres.setText("");
        edtApellidos.setText("");
        edtCorreo.setText("");
        edtTelefono.setText("");
        edtGrupoSanguineo.setText("");

        // Configurar hints
        edtNombres.setHint("Ingrese sus nombres");
        edtApellidos.setHint("Ingrese sus apellidos");
        edtCorreo.setHint("Ingrese su correo");
        edtTelefono.setHint("Ingrese su teléfono");
        edtGrupoSanguineo.setHint("Ingrese su tipo de sangre");
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verRegistrados();
            }
        });
    }

    private void registrarUsuario() {
        String nombres = edtNombres.getText().toString().trim();
        String apellidos = edtApellidos.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String grupoSanguineo = edtGrupoSanguineo.getText().toString().trim();

        // Validar campos
        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || 
            telefono.isEmpty() || grupoSanguineo.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de correo básico
        if (!correo.contains("@") || !correo.contains(".")) {
            Toast.makeText(this, "Por favor ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar en CSV
        if (guardarEnCSV(nombres, apellidos, correo, telefono, grupoSanguineo)) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean guardarEnCSV(String nombres, String apellidos, String correo, String telefono, String grupoSanguineo) {
        try {
            File file = new File(getFilesDir(), "usuarios_registrados.csv");
            boolean isNewFile = !file.exists();
            
            FileWriter writer = new FileWriter(file, true);
            
            // Si es un archivo nuevo, agregar encabezados
            if (isNewFile) {
                writer.append("Nombres,Apellidos,Correo,Telefono,Grupo Sanguineo\n");
            }
            
            // Agregar datos del usuario
            writer.append(nombres).append(",")
                  .append(apellidos).append(",")
                  .append(correo).append(",")
                  .append(telefono).append(",")
                  .append(grupoSanguineo).append("\n");
            
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void limpiarCampos() {
        edtNombres.setText("");
        edtApellidos.setText("");
        edtCorreo.setText("");
        edtTelefono.setText("");
        edtGrupoSanguineo.setText("");
    }

    private void verRegistrados() {
        Intent intent = new Intent(this, ViewRegisteredActivity.class);
        startActivity(intent);
    }
}