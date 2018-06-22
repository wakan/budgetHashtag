package fr.budgethashtag.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.BudgetProvider;

import java.util.Objects;


public class TestActivity extends Activity implements View.OnClickListener {

    private Button btn;
    /** * A dummy counter */
    private int count;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        btn= findViewById(R.id.button);
        btn.setOnClickListener(this);
        btn.setText("Hello");
    }
    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        count++;
        btn.setText("Hello " + count);

        ContentResolver cr = getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(BudgetProvider.Budget.KEY_COL_LIB, "Food");
        cr.insert(BudgetProvider.CONTENT_URI,cv);

        Cursor cursor = cr.query(BudgetProvider.CONTENT_URI, null, null, null, null);
        int i = 0;
        while (Objects.requireNonNull(cursor).moveToNext()) {
            String lib = cursor.getString(cursor.getColumnIndex(BudgetProvider.Budget.KEY_COL_LIB));
            Toast.makeText(this, i++ + lib, Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

}
