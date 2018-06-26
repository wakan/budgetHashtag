package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.BudgetProvider;

public class SaveBudgetAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Context> contextRef;
    private final String libelle;
    private final Float concurrency;
    private final String color;

    public SaveBudgetAsyncTask(Context contextRef,
                               String libelle, Float concurrency, String color) {
        this.contextRef = new WeakReference<>(contextRef);
        this.libelle = libelle;
        this.concurrency = concurrency;
        this.color = color;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(BudgetProvider.Budget.KEY_COL_LIB, libelle);
        cv.put(BudgetProvider.Budget.KEY_COL_PREVISIONNEL, concurrency);
        Uri uriAdd = cr.insert(BudgetProvider.CONTENT_URI,cv);
        if(uriAdd == null)
            try {
                throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_save_budget));
            } catch (OperationApplicationException e) {
                //TODO : Do better because it is not good here for catch exception
                e.printStackTrace();
            }
        return null;
    }
}
