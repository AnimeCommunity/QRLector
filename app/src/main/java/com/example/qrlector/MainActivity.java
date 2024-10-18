package com.example.qrlector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnScanQR, btnViewHistory, btnSettings, btnCreateQrBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asegúrate de que este es el nombre correcto del layout XML

        // Referencia a los botones en el layout
        btnScanQR = findViewById(R.id.scan_button);
        btnViewHistory = findViewById(R.id.history_button);
        btnSettings = findViewById(R.id.btn_settings);
        btnCreateQrBarcode = findViewById(R.id.btn_create_qr_barcode);

        // Acción para abrir la actividad de escaneo de QR
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        // Acción para abrir la actividad de historial
        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // Acción para abrir la actividad de configuración
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Acción para abrir la actividad de creación de QR o código de barras
        btnCreateQrBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQrBarcodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
