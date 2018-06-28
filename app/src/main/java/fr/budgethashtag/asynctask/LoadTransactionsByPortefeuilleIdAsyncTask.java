package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.budgethashtag.contentprovider.TransactionProvider;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.helpers.TransactionHelper;
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback;

public class LoadTransactionsByPortefeuilleIdAsyncTask extends AsyncTask<Void, Void, List<ContentValues>> {

    private final WeakReference<Context> contextRef;
    private final LoadTransactionsByPortefeuilleIdCallback listener;
    public LoadTransactionsByPortefeuilleIdAsyncTask(Context context,
                                             LoadTransactionsByPortefeuilleIdCallback listener) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
    }
    @Override
    protected List<ContentValues> doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE + "=?";
        int idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        String[] whereParam = {String.valueOf(idPortefeuille)};
        Cursor c = cr.query(TransactionProvider.CONTENT_URI,
                null, where, whereParam, null);
        List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
        try {
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = TransactionHelper.extractContentValueFromCursor(c);
                ret.add(cv);
            }
        }
        finally {
            c.close();
        }
        return ret;
    }
    @Override
    protected void onPostExecute(List<ContentValues> c){
        super.onPostExecute(c);
        listener.onLoadTransactionsByPortefeuilleId(c);
    }

}
