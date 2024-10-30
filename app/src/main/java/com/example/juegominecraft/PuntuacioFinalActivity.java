package com.example.juegominecraft;

import android.content.Intent;
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
    private Button volverJugar, btnBorrarProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciofinal);

        db = new SQLiteActivity(this);
        tablaPuntuaciones = findViewById(R.id.tablaPuntuacion);
        btnBorrarProgreso = findViewById(R.id.btnBorrarProgreso);
        volverJugar = findViewById(R.id.volverJugar);

        cargarDatos();
        btnBorrarProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.borrarTodo();
                tablaPuntuaciones.removeAllViews();
                Toast.makeText(PuntuacioFinalActivity.this, "Progreso borrado", Toast.LENGTH_SHORT).show();
            }
        });
        volverJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuntuacioFinalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cargarDatos() {
        TableRow encabezado = new TableRow(this);

        TextView encabezadoNombre = new TextView(this);
        encabezadoNombre.setText("Nombre");
        encabezadoNombre.setPadding(20, 10, 20, 10);
        encabezadoNombre.setTextSize(22);
        encabezadoNombre.setTextColor(getResources().getColor(R.color.white));
        encabezadoNombre.setBackgroundColor(getResources().getColor(R.color.rojo));
        encabezado.addView(encabezadoNombre);

        TextView encabezadoPuntuacion = new TextView(this);
        encabezadoPuntuacion.setText("Puntuación");
        encabezadoPuntuacion.setPadding(20, 10, 20, 10);
        encabezadoPuntuacion.setTextSize(22);
        encabezadoPuntuacion.setTextColor(getResources().getColor(R.color.white));
        encabezadoPuntuacion.setBackgroundColor(getResources().getColor(R.color.rojo));
        encabezado.addView(encabezadoPuntuacion);

        tablaPuntuaciones.addView(encabezado);

        Cursor cursor = db.obtenirTotsElsJugadors();
        if (cursor.moveToFirst()) {
            int rowIndex = 0;
            do {
                TableRow fila = new TableRow(this);

                TextView nombreJugador = new TextView(this);
                nombreJugador.setText(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_NOM)));
                nombreJugador.setPadding(20, 10, 20, 10);
                nombreJugador.setTextSize(20);
                nombreJugador.setTextColor(getResources().getColor(R.color.white));
                nombreJugador.setBackgroundColor(rowIndex % 2 == 0 ? getResources().getColor(R.color.gris) : getResources().getColor(R.color.black));
                fila.addView(nombreJugador);

                TextView puntuacionJugador = new TextView(this);
                puntuacionJugador.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_PUNTS))));
                puntuacionJugador.setPadding(20, 10, 20, 10);
                puntuacionJugador.setTextSize(20);
                puntuacionJugador.setTextColor(getResources().getColor(R.color.white));
                puntuacionJugador.setBackgroundColor(rowIndex % 2 == 0 ? getResources().getColor(R.color.gris) : getResources().getColor(R.color.black));
                fila.addView(puntuacionJugador);

                tablaPuntuaciones.addView(fila);
                rowIndex++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}