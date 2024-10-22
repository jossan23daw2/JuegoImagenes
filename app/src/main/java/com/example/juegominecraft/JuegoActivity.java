package com.example.juegominecraft;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    private ImageView imatgePrincipal;
    private ProgressBar barraDeProgres;
    private ImageView imatgeOpcio1, imatgeOpcio2, imatgeOpcio3, imatgeOpcio4;
    private Button botoVerifica;
    private int opcioCorrecte;
    private boolean seleccionat = false;
    private ImageView[] arrayOpcions;
    private int seleccioIndex = -1;
    private int contador = 0;

    private SQLiteActivity dbHelper;
    private int puntsActuals;

    private int[] bayonettaI = {R.drawable.bayonetta0, R.drawable.bayonetta1, R.drawable.bayonetta2, R.drawable.bayonetta3};
    private int[] donkeyI = {R.drawable.donkey0, R.drawable.donkey1, R.drawable.donkey2, R.drawable.donkey3};
    private int[] sonicI = {R.drawable.sonic0, R.drawable.sonic1, R.drawable.sonic2, R.drawable.sonic3};
    private int[] canelaI = {R.drawable.canela0, R.drawable.canela1, R.drawable.canela2, R.drawable.canela3};
    private int[] greninjaI = {R.drawable.greninja0, R.drawable.greninja1, R.drawable.greninja2, R.drawable.greninja3};
    private int[] ikeI = {R.drawable.ike0, R.drawable.ike1, R.drawable.ike2, R.drawable.ike3};
    private int[] incineroarI = {R.drawable.incineroar0, R.drawable.incineroar1, R.drawable.incineroar2, R.drawable.incineroar3};
    private int[] linkI = {R.drawable.link0, R.drawable.link1, R.drawable.link2, R.drawable.link3};
    private int[] steveI = {R.drawable.steve0, R.drawable.steve1, R.drawable.steve2, R.drawable.steve3};
    private int[] terryI = {R.drawable.terry0, R.drawable.terry1, R.drawable.terry2, R.drawable.terry3};

    private int[][] personatges = {bayonettaI, donkeyI, sonicI, canelaI, greninjaI, ikeI, incineroarI, linkI, steveI, terryI};
    private int[] actualImatgesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        imatgePrincipal = findViewById(R.id.imatgePrincipal);
        barraDeProgres = findViewById(R.id.barraDeProgres);
        imatgeOpcio1 = findViewById(R.id.imatgeOpcio1);
        imatgeOpcio2 = findViewById(R.id.imatgeOpcio2);
        imatgeOpcio3 = findViewById(R.id.imatgeOpcio3);
        imatgeOpcio4 = findViewById(R.id.imatgeOpcio4);
        botoVerifica = findViewById(R.id.botoVerifica);

        arrayOpcions = new ImageView[] {imatgeOpcio1, imatgeOpcio2, imatgeOpcio3, imatgeOpcio4};

        dbHelper = new SQLiteActivity(this);
        puntsActuals = dbHelper.obtenirPunts();

        ocultarOpcions();
        crearJoc();
    }

    private void crearJoc() {
        Random random = new Random();

        int personatgeIndex = random.nextInt(personatges.length);
        actualImatgesSet = personatges[personatgeIndex];

        List<Integer> indexU = new ArrayList<>();

        int randomIndex = random.nextInt(actualImatgesSet.length);
        imatgePrincipal.setImageResource(actualImatgesSet[randomIndex]);

        indexU.add(randomIndex);

        opcioCorrecte = random.nextInt(4);
        arrayOpcions[opcioCorrecte].setImageResource(actualImatgesSet[randomIndex]);

        for (int i = 0; i < arrayOpcions.length; i++) {
            if (i != opcioCorrecte) {
                int altreIndex;
                do {
                    altreIndex = random.nextInt(actualImatgesSet.length);
                } while (indexU.contains(altreIndex));
                arrayOpcions[i].setImageResource(actualImatgesSet[altreIndex]);
                indexU.add(altreIndex);
            }
        }

        ocultarOpcions();
        tempsImatge();
    }

    private void tempsImatge() {
        barraDeProgres.setMax(1000);
        barraDeProgres.setProgress(1000);

        new CountDownTimer(3000, 30) {
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 1000 / 3000);
                barraDeProgres.setProgress(progress);
            }

            public void onFinish() {
                imatgePrincipal.setImageResource(android.R.color.black);
                ensenyaOpcions();
                verficacio();
            }
        }.start();
    }

    private void verficacio() {
        for (int i = 0; i < arrayOpcions.length; i++) {
            final int index = i;
            arrayOpcions[i].setOnClickListener(v -> {
                if (seleccioIndex != -1 && seleccioIndex != index) {
                    arrayOpcions[seleccioIndex].setBackgroundResource(0);
                }

                seleccioIndex = index;
                arrayOpcions[index].setBackgroundResource(R.drawable.borde_rojo);

                if (!seleccionat) {
                    botoVerifica.setVisibility(View.VISIBLE);
                }
                seleccionat = true;

                botoVerifica.setOnClickListener(v1 -> {
                    if (seleccioIndex == opcioCorrecte) {
                        Toast.makeText(JuegoActivity.this, "Correcte!", Toast.LENGTH_SHORT).show();
                        puntsActuals += 100;
                    } else {
                        Toast.makeText(JuegoActivity.this, "Incorrecte", Toast.LENGTH_SHORT).show();
                        puntsActuals -= 50;
                    }

                    dbHelper.actualitzarPunts(puntsActuals);

                    resetJoc();
                });
            });
        }
    }

    private void ocultarOpcions() {
        for (ImageView option : arrayOpcions) {
            option.setVisibility(View.INVISIBLE);
        }
            botoVerifica.setVisibility(View.INVISIBLE);
    }

    private void ensenyaOpcions() {
        for (ImageView option : arrayOpcions) {
            option.setVisibility(View.VISIBLE);
        }
    }
    private void mostrarPuntuacion() {
        Intent intent = new Intent(JuegoActivity.this, PuntuacioFinalActivity.class);
        intent.putExtra("PUNTS", puntsActuals);
        startActivity(intent);
        finish();
    }
    private void resetJoc() {
        seleccionat = false;
        seleccioIndex = -1;

        for (ImageView option : arrayOpcions) {
            option.setBackgroundResource(0);
        }

        ocultarOpcions();
        contador++;

        if (contador >= 2) {
            mostrarPuntuacion();
        } else {
            crearJoc();
        }
    }

}

