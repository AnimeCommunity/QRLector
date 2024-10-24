package com.example.qrlector;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateQrActivity extends AppCompatActivity {

    private EditText etQRText;
    private Button btnGenerateQR, btnSaveQR, btnShareQR;
    private ImageView ivQRCode;
    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final String CHANNEL_ID = "qr_notifications";
    private static final int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        etQRText = findViewById(R.id.et_qr_text);
        btnGenerateQR = findViewById(R.id.btn_generate_qr);
        btnSaveQR = findViewById(R.id.btn_save_qr);
        btnShareQR = findViewById(R.id.btn_share_qr);
        ivQRCode = findViewById(R.id.iv_qr_code);

        // Configurar los botones
        btnGenerateQR.setOnClickListener(v -> generateQRCode());
        btnSaveQR.setOnClickListener(v -> checkPermissionAndSave());
        btnShareQR.setOnClickListener(v -> shareQRCode());

        // Crear el canal de notificaciones
        createNotificationChannel();
    }

    private void generateQRCode() {
        String text = etQRText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Introduce un texto o URL para el QR", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 600, 600);
            Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.RGB_565);

            for (int x = 0; x < 600; x++) {
                for (int y = 0; y < 600; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ivQRCode.setImageBitmap(bitmap);

            // Verificar si las notificaciones están habilitadas y enviar una notificación
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean notificationsEnabled = prefs.getBoolean("notifications", true);
            if (notificationsEnabled) {
                sendNotification("Código QR creado", "Tu código QR ha sido creado exitosamente.");
            }

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el código QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissionAndSave() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        } else {
            saveQRCode();
        }
    }

    private void saveQRCode() {
        BitmapDrawable drawable = (BitmapDrawable) ivQRCode.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "Primero genera el código QR", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = drawable.getBitmap();
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "QRImages");

        if (!directory.exists() && !directory.mkdirs()) {
            Toast.makeText(this, "No se pudo crear el directorio para guardar el QR", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "QR_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(this, "Código QR guardado en la galería", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el código QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQRCode() {
        BitmapDrawable drawable = (BitmapDrawable) ivQRCode.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "Primero genera el código QR", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = drawable.getBitmap();

        try {
            File file = new File(getExternalCacheDir(), "shared_qr.png");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }

            Uri uri = FileProvider.getUriForFile(this, "com.example.qrlector.fileprovider", file);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Compartir código QR"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al compartir el código QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificaciones QR";
            String description = "Canal para notificaciones de creación de QR";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name) // Asegúrate de tener este ícono en tu drawable
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Mostrar la notificación
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveQRCode();
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede guardar el código QR.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
