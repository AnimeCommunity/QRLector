package com.example.qrlector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.RGBLuminanceSource; 
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class ScanQRActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);


        Button btnScanQR = findViewById(R.id.btn_scan_qr);
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(ScanQRActivity.this).initiateScan();
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            decodeQRFromGalleryImage(selectedImageUri);
        } else {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {

                Intent intent = new Intent(ScanQRActivity.this, ResultActivity.class);
                intent.putExtra("qr_result", result.getContents());
                startActivity(intent);
            }
        }
    }

    private void decodeQRFromGalleryImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            String contents = decodeQRCode(bitmap);
            if (contents != null) {
                // Mostrar el resultado
                Intent intent = new Intent(ScanQRActivity.this, ResultActivity.class);
                intent.putExtra("qr_result", contents);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se pudo leer el código QR", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private String decodeQRCode(Bitmap bitmap) {
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
