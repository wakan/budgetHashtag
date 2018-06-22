package fr.budgethashtag.activity;

import android.app.Activity;
import android.os.Bundle;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask;
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback;

public class MainActivity extends Activity
    implements CreateDefaultPortefeuilleIfNotExistCallback
{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CreateDefaultPortefeuilleIfNotExistAsyncTask(this, this).execute();
    }

    @Override
    public void onCreateDefaultPortefeuilleIfNotExist(int idPortefeuille) {
        loadPortefeuilleName(idPortefeuille);
        loadTransactions(idPortefeuille);
        loadBudgets(idPortefeuille);
    }

    private void loadPortefeuilleName(int idPortefeuille) {
    }
    private void loadTransactions(int idPortefeuille) {

    }
    private void loadBudgets(int idPortefeuille) {
    }


}
