package com.example.computationconference;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ViewRegisteredActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registered);

        tableLayout = findViewById(R.id.tableLayout);
        
        // Configurar la barra de acción
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Usuarios Registrados");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cargarDatosCSV();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void cargarDatosCSV() {
        File file = new File(getFilesDir(), "usuarios_registrados.csv");
        
        if (!file.exists()) {
            Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(",");
                
                if (datos.length == 5) {
                    TableRow row = new TableRow(this);
                    
                    // Configurar parámetros de la fila
                    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    row.setLayoutParams(rowParams);
                    
                    // Agregar padding y estilo para la primera fila (encabezados)
                    if (isFirstLine) {
                        row.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        isFirstLine = false;
                    }

                    for (String dato : datos) {
                        TextView textView = new TextView(this);
                        textView.setText(dato.trim());
                        textView.setPadding(16, 12, 16, 12);
                        textView.setTextSize(14);
                        
                        // Configurar parámetros del TextView
                        TableRow.LayoutParams textParams = new TableRow.LayoutParams(
                                0,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                1.0f
                        );
                        textView.setLayoutParams(textParams);
                        
                        row.addView(textView);
                    }
                    
                    tableLayout.addView(row);
                }
            }
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}