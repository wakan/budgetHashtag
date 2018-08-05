package fr.budgethashtag.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Transaction;

import java.util.Date;

public class TransactionHelper {
    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Transaction.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Transaction.KEY_COL_ID)));
        cv.put(Transaction.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_LIB)));
        cv.put(Transaction.KEY_COL_DT_VALEUR,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_DT_VALEUR)));
        cv.put(Transaction.KEY_COL_MONTANT,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_MONTANT)));
        cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(Transaction.KEY_COL_ID_PORTEFEUILLE)));
        cv.put(Transaction.KEY_COL_LOCATION_PROVIDER,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_LOCATION_PROVIDER)));
        cv.put(Transaction.KEY_COL_LOCATION_ACCURACY,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_LOCATION_ACCURACY)));
        cv.put(Transaction.KEY_COL_LOCATION_ALTITUDE,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_LOCATION_ALTITUDE)));
        cv.put(Transaction.KEY_COL_LOCATION_LATITUDE,
                c.getDouble(c.getColumnIndex(Transaction.KEY_COL_LOCATION_LATITUDE)));
        cv.put(Transaction.KEY_COL_LOCATION_LONGITUDE,
                c.getDouble(c.getColumnIndex(Transaction.KEY_COL_LOCATION_LONGITUDE)));
        return cv;
    }

    public static Uri createTransaction(Context context, long id, long idPortefeuille, String libelle, Double montant,
                                        Date dateTransac, String locationProvider, Double locationAccuracy,
                                        Double locationAltitude, Double locationLatitude, Double locationLongitude)
           throws OperationApplicationException {
            ContentResolver cr = context.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
            cv.put(Transaction.KEY_COL_LIB, libelle);
            cv.put(Transaction.KEY_COL_DT_VALEUR, dateTransac.getTime());
            cv.put(Transaction.KEY_COL_MONTANT, montant);
            Uri uri;
            if(id > 0) {
                int idUp = cr.update(Transaction.contentUriItem(idPortefeuille, id), cv, null, null);
                uri = Transaction.contentUriItem(idPortefeuille, idUp);
            } else {
            if (null != locationProvider) {
                cv.put(Transaction.KEY_COL_LOCATION_PROVIDER, locationProvider);
                cv.put(Transaction.KEY_COL_LOCATION_ACCURACY, locationAccuracy);
                cv.put(Transaction.KEY_COL_LOCATION_ALTITUDE, locationAltitude);
                cv.put(Transaction.KEY_COL_LOCATION_LATITUDE, locationLatitude);
                cv.put(Transaction.KEY_COL_LOCATION_LONGITUDE, locationLongitude);
            }
                 uri = cr.insert(Transaction.contentUriCollection(idPortefeuille), cv);
            }
            if(uri == null)
                throw new OperationApplicationException(context.getString(R.string.ex_msg_save_budget));
            return uri;
    }

}
