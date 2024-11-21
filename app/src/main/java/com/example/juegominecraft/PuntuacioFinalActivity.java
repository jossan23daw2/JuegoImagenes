package com.example.juegominecraft;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    private Button volverJugar, btnBorrarProgreso, btnGuardarCalendario;
    private String nombreJugador;
    private int puntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciofinal);

        db = new SQLiteActivity(this);
        tablaPuntuaciones = findViewById(R.id.tablaPuntuacion);
        btnBorrarProgreso = findViewById(R.id.btnBorrarProgreso);
        volverJugar = findViewById(R.id.volverJugar);
        btnGuardarCalendario = findViewById(R.id.btnGuardarCalendario);

        Intent intent = getIntent();
        puntuacion = intent.getIntExtra("PUNTS", 0);
        nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR");

        cargarDatos();

        if (nombreJugador == null || nombreJugador.isEmpty()) {
            Cursor cursor = db.obtenirTotsElsJugadors();
            if (cursor.moveToFirst()) {
                nombreJugador = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_NOM));
                puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_PUNTS));
            }
            cursor.close();
        }

        Log.d("PuntuacioFinalActivity", "Jugador cargado: " + nombreJugador + ", Puntuación: " + puntuacion);

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
        btnGuardarCalendario.setOnClickListener(v -> guardarEnCalendario());
    }

    private void guardarEnCalendario() {
        Cursor cursor = db.obtenirUltimJugador();
        if (cursor.moveToFirst()) { // Obtén siempre el último jugador
            nombreJugador = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_NOM));
            puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteActivity.COLUMNA_PUNTS));
        }
        cursor.close();

        Log.d("PuntuacioFinalActivity", "Guardando en calendario: Jugador=" + nombreJugador + ", Puntuación=" + puntuacion);

        Intent intent = new Intent(PuntuacioFinalActivity.this, CalendariActivity.class);
        intent.putExtra("NOMBRE_JUGADOR", nombreJugador);
        intent.putExtra("PUNTS", puntuacion);
        intent.putExtra("FECHA", System.currentTimeMillis());
        startActivity(intent);
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

        Cursor cursor = db.obtenirTotsElsJugadorsOrdenados(); 
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