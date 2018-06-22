package fr.budgethashtag.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask;
import fr.budgethashtag.asynctask.LoadPortefeuilleByIdAsyncTask;
import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback;
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback;

public class MainActivity extends Activity
    implements CreateDefaultPortefeuilleIfNotExistCallback,
        LoadPortefeuilleByIdCallback
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
        new LoadPortefeuilleByIdAsyncTask(this, this, idPortefeuille).execute();
    }
    private void loadTransactions(int idPortefeuille) {

    }
    private void loadBudgets(int idPortefeuille) {
    }
    @Override
    public void onLoadPortefeuilleById(ContentValues contentValues) {
        ((TextView)findViewById(R.id.lbl_title)).setText(
                contentValues.getAsString(PortefeuilleProvider.Portefeuille.KEY_COL_LIB));
    }
}
