package com.example.juegominecraft;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SQLiteActivity dbHelper;
    private EditText editTextNombre;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SQLiteActivity(this);
        editTextNombre = findViewById(R.id.input_name);
        Button btnGuardar = findViewById(R.id.button_save);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                if (!nombre.isEmpty()) {
                    guardarNombreEnDB(nombre);
                    Intent intent = new Intent(MainActivity.this, JuegoActivity.class);
                    intent.putExtra("NOMBRE_JUGADOR", nombre);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Introduce un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_ayuda){
            obrirAjustos();
            return true;
        }
        return false;
    }

    private void obrirAjustos() {
        Snackbar.make(coordinatorLayout, "AJUSTOS", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void guardarNombreEnDB(String nombre) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        db.insert("puntuacio", null, values);
        db.close();
    }
}
