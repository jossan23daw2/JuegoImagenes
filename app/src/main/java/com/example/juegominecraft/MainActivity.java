package com.example.juegominecraft;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SQLiteActivity dbHelper;
    private EditText editTextNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLiteActivity(this);
        editTextNombre = findViewById(R.id.input_name);
        Button btnGuardar = findViewById(R.id.button_save);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                if (!nombre.isEmpty()) {
                    guardarNombreEnDB(nombre);
                    // Navegar a JuegoActivity después de guardar
                    Intent intent = new Intent(MainActivity.this, JuegoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Introduce un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarNombreEnDB(String nombre) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);  // Asegúrate de que la columna en la DB se llame "nombre"
        db.insert("puntuacio", null, values);
        db.close();
    }
}
