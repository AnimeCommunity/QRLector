import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrlector.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateBarcodeActivity extends AppCompatActivity {

    private EditText etBarcodeText;
    private Button btnGenerateBarcode, btnSaveBarcode;
    private ImageView ivBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_barcode); // Asegúrate de que este es el nombre correcto del layout XML

        etBarcodeText = findViewById(R.id.et_barcode_text);
        btnGenerateBarcode = findViewById(R.id.btn_generate_barcode);
        btnSaveBarcode = findViewById(R.id.btn_save_barcode);
        ivBarcode = findViewById(R.id.iv_barcode); // Esta asignación debe estar dentro de onCreate

        btnGenerateBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBarcode();
            }
        });

        btnSaveBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBarcode();
            }
        });
    }

    private void generateBarcode() {
        String text = etBarcodeText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "Introduce un texto para el código de barras", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.CODE_128, 600, 300);
            Bitmap bitmap = Bitmap.createBitmap(600, 300, Bitmap.Config.RGB_565);
            for (int x = 0; x < 600; x++) {
                for (int y = 0; y < 300; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ivBarcode.setImageBitmap(bitmap); // Asegúrate de tener un ImageView en el layout para mostrar el código
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void saveBarcode() {
        // Aquí puedes agregar la lógica para guardar el código de barras
        Toast.makeText(this, "Funcionalidad de guardar pendiente", Toast.LENGTH_SHORT).show();
    }
}
