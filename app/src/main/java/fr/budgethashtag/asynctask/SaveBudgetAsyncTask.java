package fr.budgethashtag.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.budgethashtag.R;

import java.lang.ref.WeakReference;

public class SaveBudgetAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Context> contextRef;
    private final String libelle;
    private final double concurrency;
    private final String color;

    public SaveBudgetAsyncTask(Context contextRef,
                               String libelle, double concurrency, String color) {
        this.contextRef = new WeakReference<>(contextRef);
        this.libelle = libelle;
        this.concurrency = concurrency;
        this.color = color;
    }

    @Override
    protected Void doInBackground(Void... params) {
        /*
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        try {
            BudgetHelper.createBudget(contextRef.get(), idPortefeuille, libelle, concurrency, color);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        */
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        ((Activity)contextRef.get()).finish();
        Toast.makeText(contextRef.get(), R.string.AjoutOK, Toast.LENGTH_SHORT).show();
    }
}
