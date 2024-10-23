package com.example.juegominecraft;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PuntuacioFinalActivity extends AppCompatActivity {

    private SQLiteActivity db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciofinal);

        TableLayout tablaPuntuaciones = findViewById(R.id.tablaPuntuacion);

        SQLiteActivity dbHelper = new SQLiteActivity(this);
        Cursor cursor = dbHelper.obtenirTotsElsJugadors();

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
