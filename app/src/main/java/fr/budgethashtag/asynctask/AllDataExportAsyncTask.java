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
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;
import fr.budgethashtag.helpers.BudgetHelper;
import fr.budgethashtag.helpers.TransactionHelper;

public class AllDataExportAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> contextRef;

    public AllDataExportAsyncTask(WeakReference<Context> contextRef) {
        this.contextRef = contextRef;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        loadCachePortefeuille(cr);
        writeBudgetToCsv(cr);
        writeTransactionToCsv(cr);
        return null;
    }

    private void writeBudgetToCsv(ContentResolver cr) {
        try (Cursor c = cr.query(Portefeuille.contentUriCollection(),
                null, null, null, null)) {
            List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(c);
                Integer idPortefeuille = cv.getAsInteger(Budget.KEY_COL_ID_PORTEFEUILLE);
                String libPortefeuille = cachePortefeuille.get(idPortefeuille);
                cv.put(Budget.KEY_COL_ID_PORTEFEUILLE, libPortefeuille);
                ret.add(cv);
            }
        }
    }
    private void writeTransactionToCsv(ContentResolver cr) {
        try (Cursor c = cr.query(Portefeuille.contentUriCollection(),
                null, null, null, null)) {
            List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = TransactionHelper.extractContentValueFromCursor(c);
                Integer idPortefeuille = cv.getAsInteger(Transaction.KEY_COL_ID_PORTEFEUILLE);
                String libPortefeuille = cachePortefeuille.get(idPortefeuille);
                cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE, libPortefeuille);
                ret.add(cv);
            }
        }
    }

    private SparseArray<String> cachePortefeuille;
    private void loadCachePortefeuille(ContentResolver cr) {
        try (Cursor p = cr.query(Portefeuille.contentUriCollection(),
                null, null, null, null)) {
            cachePortefeuille = new SparseArray<>(Objects.requireNonNull(p).getCount());
            while (Objects.requireNonNull(p).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(p);
                cachePortefeuille.put(cv.getAsInteger(Portefeuille.KEY_COL_ID),
                        cv.getAsString(Portefeuille.KEY_COL_LIB));
            }
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
