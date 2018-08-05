package fr.budgethashtag.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback;

import java.lang.ref.WeakReference;
import java.util.List;

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
        /*
        ContentResolver cr = contextRef.get().getContentResolver();
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        Cursor c = cr.query(Budget.contentUriCollection(idPortefeuille),
                null, null, null, null);
        if(c == null)
            return new ArrayList<>();
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
        */
        return null;
    }
    @Override
    protected void onPostExecute(List<ContentValues> c){
        super.onPostExecute(c);
        listener.onLoadBudgetsByPortefeuilleIdCallback(c);
    }

}
