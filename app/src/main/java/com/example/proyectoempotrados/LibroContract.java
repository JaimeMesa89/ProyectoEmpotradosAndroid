package com.example.proyectoempotrados;

import android.provider.BaseColumns;

public final class LibroContract {
    private LibroContract(){}

    public static abstract class LibroEntry implements BaseColumns {
        public static final String TABLE_NAME = "Libros";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_AUTOR = "autor";
        public static final String COLUMN_NAME_EDITORIAL = "editorial";
        public static final String COLUMN_NAME_RESERVADO = "reservado";
    }
}
