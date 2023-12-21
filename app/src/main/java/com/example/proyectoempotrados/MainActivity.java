package com.example.proyectoempotrados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LibroDbHelper dbHelper;
    private EditText editText;
    private ListView listViewLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initLibros();
        setupListView();
    }

    private void initializeViews() {
        dbHelper = new LibroDbHelper(getApplicationContext(), "libros.db");
        editText = findViewById(R.id.editTextText);
        listViewLibros = findViewById(R.id.listaLibros);
    }

    private void initLibros() {
        insertLibro("El Señor de los Anillos", "J.R.R. Tolkien", "Minotauro", 0);
        insertLibro("Cien años de soledad", "Gabriel García Márquez", "Sudamericana", 0);
        insertLibro("Harry Potter y la piedra filosofal", "J.K. Rowling", "Salamandra", 0);
        insertLibro("1984", "George Orwell", "Debolsillo", 0);
        insertLibro("To Kill a Mockingbird", "Harper Lee", "J.B. Lippincott & Co.", 0);
        insertLibro("The Great Gatsby", "F. Scott Fitzgerald", "Charles Scribner's Sons", 0);
    }

    private long insertLibro(String titulo, String autor, String editorial, int reservado) {
        ContentValues values = new ContentValues();
        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, titulo);
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, autor);
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, editorial);
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, reservado);
        return Objects.requireNonNull(dbHelper.getWritableDatabase())
                .insert(LibroContract.LibroEntry.TABLE_NAME, null, values);
    }

    private void setupListView() {
        String[] titulos = dbHelper.obtenerTitulosLibros();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listViewLibros.setAdapter(adapter);

        listViewLibros.setOnItemClickListener((parent, view, position, id) -> {
            String tituloSeleccionado = (String) parent.getItemAtPosition(position);
            startDetalleLibroActivity(tituloSeleccionado);
        });
    }

    private void startDetalleLibroActivity(String titulo) {
        Intent intent = new Intent(MainActivity.this, DetalleLibroActivity.class);
        intent.putExtra("titulo_libro", titulo);
        startActivity(intent);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.buttonSearch) {
            searchByName();
        }
    }

    @SuppressLint("Rangs")
    public void searchByName() {
        String tituloBusqueda = editText.getText().toString().trim().toLowerCase();
        String[] titulosEncontrados = (tituloBusqueda.isEmpty()) ?
                dbHelper.obtenerTitulosLibros() :
                dbHelper.obtenerLibroPorTitulo(tituloBusqueda);

        updateListViewWithResults(titulosEncontrados);
    }

    private void updateListViewWithResults(String[] titulos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listViewLibros.setAdapter(adapter);

        listViewLibros.setOnItemClickListener((parent, view, position, id) -> {
            String tituloSeleccionado = (String) parent.getItemAtPosition(position);
            startDetalleLibroActivity(tituloSeleccionado);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}