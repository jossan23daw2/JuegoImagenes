package com.example.juegominecraft;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PuntuacioFinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciofinal);

        int puntuacion = getIntent().getIntExtra("PUNTS", 0);

        TextView puntuacionText = findViewById(R.id.textPuntuacion);
        puntuacionText.setText(" " + puntuacion);
    }

}
