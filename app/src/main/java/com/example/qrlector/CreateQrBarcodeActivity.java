package com.example.qrlector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CreateQrBarcodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_barcode);

        // Inicializa los botones aquí
        Button btnCreateQr = findViewById(R.id.btn_create_qr);
        Button btnCreateBarcode = findViewById(R.id.btn_create_barcode);

        // Configura el listener para el botón de crear QR
        btnCreateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateQrBarcodeActivity.this, CreateQrActivity.class);
                startActivity(intent);
            }
        });

        // Configura el listener para el botón de crear código de barras
        btnCreateBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateQrBarcodeActivity.this, CreateBarcodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
