package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Objects;

import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback;

public class LoadPortefeuilleByIdAsyncTask extends AsyncTask<Void, Void, ContentValues> {

    private final WeakReference<Context> contextRef;
    private final LoadPortefeuilleByIdCallback listener;
    public LoadPortefeuilleByIdAsyncTask(Context context,
                                         LoadPortefeuilleByIdCallback listener) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
    }
    @Override
    protected ContentValues doInBackground(Void... params) {
        SharedPreferences appSharedPref =  contextRef.get().getSharedPreferences("BudgetHashtagSharedPref", Context.MODE_PRIVATE);
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = PortefeuilleProvider.Portefeuille.KEY_COL_ID + "=?";
        String[] whereParam = {String.valueOf(
                appSharedPref.getInt(CreateDefaultPortefeuilleIfNotExistAsyncTask.ID_PORTEFEULLE_SELECTED, 0)
        )};
        ContentValues cv = new ContentValues();
        try (Cursor c = cr.query(PortefeuilleProvider.CONTENT_URI,
                null, where, whereParam, null)) {
            Objects.requireNonNull(c).moveToNext();
            cv = PortefeuilleHelper.extractContentValueFromCursor(c);
        }
        return cv;
    }
    @Override
    protected void onPostExecute(ContentValues c){
        super.onPostExecute(c);
        listener.onLoadPortefeuilleById(c);
    }


}
