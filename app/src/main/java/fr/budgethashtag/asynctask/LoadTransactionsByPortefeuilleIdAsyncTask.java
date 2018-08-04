package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.budgethashtag.basecolumns.Transaction;
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
        /*
        ContentResolver cr = contextRef.get().getContentResolver();
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        Cursor c = cr.query(Transaction.contentUriCollection(idPortefeuille),
                null, null, null, null);
        if(null == c)
            return new ArrayList<>();
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
        */
        return null;
    }
    @Override
    protected void onPostExecute(List<ContentValues> c){
        super.onPostExecute(c);
        listener.onLoadTransactionsByPortefeuilleId(c);
    }

}
