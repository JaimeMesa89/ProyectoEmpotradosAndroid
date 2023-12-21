package com.example.proyectoempotrados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleLibroActivity extends AppCompatActivity {
    private LibroDbHelper dbHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

        dbHelper = new LibroDbHelper(getApplicationContext(), "libros.db");

        Intent intent = getIntent();
        String tituloLibro = intent.getStringExtra("titulo_libro");

        Cursor cursor = dbHelper.obtenerDetallesLibro(tituloLibro);

        if (cursor != null && cursor.moveToFirst()) {
            TextView textViewTitulo = findViewById(R.id.textViewTitulo);
            textViewTitulo.setText("Título: " + cursor.getString(cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_TITULO)));

            TextView textViewAutor = findViewById(R.id.textViewAutor);
            textViewAutor.setText("Autor: " + cursor.getString(cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_AUTOR)));

            TextView textViewEditorial = findViewById(R.id.textViewEditorial);
            textViewEditorial.setText("Editorial: " + cursor.getString(cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL)));

            TextView textViewReservado = findViewById(R.id.textViewReservado);
            final boolean[] reservado = {cursor.getInt(cursor.getColumnIndex(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO)) == 1};
            textViewReservado.setText("Reservado: " + (reservado[0] ? "Sí" : "No"));

            final Button buttonCancelarReserva = findViewById(R.id.buttonCancelarReserva);
            final Button buttonReservar = findViewById(R.id.buttonReservar);

            // Configura la visibilidad de los botones según el estado de reserva
            if (reservado[0]) {
                buttonReservar.setVisibility(View.GONE);
                buttonCancelarReserva.setVisibility(View.VISIBLE);
            } else {
                buttonReservar.setVisibility(View.VISIBLE);
                buttonCancelarReserva.setVisibility(View.GONE);
            }

            cursor.close();

            buttonReservar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 1);
                    dbHelper.getWritableDatabase().update(
                            LibroContract.LibroEntry.TABLE_NAME,
                            values,
                            LibroContract.LibroEntry.COLUMN_NAME_TITULO + " = ?",
                            new String[]{tituloLibro}
                    );

                    reservado[0] = true;
                    textViewReservado.setText("Reservado: Sí");

                    mostrarToast();
                    buttonReservar.setVisibility(View.GONE);
                    buttonCancelarReserva.setVisibility(View.VISIBLE);
                }
            });

            buttonCancelarReserva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
                    dbHelper.getWritableDatabase().update(
                            LibroContract.LibroEntry.TABLE_NAME,
                            values,
                            LibroContract.LibroEntry.COLUMN_NAME_TITULO + " = ?",
                            new String[]{tituloLibro}
                    );

                    reservado[0] = false;
                    textViewReservado.setText("Reservado: No");

                    mostrarToastCancelarReserva();
                    buttonCancelarReserva.setVisibility(View.GONE);
                    buttonReservar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void mostrarToast(){
        Toast.makeText(this, R.string.reservado, Toast.LENGTH_LONG).show();
    }

    private void mostrarToastCancelarReserva(){
        Toast.makeText(this, R.string.cancelarReserva, Toast.LENGTH_LONG).show();
    }
}