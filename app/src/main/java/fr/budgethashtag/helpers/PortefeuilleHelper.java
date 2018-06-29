package fr.budgethashtag.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;

import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;

import java.lang.ref.WeakReference;

public class PortefeuilleHelper {

    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Portefeuille.KEY_COL_ID)));
        cv.put(Portefeuille.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Portefeuille.KEY_COL_LIB)));
        return cv;
    }


    public static long getIdPortefeuilleFromSharedPref(WeakReference<Context> contextRef) {
        SharedPreferences appSharedPref =  contextRef.get().getSharedPreferences("BudgetHashtagSharedPref", Context.MODE_PRIVATE);
        long idPortefeuille = appSharedPref.getLong(CreateDefaultPortefeuilleIfNotExistAsyncTask.ID_PORTEFEULLE_SELECTED, 0);
        return idPortefeuille;
    }

}
