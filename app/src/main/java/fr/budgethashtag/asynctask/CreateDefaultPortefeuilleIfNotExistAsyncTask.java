package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Objects;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.helpers.UriHelper;
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback;

public class CreateDefaultPortefeuilleIfNotExistAsyncTask extends AsyncTask<Void, Void, Long> {

    private final WeakReference<Context> contextRef;
    private final CreateDefaultPortefeuilleIfNotExistCallback listener;
    public   static final String ID_PORTEFEULLE_SELECTED =  "IdPortefeuilleSelected";

    public CreateDefaultPortefeuilleIfNotExistAsyncTask(Context context,
                                       CreateDefaultPortefeuilleIfNotExistCallback listener ) {
        this.contextRef = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected Long doInBackground(Void... params) {
        SharedPreferences appSharedPref =  contextRef.get().getSharedPreferences("BudgetHashtagSharedPref", Context.MODE_PRIVATE);
        if(appSharedPref.contains(ID_PORTEFEULLE_SELECTED)) {
            return appSharedPref.getInt(ID_PORTEFEULLE_SELECTED, -1);
        } else {
            long idPortefeuille = createDefaultPortefeuille();
            SharedPreferences.Editor editor = appSharedPref.edit();
            editor.putInt(ID_PORTEFEULLE_SELECTED, idPortefeuille);
            editor.apply();
            return idPortefeuille;
        }
    }
    @Override
    protected void onPostExecute(Integer i){
        super.onPostExecute(i);
        listener.onCreateDefaultPortefeuilleIfNotExist(i);
    }

    private int createDefaultPortefeuille() {
        ContentResolver cr = contextRef.get().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(PortefeuilleProvider.Portefeuille.KEY_COL_LIB, contextRef.get().getString(R.string.portefeuille_default_lib));
        Uri uriAdd = cr.insert(BudgetHashtagProvider.Portefeuille.contentUriCollection(),cv);
        if(uriAdd == null)
        try {
            throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_create_default_portefeuille));
        } catch (OperationApplicationException e) {
            //TODO : Do better because it is not good here for catch exception
            e.printStackTrace();
        }
        return UriHelper.getIdFromContentUri(uriAdd);
    i

}
