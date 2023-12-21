package com.example.proyectoempotrados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LibroDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LibroContract.LibroEntry.TABLE_NAME + " (" +
                    LibroContract.LibroEntry._ID + " INTEGER PRIMARY KEY," +
                    LibroContract.LibroEntry.COLUMN_NAME_TITULO + " TEXT UNIQUE," +
                    LibroContract.LibroEntry.COLUMN_NAME_AUTOR + " TEXT," +
                    LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL + " TEXT," +
                    LibroContract.LibroEntry.COLUMN_NAME_RESERVADO + " INTEGER DEFAULT 0" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LibroContract.LibroEntry.TABLE_NAME;

    public LibroDbHelper(Context context, String database_name) {
        super(context, database_name, null, DATABASE_VERSION);
    }

    private static final String[] PROJECTION_TITULOS = {
            LibroContract.LibroEntry.COLUMN_NAME_TITULO
    };

    public String[] obtenerTitulosLibros(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                LibroContract.LibroEntry.TABLE_NAME,
                PROJECTION_TITULOS,
                null,null,null,null,null
        );
        List<String> titulos = new ArrayList<>();
        if(cursor != null){
            try {
                int tituloIndex = cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_TITULO);
                while (cursor.moveToNext()){
                    String titulo = cursor.getString(tituloIndex);
                    titulos.add(titulo);
                }
            } finally {
                cursor.close();
            }
        }
        return titulos.toArray(new String[0]);
    }

    public String[] obtenerLibroPorTitulo(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                LibroContract.LibroEntry.TABLE_NAME,
                PROJECTION_TITULOS,
                LibroContract.LibroEntry.COLUMN_NAME_TITULO + " = ?",
                new String[]{titulo},
                null, null, null
        );

        List<String> titulos = new ArrayList<>();
        if (cursor != null) {
            try {
                int tituloIndex = cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_TITULO);
                while (cursor.moveToNext()) {
                    String tituloEncontrado = cursor.getString(tituloIndex);
                    titulos.add(tituloEncontrado);
                }
            } finally {
                cursor.close();
            }
        }
        return titulos.toArray(new String[0]);
    }


    public Cursor obtenerDetallesLibro(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                LibroContract.LibroEntry.TABLE_NAME,
                null,
                LibroContract.LibroEntry.COLUMN_NAME_TITULO + " = ?",
                new String[]{titulo},
                null,
                null,
                null
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
