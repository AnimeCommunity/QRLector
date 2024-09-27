package com.example.qrlector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        // escanear QR
        Button btnScanQR = findViewById(R.id.btn_scan_qr);
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(ScanQRActivity.this).initiateScan();
            }
        });

        // tomar foto
        Button btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });

        //abrir galería
        Button btnSelectGallery = findViewById(R.id.btn_select_gallery);
        btnSelectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtener la foto capturada
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Aquí puedes guardar o mostrar la imagen
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtener la imagen seleccionada de la galería
            Uri selectedImageUri = data.getData();
            // Aquí puedes trabajar con la URI, como mostrar o guardar la imagen
        } else {
            // Manejar el resultado del escaneo QR
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                // Mostrar el contenido escaneado o guardar en la base de datos
                Intent intent = new Intent(ScanQRActivity.this, ResultActivity.class);
                intent.putExtra("qr_result", result.getContents());
                startActivity(intent);
            }
        }
    }
}

