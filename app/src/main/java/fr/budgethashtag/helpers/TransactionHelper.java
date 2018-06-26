package fr.budgethashtag.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import fr.budgethashtag.contentprovider.TransactionProvider;

public class TransactionHelper {
    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(TransactionProvider.Transaction.KEY_COL_ID,
                c.getInt(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_ID)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LIB,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LIB)));
        cv.put(TransactionProvider.Transaction.KEY_COL_DT_VALEUR,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_DT_VALEUR)));
        cv.put(TransactionProvider.Transaction.KEY_COL_MONTANT,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_MONTANT)));
        cv.put(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_TIME,
                c.getLong(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_TIME)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_PROVIDER,
                c.getString(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_PROVIDER)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_ACCURACY,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_ACCURACY)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_ALTITUDE,
                c.getFloat(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_ALTITUDE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_LATITUDE,
                c.getDouble(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_LATITUDE)));
        cv.put(TransactionProvider.Transaction.KEY_COL_LOCATION_LONGITUDE,
                c.getDouble(c.getColumnIndex(TransactionProvider.Transaction.KEY_COL_LOCATION_LONGITUDE)));
        return cv;
    }

}
