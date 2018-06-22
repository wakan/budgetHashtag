package fr.budgethashtag.activity;

import android.app.Activity;
import android.os.Bundle;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CreateDefaultPortefeuilleIfNotExistAsyncTask(this).execute();
    }
}
