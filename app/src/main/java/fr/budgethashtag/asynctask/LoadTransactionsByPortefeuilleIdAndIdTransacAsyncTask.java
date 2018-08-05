package fr.budgethashtag.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdAndIdTransacCallback;

import java.lang.ref.WeakReference;

public class LoadTransactionsByPortefeuilleIdAndIdTransacAsyncTask extends AsyncTask<Void, Void, ContentValues> {

    private final WeakReference<Context> contextRef;
    private final LoadTransactionsByPortefeuilleIdAndIdTransacCallback listener;
    private final int id;
    public LoadTransactionsByPortefeuilleIdAndIdTransacAsyncTask(Context context,
                   LoadTransactionsByPortefeuilleIdAndIdTransacCallback listener,
                   int id) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
        this.id  = id;
    }
    @Override
    protected ContentValues doInBackground(Void... params) {
        /*
        ContentResolver cr = contextRef.get().getContentResolver();
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        try (Cursor c = cr.query(Transaction.contentUriItem(idPortefeuille, id),
                null, null, null, null)) {
            Objects.requireNonNull(c).moveToNext();
            return TransactionHelper.extractContentValueFromCursor(c);
        }
        */
        return null;
    }
    @Override
    protected void onPostExecute(ContentValues c){
        super.onPostExecute(c);
        listener.onLoadTransactionsByPortefeuilleIdAndIdTransac(c);
    }

}
