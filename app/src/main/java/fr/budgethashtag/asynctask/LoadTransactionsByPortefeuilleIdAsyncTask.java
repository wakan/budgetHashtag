package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.budgethashtag.contentprovider.TransactionProvider;
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback;

public class LoadTransactionsByPortefeuilleIdAsyncTask extends AsyncTask<Void, Void, List<ContentValues>> {

    private final WeakReference<Context> contextRef;
    private final LoadTransactionsByPortefeuilleIdCallback listener;
    private final int idPortefeuilleSelected;
    public LoadTransactionsByPortefeuilleIdAsyncTask(Context context,
                                             LoadTransactionsByPortefeuilleIdCallback listener,
                                             int idPortefeuilleSelected ) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
        this.idPortefeuilleSelected = idPortefeuilleSelected;
    }
    @Override
    protected List<ContentValues> doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE + "=?";
        String[] whereParam = {String.valueOf(idPortefeuilleSelected)};
        Cursor c = cr.query(TransactionProvider.CONTENT_URI,
                null, where, whereParam, null);
        List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
        while (Objects.requireNonNull(c).moveToNext()) {
            ContentValues cv = extractContentValueFromCursor(c);
            ret.add(cv);
        }
        c.close();
        return ret;
    }
    @Override
    protected void onPostExecute(List<ContentValues> c){
        super.onPostExecute(c);
        listener.onLoadTransactionsByPortefeuilleId(c);
    }
    @NonNull
    private ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(TransactionProvider.Transaction.KEY_COL_ID,
                c.getInt(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_ID)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LIB,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LIB)));
        cv.put(TransactionProvider.Transaction.KEY_COL_DT_VALEUR,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_DT_VALEUR)));
        cv.put(TransactionProvider.Transaction.KEY_COL_MONTANT,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_MONTANT)));
        cv.put(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_TIME,
                c.getLong(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_TIME)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_PROVIDER,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_PROVIDER)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_ACCURACY,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_ACCURACY)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_ALTITUDE,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_ALTITUDE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_LATITUDE,
                c.getDouble(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_LATITUDE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_LONGITUDE,
                c.getDouble(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_LONGITUDE)));
        return cv;
    }

}
