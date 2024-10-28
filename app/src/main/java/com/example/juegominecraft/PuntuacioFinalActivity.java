package com.example.juegominecraft;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PuntuacioFinalActivity extends AppCompatActivity {

    private SQLiteActivity db;
    private TableLayout tablaPuntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciofinal);

        db = new SQLiteActivity(this);
        tablaPuntuaciones = findViewById(R.id.tablaPuntuacion);
        Button btnBorrarProgreso = findViewById(R.id.btnBorrarProgreso);

        cargarDatos();
        btnBorrarProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.borrarTodo();
                tablaPuntuaciones.removeAllViews();
                Toast.makeText(PuntuacioFinalActivity.this, "Progreso borrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatos() {
        Cursor cursor = db.obtenirTotsElsJugadors();
        if (cursor.moveToFirst()) {
            do {
                TableRow fila = new TableRow(this);

                TextView nombreJugador = new TextView(this);
                nombreJugador.setText(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_NOM)));
                nombreJugador.setPadding(20, 10, 20, 10);
                fila.addView(nombreJugador);

                TextView puntuacionJugador = new TextView(this);
                puntuacionJugador.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_PUNTS))));
                puntuacionJugador.setPadding(20, 10, 20, 10);
                fila.addView(puntuacionJugador);

                tablaPuntuaciones.addView(fila);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
