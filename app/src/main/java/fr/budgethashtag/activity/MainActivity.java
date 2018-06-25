package fr.budgethashtag.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask;
import fr.budgethashtag.asynctask.LoadBudgetsByPortefeuilleIdAsyncTask;
import fr.budgethashtag.asynctask.LoadPortefeuilleByIdAsyncTask;
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAsyncTask;
import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.fragment.BudgetFragment;
import fr.budgethashtag.fragment.TransactionFragment;
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback;
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback;
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback;
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback;

public class MainActivity extends Activity
    implements CreateDefaultPortefeuilleIfNotExistCallback,
        LoadPortefeuilleByIdCallback,
        LoadTransactionsByPortefeuilleIdCallback,
        LoadBudgetsByPortefeuilleIdCallback
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
        new LoadTransactionsByPortefeuilleIdAsyncTask(this, this, idPortefeuille)
                .execute();
    }
    private void loadBudgets(int idPortefeuille) {
        new LoadBudgetsByPortefeuilleIdAsyncTask(this, this, idPortefeuille)
                .execute();
    }
    @Override
    public void onLoadPortefeuilleById(ContentValues contentValues) {
        ((TextView)findViewById(R.id.lbl_title)).setText(
                contentValues.getAsString(PortefeuilleProvider.Portefeuille.KEY_COL_LIB));
    }
    @Override
    public void onLoadTransactionsByPortefeuilleId(List<ContentValues> contentValuesList) {
        TransactionFragment transactionFragment = (TransactionFragment)getFragmentManager().findFragmentById(R.id.frg_transaction);
        transactionFragment.onLoadTransactionsByPortefeuilleId(contentValuesList);
    }

    @Override
    public void onLoadBudgetsByPortefeuilleIdCallback(List<ContentValues> contentValuesList) {
        BudgetFragment budgetFragment = (BudgetFragment)getFragmentManager().findFragmentById(R.id.frg_budget);
        budgetFragment.onLoadBudgetsByPortefeuilleIdCallback(contentValuesList);
    }

}
