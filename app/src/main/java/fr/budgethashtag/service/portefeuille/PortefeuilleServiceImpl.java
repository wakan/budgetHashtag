package fr.budgethashtag.service.portefeuille;


import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.helpers.PortefeuilleHelper;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class PortefeuilleServiceImpl implements PortefeuilleService {
    public   static final String ID_PORTEFEULLE_SELECTED =  "IdPortefeuilleSelected";

    private static final int ID_NOT_EXIST = -1;

    @Override
    public Long getOrCreateDefaultPortefeuilleIfNotExist(final Context context)
            throws OperationApplicationException {
        SharedPreferences appSharedPref =  context.getSharedPreferences(
                BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
        if(appSharedPref.contains(ID_PORTEFEULLE_SELECTED)) {
            return appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        } else {
            long idPortefeuille = createDefaultPortefeuille(context);
            SharedPreferences.Editor editor = appSharedPref.edit();
            editor.putLong(ID_PORTEFEULLE_SELECTED, idPortefeuille);
            editor.apply();
            return idPortefeuille;
        }
    }

    @Override
    public Long createDefaultPortefeuille(final Context context)
            throws OperationApplicationException {
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_LIB, context.getString(R.string.portefeuille_default_lib));
        Uri uriAdd = cr.insert(Portefeuille.contentUriCollection(), cv);
        if (uriAdd == null)
            throw new OperationApplicationException(context.getString(R.string.ex_msg_create_default_portefeuille));
        return Long.parseLong(Objects.requireNonNull(uriAdd).getPathSegments().get(1));
    }

    @Override
    public long getIdPortefeuilleFromSharedPref(final Context context) {
        SharedPreferences appSharedPref =  context.getSharedPreferences(
                BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
        return appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
    }

    @Override
    public ContentValues getPortefeuilleById(final Context context) {
        ContentResolver cr = context.getContentResolver();
        long idPortefeuille = getIdPortefeuilleFromSharedPref(context);
        ContentValues cv;
        try (Cursor c = cr.query(Portefeuille.contentUriItem(idPortefeuille),
                null, null, null, null)) {
            Objects.requireNonNull(c).moveToNext();
            cv = PortefeuilleHelper.extractContentValueFromCursor(c);
        }
        return cv;
    }

}
