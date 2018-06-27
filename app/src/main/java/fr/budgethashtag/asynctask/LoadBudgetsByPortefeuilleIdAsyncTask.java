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
        SharedPreferences appSharedPref =  contextRef.get().getSharedPreferences("BudgetHashtagSharedPref", Context.MODE_PRIVATE);
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE + "=?";
        String[] whereParam = {String.valueOf(
                appSharedPref.getInt(CreateDefaultPortefeuilleIfNotExistAsyncTask.ID_PORTEFEULLE_SELECTED, 0)
        )};
        Cursor c = cr.query(BudgetProvider.CONTENT_URI,
                null, where, whereParam, null);
        List<ContentValues> ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
        try {
            while (Objects.requireNonNull(c).moveToNext()) {
                ContentValues cv = BudgetHelper.extractContentValueFromCursor(c);
                ret.add(cv);
            }
        } finally {
            if(null != c)
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
