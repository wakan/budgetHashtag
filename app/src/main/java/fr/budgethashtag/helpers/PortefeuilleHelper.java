package fr.budgethashtag.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import fr.budgethashtag.contentprovider.PortefeuilleProvider;

public class PortefeuilleHelper {

    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(PortefeuilleProvider.Portefeuille.KEY_COL_ID,
                c.getInt(c.getColumnIndex(PortefeuilleProvider.Portefeuille.KEY_COL_ID)));
        cv.put(PortefeuilleProvider.Portefeuille.KEY_COL_LIB,
                c.getString(c.getColumnIndex(PortefeuilleProvider.Portefeuille.KEY_COL_LIB)));
        return cv;
    }

}
