package com.example.juegominecraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "puntuacio.db";
    private static final int DATABASE_VERSION = 2; // Incrementado para reflejar el cambio de esquema

    public static final String TAULA_PUNTS = "puntuacio";
    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_PUNTS = "punts";
    public static final String COLUMNA_NOMBRE = "nombre";  // Nueva columna

    private static final String SQL_CREACIO_TAULA_PUNTS =
            "CREATE TABLE " + TAULA_PUNTS + " (" +
                    COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNA_PUNTS + " INTEGER, " +
                    COLUMNA_NOMBRE + " TEXT)";  // Añadido campo nombre

    public SQLiteActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREACIO_TAULA_PUNTS);
        // Inicializa la tabla con un nombre y puntos por defecto
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_PUNTS, 0);
        valors.put(COLUMNA_NOMBRE, "Jugador");  // Nombre por defecto
        db.insert(TAULA_PUNTS, null, valors);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TAULA_PUNTS);
        onCreate(db);
    }

    // Método para obtener el nombre
    public String obtenerNombre() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_NOMBRE + " FROM " + TAULA_PUNTS + " LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_NOMBRE));
            cursor.close();
            return nombre;
        }
        cursor.close();
        return null;
    }

    // Método para actualizar el nombre
    public void actualizarNombre(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_NOMBRE, nombre);
        db.update(TAULA_PUNTS, valors, null, null);
    }

    // Otros métodos para puntos
    public int obtenirPunts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_PUNTS + " FROM " + TAULA_PUNTS + " LIMIT 1", null);
        if (cursor.moveToFirst()) {
            int punts = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_PUNTS));
            cursor.close();
            return punts;
        }
        cursor.close();
        return 0;
    }

    public void actualitzarPunts(int punts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_PUNTS, punts);
        db.update(TAULA_PUNTS, valors, null, null);
    }
}
