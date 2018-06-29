package fr.budgethashtag.asynctask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.helpers.PortefeuilleHelper;

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
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        ContentResolver cr = contextRef.get().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Budget.KEY_COL_LIB, libelle);
        cv.put(Budget.KEY_COL_PREVISIONNEL, concurrency);
        cv.put(Budget.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
        Uri uriAdd = cr.insert(Budget.contentUriCollection(idPortefeuille),cv);
        if(uriAdd == null)
            try {
                throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_save_budget));
            } catch (OperationApplicationException e) {
                //TODO : Do better because it is not good here for catch exception
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        ((Activity)contextRef.get()).finish();
        Toast.makeText(contextRef.get(), "Ajout OK", Toast.LENGTH_SHORT).show();
    }
}
