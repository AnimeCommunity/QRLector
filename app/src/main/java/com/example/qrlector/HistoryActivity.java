package com.example.qrlector;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.history_list);
        dbHelper = new DatabaseHelper(this);

        ArrayList<String> qrResults = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.QREntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            qrResults.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.QREntry.COLUMN_NAME_RESULT)));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, qrResults);
        listView.setAdapter(adapter);
    }
}
