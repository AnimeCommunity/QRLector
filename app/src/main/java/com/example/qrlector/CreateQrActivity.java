import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrlector.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateQrActivity extends AppCompatActivity {

    private EditText etQRText;
    private Button btnGenerateQR, btnSaveQR;
    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr); // Asegúrate de que este es el nombre correcto del layout XML

        etQRText = findViewById(R.id.et_qr_text);
        btnGenerateQR = findViewById(R.id.btn_generate_qr);
        btnSaveQR = findViewById(R.id.btn_save_qr);

        ivQRCode = new ImageView(this);  // ImageView dinámico para mostrar el QR
        addContentView(ivQRCode, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });

        btnSaveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCode();
            }
        });
    }

    private void generateQRCode() {
        String text = etQRText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "Ingrese texto o URL para el QR", Toast.LENGTH_SHORT).show();
            return;
        }

        ivQRCode = findViewById(R.id.iv_qr_code);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 600, 600);
            Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.RGB_565);
            for (int x = 0; x < 600; x++) {
                for (int y = 0; y < 600; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ivQRCode.setImageBitmap(bitmap); // Mostrar el QR en ImageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void saveQRCode() {
        // Aquí puedes agregar la lógica para guardar el QR en el almacenamiento del dispositivo
        Toast.makeText(this, "Funcionalidad de guardar pendiente", Toast.LENGTH_SHORT).show();
    }
}
