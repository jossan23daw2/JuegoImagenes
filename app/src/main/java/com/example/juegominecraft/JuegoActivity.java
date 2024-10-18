package com.example.juegominecraft;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    private ImageView mainImage;
    private ProgressBar progressBar;
    private ImageView option1, option2, option3, option4;
    private Button checkButton;
    private int correctOption; // Índice de la opción correcta
    private boolean hasSelected = false;
    private ImageView[] options;
    private int selectedOptionIndex = -1; // Índice de la imagen seleccionada actualmente

    // Arreglos de imágenes por personaje
    private int[] bayonettaImages = {
            R.drawable.bayonetta0, R.drawable.bayonetta1, R.drawable.bayonetta2,
            R.drawable.bayonetta3
    };

    private int[] donkeyImages = {
            R.drawable.donkey0, R.drawable.donkey1, R.drawable.donkey2,
            R.drawable.donkey3
    };

    private int[] sonicImages = {
            R.drawable.sonic0, R.drawable.sonic1, R.drawable.sonic2,
            R.drawable.sonic3
    };

    // Mantén un arreglo de arreglos para acceder fácilmente
    private int[][] characterImageSets = {bayonettaImages, donkeyImages, sonicImages};
    private int[] currentImageSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        mainImage = findViewById(R.id.mainImage);
        progressBar = findViewById(R.id.progressBar);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        checkButton = findViewById(R.id.checkButton);

        options = new ImageView[] {option1, option2, option3, option4};

        // Ocultar opciones al inicio
        hideOptions();

        // Inicia el juego
        startNewGame();
    }

    private void startNewGame() {
        Random random = new Random();

        // Selecciona aleatoriamente un conjunto de imágenes de un personaje
        int characterIndex = random.nextInt(characterImageSets.length);
        currentImageSet = characterImageSets[characterIndex];

        // Lista para mantener un registro de los índices ya utilizados
        List<Integer> usedIndices = new ArrayList<>();

        // Selecciona una imagen aleatoria de ese personaje como imagen principal
        int randomImageIndex = random.nextInt(currentImageSet.length);
        mainImage.setImageResource(currentImageSet[randomImageIndex]);

        // Agrega el índice de la imagen principal a la lista de usados
        usedIndices.add(randomImageIndex);

        // Establece cuál será la imagen correcta entre las opciones
        correctOption = random.nextInt(4);
        options[correctOption].setImageResource(currentImageSet[randomImageIndex]);

        // Asigna imágenes aleatorias (del mismo personaje) a las otras opciones, sin repetir
        for (int i = 0; i < options.length; i++) {
            if (i != correctOption) {
                int otherImageIndex;
                do {
                    otherImageIndex = random.nextInt(currentImageSet.length);
                } while (usedIndices.contains(otherImageIndex)); // Evita repeticiones
                options[i].setImageResource(currentImageSet[otherImageIndex]);
                usedIndices.add(otherImageIndex); // Marca este índice como usado
            }
        }

        // Oculta las opciones al inicio
        hideOptions();
        startTimer();
    }

    private void startTimer() {
        // Reinicia la barra de progreso para cada nuevo juego
        progressBar.setMax(1000);
        progressBar.setProgress(1000);

        // Temporizador de 3 segundos
        new CountDownTimer(3000, 30) {
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 1000 / 3000); // Escala a 1000 como max
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                // Poner imagen negra y mostrar opciones
                mainImage.setImageResource(android.R.color.black);
                showOptions();
                setOptionClickListeners();
            }
        }.start();
    }

    private void setOptionClickListeners() {
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            options[i].setOnClickListener(v -> {
                // Deseleccionar la imagen anterior
                if (selectedOptionIndex != -1 && selectedOptionIndex != index) {
                    options[selectedOptionIndex].setBackgroundResource(0);
                }

                // Marca la imagen actual con borde rojo
                selectedOptionIndex = index;
                options[index].setBackgroundResource(R.drawable.borde_rojo);

                // Mostrar botón de verificar
                if (!hasSelected) {
                    checkButton.setVisibility(View.VISIBLE);
                }
                hasSelected = true;

                checkButton.setOnClickListener(v1 -> {
                    if (selectedOptionIndex == correctOption) {
                        Toast.makeText(JuegoActivity.this, "¡Correcto!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(JuegoActivity.this, "Incorrecto", Toast.LENGTH_SHORT).show();
                    }

                    // Reiniciar juego después de verificar
                    resetGame();
                });
            });
        }
    }

    private void hideOptions() {
        for (ImageView option : options) {
            option.setVisibility(View.INVISIBLE);
        }
        checkButton.setVisibility(View.GONE);
    }

    private void showOptions() {
        for (ImageView option : options) {
            option.setVisibility(View.VISIBLE);
        }
    }

    private void resetGame() {
        hasSelected = false;
        selectedOptionIndex = -1;

        // Restablecer los bordes de las imágenes
        for (ImageView option : options) {
            option.setBackgroundResource(0);
        }

        // Ocultar opciones y reiniciar el juego
        hideOptions();
        startNewGame();
    }
}

