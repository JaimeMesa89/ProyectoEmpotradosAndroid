package com.example.proyectoempotrados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private LibroDbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText editText;
    private ListView listViewLibros;

    private void initializeViews() {
        dbHelper = new LibroDbHelper(getApplicationContext(), "libros.db");
        editText = findViewById(R.id.editTextText);
        db = dbHelper.getWritableDatabase();
        listViewLibros = findViewById(R.id.listaLibros);
    }

    private void initLibros(){
        ContentValues values = new ContentValues();
        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "El Señor de los Anillos");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "J.R.R. Tolkien");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "Minotauro");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);

        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "Cien años de soledad");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "Gabriel García Márquez");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "Sudamericana");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);

        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "Harry Potter y la piedra filosofal");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "J.K. Rowling");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "Salamandra");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);

        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "1984");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "George Orwell");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "Debolsillo");
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);

        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "To Kill a Mockingbird");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "Harper Lee");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "J.B. Lippincott & Co.");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);

        values.put(LibroContract.LibroEntry.COLUMN_NAME_TITULO, "The Great Gatsby");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_AUTOR, "F. Scott Fitzgerald");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_EDITORIAL, "Charles Scribner's Sons");
        values.put(LibroContract.LibroEntry.COLUMN_NAME_RESERVADO, 0);
        db.insert(LibroContract.LibroEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        initLibros();

        String[] titulos = obtenerTitulosLibros();

        ListView listViewLibros = findViewById(R.id.listaLibros);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listViewLibros.setAdapter(adapter);

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tituloSeleccionado = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetalleLibroActivity.class);
                intent.putExtra("titulo_libro", tituloSeleccionado);
                startActivity(intent);
            }
        });
    }

    private String[] obtenerTitulosLibros() {
        return dbHelper.obtenerTitulosLibros();
    }

    private String[] obtenerLibroPorTitulo(String titulo) {
        return dbHelper.obtenerLibroPorTitulo(titulo);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSearch: search_by_name(); break;
        }
    }

    @SuppressLint("Rangs")
    public void search_by_name() {
        String tituloBusqueda = editText.getText().toString().trim().toLowerCase();
        String[] titulosEncontrados;

        if (tituloBusqueda.isEmpty()) {
            titulosEncontrados = dbHelper.obtenerTitulosLibros();
        } else {
            titulosEncontrados = dbHelper.obtenerLibroPorTitulo(tituloBusqueda);
        }

        ListView listViewLibros = findViewById(R.id.listaLibros);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulosEncontrados);
        listViewLibros.setAdapter(adapter);


        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tituloSeleccionado = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetalleLibroActivity.class);
                intent.putExtra("titulo_libro", tituloSeleccionado);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}