package com.example.qrlector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1001;

    private Button btnScanQR, btnOpenImage;
    private ImageView imageView;
    private TextView tvResult;
    private NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanQR = findViewById(R.id.btn_scan_qr);
        btnOpenImage = findViewById(R.id.btn_open_image);
        imageView = findViewById(R.id.image_view);
        tvResult = findViewById(R.id.tv_result);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener((item) -> {
            if (item.getItemId() == R.id.menu_scan) {
                // Puedes agregar lógica para el escaneo aquí
                return true;
            } else if (item.getItemId() == R.id.menu_history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)); // Iniciar la actividad
                return true;
            } else if (item.getItemId() == R.id.menu_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)); // Iniciar la actividad
                return true;
            } //else if (item.getItemId() == R.id.menu_create) {
                //startActivity(new Intent(MainActivity.this, CreateActivity.class)); // Iniciar la actividad
                //return true;}
            else {
                return false;
            }
        });



        // Configurar los botones
        btnScanQR.setOnClickListener(v -> startQRScanner());
        btnOpenImage.setOnClickListener(v -> openImageGallery());
    }

    private void startQRScanner() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Escanea el código QR");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    private void openImageGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else {
                    tvResult.setText("El resultado es: " + result.getContents());
                    Toast.makeText(this, "Resultado: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
