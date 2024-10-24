package com.example.qrlector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Carga de imágenes
        List<File> imageFiles = getImageFiles();

        if (imageFiles.isEmpty()) {
            Toast.makeText(this, "No se encontraron imágenes en el historial", Toast.LENGTH_SHORT).show();
        }

        imageAdapter = new ImageAdapter(this, imageFiles, this::onImageSelected);
        recyclerView.setAdapter(imageAdapter);
    }

    private void onImageSelected(File file) {
        // Muestra el diálogo para enviar la imagen seleccionada
        new AlertDialog.Builder(this)
                .setTitle("Enviar imagen")
                .setMessage("¿Deseas enviar esta imagen?")
                .setPositiveButton("Enviar", (dialog, which) -> sendImage(file))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void sendImage(File file) {
        // Obtén la URI del archivo utilizando FileProvider
        Uri fileUri = androidx.core.content.FileProvider.getUriForFile(
                this, getPackageName() + ".fileprovider", file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Concede permisos de lectura

        startActivity(Intent.createChooser(sendIntent, "Enviar imagen a través de"));
    }



    private List<File> getImageFiles() {
        // Inicializamos la lista de archivos de imagen
        List<File> imageFiles = new ArrayList<>();

        // Directorio para imágenes de códigos QR
        File qrDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "QRImages");

        // Directorio para imágenes de códigos de barras
        File barcodeDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Barcodes");

        // Verificar si los directorios existen y cargar imágenes
        if (qrDir.exists()) {
            File[] qrFiles = qrDir.listFiles();
            if (qrFiles != null) {
                for (File file : qrFiles) {
                    if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                        imageFiles.add(file);
                    }
                }
            }
        }

        if (barcodeDir.exists()) {
            File[] barcodeFiles = barcodeDir.listFiles();
            if (barcodeFiles != null) {
                for (File file : barcodeFiles) {
                    if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                        imageFiles.add(file);
                    }
                }
            }
        }

        // Devolver una lista vacía si no se encuentran archivos
        return imageFiles.isEmpty() ? new ArrayList<>() : imageFiles;
    }
}
