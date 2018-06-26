package fr.budgethashtag.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.SaveBudgetAsyncTask;

public class AddBudgetActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        findViewById(R.id.btn_save_budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libelle = ((EditText)findViewById(R.id.txt_libelle)).getText().toString();
                String strConcurrency = ((EditText)findViewById(R.id.txt_previsionnel)).getText().toString();
                Float concurrency = Float.parseFloat(strConcurrency);
                String color = "";
                new SaveBudgetAsyncTask(AddBudgetActivity.this, libelle, concurrency, color).execute();
            }
        });
    }
}
