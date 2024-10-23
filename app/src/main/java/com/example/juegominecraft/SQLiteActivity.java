package com.example.juegominecraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "puntuacio.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TAULA_PUNTS = "puntuacio";
    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_PUNTS = "punts";
    public static final String COLUMNA_NOM = "nombre";

    private static final String SQL_CREACIO_TAULA_PUNTS =
            "CREATE TABLE " + TAULA_PUNTS + " (" +
                    COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNA_PUNTS + " INTEGER, " +
                    COLUMNA_NOM + " TEXT)";

    public SQLiteActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREACIO_TAULA_PUNTS);
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_PUNTS, 0);
        valors.put(COLUMNA_NOM, "Jugador");
        db.insert(TAULA_PUNTS, null, valors);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TAULA_PUNTS);
        onCreate(db);
    }

    public int obtenirPuntuacioPerNom(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_PUNTS + " FROM " + TAULA_PUNTS + " WHERE " + COLUMNA_NOM + "=?", new String[] {nombre});
        if (cursor.moveToFirst()) {
            int punts = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_PUNTS));
            cursor.close();
            return punts;
        }
        cursor.close();
        return 0;
    }

    public boolean jugadorExistent(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TAULA_PUNTS + " WHERE " + COLUMNA_NOM + "=?", new String[]{nombre});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        cursor.close();
        return false;
    }

    public void insertarNouJugador(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_NOM, nombre);
        valors.put(COLUMNA_PUNTS, 0);
        db.insert(TAULA_PUNTS, null, valors);
    }

    public void actualizaPuntuacioJugador(String nombre, int punts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put(COLUMNA_PUNTS, punts);
        db.update(TAULA_PUNTS, valors, COLUMNA_NOM + "=?", new String[] {nombre});
    }

    public Cursor obtenirTotsElsJugadors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMNA_NOM + ", " + COLUMNA_PUNTS + " FROM " + TAULA_PUNTS, null);
    }

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
}
