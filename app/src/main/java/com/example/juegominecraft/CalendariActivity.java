package com.example.juegominecraft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CalendariActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CALENDAR = 100;

    private ContentResolver contentResolver;
    private Set<String> calendaris = new HashSet<String>();
    private List<String> events = new ArrayList<String>();
    private String nombreJugador;
    private int puntuacion;
    private long hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR");
        puntuacion = intent.getIntExtra("PUNTS", 0);
        hora = intent.getLongExtra("FECHA", System.currentTimeMillis());

        if (nombreJugador == null || nombreJugador.isEmpty()) {
            nombreJugador = "Jugador desconocido";
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {


            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }



        setContentView(R.layout.activity_puntuaciofinal);
        contentResolver = getContentResolver();

        checkAndRequestPermissions();

        Log.d("CalendariActivity", "Jugador: " + nombreJugador + ", Puntuación: " + puntuacion + ", Hora: " + hora);
    }

    public void onClick(View view) {
        afegirEvent(nombreJugador, puntuacion, hora);
        obtenirEvents();
        Log.i(getClass().getName(), calendaris.toString());
        Log.i(getClass().getName(), events.toString());
    }

    /**
     * Mètode que permet afegir un event a un calendari de l'usuari
     */
    private void afegirEvent(String jugador, int punts, long hora) {
        int calendarId = obtenirCalendariPerId(getContentResolver(), "15586175.clot@fje.edu");
        if (calendarId == -1) {
            Toast.makeText(this, "Error: no se encontró el calendario", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues esdeveniment = new ContentValues();
        esdeveniment.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        esdeveniment.put(CalendarContract.Events.TITLE, "Puntuación de " + jugador);
        esdeveniment.put(CalendarContract.Events.DESCRIPTION, "Puntuación obtenida: " + punts);
        esdeveniment.put(CalendarContract.Events.DTSTART, hora);
        esdeveniment.put(CalendarContract.Events.DTEND, hora + 60 * 60 * 1000); // Duración 1 hora
        esdeveniment.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, esdeveniment);

        if (uri != null) {
            int id = Integer.parseInt(uri.getLastPathSegment());
            Toast.makeText(this, "Evento creado con ID: " + id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al crear el evento", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Mètode que recupera tots els calendaris disponibles al dispositiu
     */
    private void obtenirCalendaris() {
        //la URI dels calendaris és content://com.android.calendar/calendars
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projeccio = {CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_COLOR,
                CalendarContract.Calendars.VISIBLE};
        Cursor cursor = contentResolver.query(uri, projeccio, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String nomIntern = cursor.getString(0);
                    String nomMostrat = cursor.getString(1);
                    @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean seleccionat = !cursor.getString(3).equals("0");
                    calendaris.add(nomMostrat);
                }
            }
        } catch (AssertionError ex) {
        }
    }

    /**
     * Mètode que recupera determinats events d'un calendari.
     * Filtra pel titol del esdeveniment
     */
    private void obtenirEvents() {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String seleccio = String.format("(%s = ?)", CalendarContract.Events.TITLE);
        String[] seleccioArgs = new String[]{"DAM2 Escola del Clot"};
        String[] projeccio = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART
        };
        Cursor cursor = contentResolver.query(uri, projeccio, seleccio, seleccioArgs, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String titol = cursor.getString(1);
            events.add(titol);
        }
    }

    @SuppressLint("Range")
    private int obtenirCalendariPerId(ContentResolver contentResolver, String calendarName) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };
        String selection = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{calendarName};

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                }
            } finally {
                cursor.close();
            }
        }
        return -1; // Return -1 if calendar not found
    }
    private void checkAndRequestPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_CALENDAR);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_READ_CALENDAR);
        } else {
            // Si los permisos ya están concedidos, ejecuta la lógica principal
            afegirEvent(nombreJugador, puntuacion, hora);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                afegirEvent(nombreJugador, puntuacion, hora);
            } else {
                Toast.makeText(this, "Permisos denegados. No se puede crear el evento.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}