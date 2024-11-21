package com.example.juegominecraft;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    private static final String URL = "http:/192.168.102.125:8000/";
    private TextView text;
    private ImageView imatgePrincipal;
    private ProgressBar barraDeProgres;
    private ImageView imatgeOpcio1, imatgeOpcio2, imatgeOpcio3, imatgeOpcio4;
    private Button botoVerifica;
    private int opcioCorrecte;
    private boolean seleccionat = false;
    private ImageView[] arrayOpcions;
    private int seleccioIndex = -1;
    private int contador = 0;
    public String nombreJugador;
    private LinearLayout coordinatorLayout;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imatgePrincipal = findViewById(R.id.imatgePrincipal);
        barraDeProgres = findViewById(R.id.barraDeProgres);
        imatgeOpcio1 = findViewById(R.id.imatgeOpcio1);
        imatgeOpcio2 = findViewById(R.id.imatgeOpcio2);
        imatgeOpcio3 = findViewById(R.id.imatgeOpcio3);
        imatgeOpcio4 = findViewById(R.id.imatgeOpcio4);
        botoVerifica = findViewById(R.id.botoVerifica);
        text = findViewById(R.id.text);
        arrayOpcions = new ImageView[] {imatgeOpcio1, imatgeOpcio2, imatgeOpcio3, imatgeOpcio4};

        dbHelper = new SQLiteActivity(this);
        puntsActuals = dbHelper.obtenirPunts();

        nombreJugador = getIntent().getStringExtra("NOMBRE_JUGADOR");

        if (!dbHelper.jugadorExistent(nombreJugador)) {
            dbHelper.insertarNouJugador(nombreJugador);
        }

        puntsActuals = dbHelper.obtenirPuntuacioPerNom(nombreJugador);


        ocultarOpcions();
        crearJoc();

        StrictMode.ThreadPolicy politiques = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(politiques);

        String dadesLLegides = null;
        try {
            java.net.URL urlObj = new URL(URL);
            dadesLLegides = llegirJSON(urlObj);
            Log.i(MainActivity.class.getName(), dadesLLegides);
            text.setText(dadesLLegides);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject objecte = new JSONObject(dadesLLegides);
            Log.i(MainActivity.class.getName(), "Nombre d'entrades " + objecte.length());
            Log.i(MainActivity.class.getName(), objecte.getString("nom"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String llegirJSON(URL urlObj) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpURLConnection clientHTTP = (HttpURLConnection) urlObj.openConnection();

        if (clientHTTP.getResponseCode() == 200) {
            InputStream contingut = clientHTTP.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(contingut));
            String linia;
            while ((linia = reader.readLine()) != null) {
                builder.append(linia);
            }
        } else {
            Log.e(MainActivity.class.toString(), "Problemes HTTP");
        }
        return builder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ayuda) {
            obrirAjustos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obrirAjustos() {
        Intent intent = new Intent(JuegoActivity.this, AyudaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("JuegoActivity", "JuegoActivity paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("JuegoActivity", "JuegoActivity resumed");
    }

    private void crearJoc() {
        Random random = new Random();

        int personatgeIndex = random.nextInt(personatges.length);
        actualImatgesSet = personatges[personatgeIndex];

        List<Integer> indexU = new ArrayList<>();

        int randomIndex = random.nextInt(actualImatgesSet.length);
        int imagenSeleccionada = actualImatgesSet[randomIndex];
        imatgePrincipal.setImageResource(imagenSeleccionada);

        text.setVisibility(View.INVISIBLE);

        String nombreImagen = getResources().getResourceEntryName(imagenSeleccionada);
        obtenerTextoImagen(nombreImagen);

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

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                text.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void obtenerTextoImagen(String nombreImagen) {
        new Thread(() -> {
            try {
                URL url = new URL(URL + "?nom=" + nombreImagen);
                String respuesta = llegirJSON(url);

                JSONObject jsonObject = new JSONObject(respuesta);
                String textoImagen = jsonObject.getString("nom");

                runOnUiThread(() -> text.setText(textoImagen));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> text.setText("Error al obtener el texto de la imagen."));
            }
        }).start();
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
                imatgePrincipal.setImageResource(R.mipmap.img);
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

                    dbHelper.actualizaPuntuacioJugador(nombreJugador, puntsActuals);

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

        if (contador >= 10) {
            mostrarPuntuacion();
        } else {
            crearJoc();
        }
    }

}