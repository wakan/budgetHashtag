package fr.budgethashtag.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import fr.budgethashtag.basecolumns.Portefeuille;

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


}
