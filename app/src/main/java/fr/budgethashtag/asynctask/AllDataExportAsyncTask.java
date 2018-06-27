package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.BudgetProvider;
import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.contentprovider.TransactionProvider;
import fr.budgethashtag.helpers.BudgetHelper;
import fr.budgethashtag.helpers.TransactionHelper;

public class AllDataExportAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> contextRef;

    public AllDataExportAsyncTask(WeakReference<Context> contextRef) {
        this.contextRef = contextRef;
    }

    @Override
    protected Void doInBackground(Void... params) {
        SharedPreferences appSharedPref = contextRef.get().getSharedPreferences("BudgetHashtagSharedPref", Context.MODE_PRIVATE);
        ContentResolver cr = contextRef.get().getContentResolver();
        loadCachePortefeuille(cr);
        writeBudgetToCsv(cr);
        writeTransactionToCsv(cr);
        return null;
    }

    private void writeBudgetToCsv(ContentResolver cr) {
        try (Cursor c = cr.query(BudgetProvider.CONTENT_URI,
                null, null, null, null)) {
            List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(c);
                Integer idPortefeuille = cv.getAsInteger(BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE);
                String libPortefeuille = cachePortefeuille.get(idPortefeuille);
                cv.put(BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE, libPortefeuille);
                ret.add(cv);
            }
        }
    }
    private void writeTransactionToCsv(ContentResolver cr) {
        Cursor c = cr.query(TransactionProvider.CONTENT_URI,
                null, null, null, null);
        try {
            List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = TransactionHelper.extractContentValueFromCursor(c);
                Integer idPortefeuille = cv.getAsInteger(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE);
                String libPortefeuille = cachePortefeuille.get(idPortefeuille);
                cv.put(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE, libPortefeuille);
                ret.add(cv);
            }
        } finally {
            if(null != c)
                c.close();
        }
    }

    private SparseArray<String> cachePortefeuille;
    private void loadCachePortefeuille(ContentResolver cr) {
        Cursor p = cr.query(PortefeuilleProvider.CONTENT_URI,
                    null, null, null, null);
        try {
            cachePortefeuille = new SparseArray<>(Objects.requireNonNull(p).getCount());
            while (Objects.requireNonNull(p).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(p);
                cachePortefeuille.put(cv.getAsInteger(PortefeuilleProvider.Portefeuille.KEY_COL_ID),
                        cv.getAsString(PortefeuilleProvider.Portefeuille.KEY_COL_LIB));
            }
        } finally {
            if(null != p)
                p.close();
        }
    }


    @Override
    protected void onPostExecute(Void params){
        super.onPostExecute(params);
        Toast.makeText(contextRef.get(),
                contextRef.get().getString(R.string.toast_export_done),
                Toast.LENGTH_LONG).show();
    }

}
