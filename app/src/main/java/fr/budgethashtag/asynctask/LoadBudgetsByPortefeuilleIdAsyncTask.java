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

import fr.budgethashtag.contentprovider.BudgetProvider;
import fr.budgethashtag.helpers.BudgetHelper;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback;

public class LoadBudgetsByPortefeuilleIdAsyncTask extends AsyncTask<Void, Void, List<ContentValues>> {

    private final WeakReference<Context> contextRef;
    private final LoadBudgetsByPortefeuilleIdCallback listener;
    public LoadBudgetsByPortefeuilleIdAsyncTask(Context context,
                                                LoadBudgetsByPortefeuilleIdCallback listener) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
    }
    @Override
    protected List<ContentValues> doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        int idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        Cursor c = cr.query(BudgetHashtagProvider.Budget.CONTENT_URI,
                null, null, null, null);
        List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
        try {
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(c);
                ret.add(cv);
            }
        } finally {
            c.close();
        }
        return ret;
    }
    @Override
    protected void onPostExecute(List<ContentValues> c){
        super.onPostExecute(c);
        listener.onLoadBudgetsByPortefeuilleIdCallback(c);
    }

}
