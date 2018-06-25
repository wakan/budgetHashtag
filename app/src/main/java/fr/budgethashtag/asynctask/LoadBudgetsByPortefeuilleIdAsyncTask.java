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

import fr.budgethashtag.contentprovider.BudgetProvider;
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback;

public class LoadBudgetsByPortefeuilleIdAsyncTask extends AsyncTask<Void, Void, List<ContentValues>> {

    private final WeakReference<Context> contextRef;
    private final LoadBudgetsByPortefeuilleIdCallback listener;
    private final int idPortefeuilleSelected;
    public LoadBudgetsByPortefeuilleIdAsyncTask(Context context,
                                                LoadBudgetsByPortefeuilleIdCallback listener,
                                                int idPortefeuilleSelected ) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
        this.idPortefeuilleSelected = idPortefeuilleSelected;
    }
    @Override
    protected List<ContentValues> doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        String where = BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE + "=?";
        String[] whereParam = {String.valueOf(idPortefeuilleSelected)};
        Cursor c = cr.query(BudgetProvider.CONTENT_URI,
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
        listener.onLoadBudgetsByPortefeuilleIdCallback(c);
    }
    @NonNull
    private ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(BudgetProvider.Budget.KEY_COL_ID,
                c.getInt(c.getColumnIndex(BudgetProvider.Budget.KEY_COL_ID)));
        cv.put(BudgetProvider.Budget.KEY_COL_LIB,
                c.getString(c.getColumnIndex(BudgetProvider.Budget.KEY_COL_LIB)));
        cv.put(BudgetProvider.Budget.KEY_COL_COLOR,
                c.getString(c.getColumnIndex(BudgetProvider.Budget.KEY_COL_COLOR)));
        cv.put(BudgetProvider.Budget.KEY_COL_PREVISIONNEL,
                c.getFloat(c.getColumnIndex(BudgetProvider.Budget.KEY_COL_PREVISIONNEL)));
        cv.put(BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE)));
        return cv;
    }

}
