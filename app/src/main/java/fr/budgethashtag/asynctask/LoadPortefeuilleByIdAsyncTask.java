package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Objects;

import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback;

public class LoadPortefeuilleByIdAsyncTask extends AsyncTask<Void, Void, ContentValues> {

    private final WeakReference<Context> contextRef;
    private final LoadPortefeuilleByIdCallback listener;
    private final int idPortefeuilleSelected;
    public LoadPortefeuilleByIdAsyncTask(Context context,
                                         LoadPortefeuilleByIdCallback listener,
                                         int idPortefeuilleSelected ) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
        this.idPortefeuilleSelected = idPortefeuilleSelected;
    }
    @Override
    protected ContentValues doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = PortefeuilleProvider.Portefeuille.KEY_COL_ID + "=?";
        String[] whereParam = {String.valueOf(idPortefeuilleSelected)};
        Cursor c = cr.query(PortefeuilleProvider.CONTENT_URI,
                null, where, whereParam, null);
        Objects.requireNonNull(c).moveToNext();
        ContentValues cv = new ContentValues();
        cv.put(PortefeuilleProvider.Portefeuille.KEY_COL_ID,
                c.getInt(c.getColumnIndex(PortefeuilleProvider.Portefeuille.KEY_COL_ID)));
        cv.put(PortefeuilleProvider.Portefeuille.KEY_COL_LIB,
                c.getString(c.getColumnIndex(PortefeuilleProvider.Portefeuille.KEY_COL_LIB)));
        c.close();
        return cv;
    }
    @Override
    protected void onPostExecute(ContentValues c){
        super.onPostExecute(c);
        listener.onLoadPortefeuilleById(c);
    }

}
