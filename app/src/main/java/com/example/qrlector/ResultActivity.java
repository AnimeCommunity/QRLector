package com.example.qrlector;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView resultTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Resultado");
        resultTextView = findViewById(R.id.result_text);
        dbHelper = new DatabaseHelper(this);

        String qrResult = getIntent().getStringExtra("qr_result");
        resultTextView.setText(qrResult);

        // guardar en la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.QREntry.COLUMN_NAME_RESULT, qrResult);
        db.insert(DatabaseContract.QREntry.TABLE_NAME, null, values);
    }
}
