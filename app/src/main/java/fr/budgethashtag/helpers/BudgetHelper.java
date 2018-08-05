package fr.budgethashtag.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;

public class BudgetHelper {

    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Budget.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Budget.KEY_COL_ID)));
        cv.put(Budget.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Budget.KEY_COL_LIB)));
        cv.put(Budget.KEY_COL_COLOR,
                c.getString(c.getColumnIndex(Budget.KEY_COL_COLOR)));
        cv.put(Budget.KEY_COL_PREVISIONNEL,
                c.getFloat(c.getColumnIndex(Budget.KEY_COL_PREVISIONNEL)));
        cv.put(Budget.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(Budget.KEY_COL_ID_PORTEFEUILLE)));
        if(c.getColumnCount() > 5) {
            cv.put(Budget.KEY_COL_EXP_SUM_MNT,
                    c.getInt(c.getColumnIndex(Budget.KEY_COL_EXP_SUM_MNT)));
            cv.put(Budget.KEY_COL_EXP_COUNT_MNT,
                    c.getInt(c.getColumnIndex(Budget.KEY_COL_EXP_COUNT_MNT)));
        }

        return cv;
    }

    public static Uri createBudget(Context context, long idPortefeuille, String libelle, Double montantPrevi, String color)
            throws OperationApplicationException {
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Budget.KEY_COL_LIB, libelle);
        cv.put(Budget.KEY_COL_PREVISIONNEL, montantPrevi);
        cv.put(Budget.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
        cv.put(Budget.KEY_COL_COLOR, color);
        Uri uriAdd = cr.insert(Budget.contentUriCollection(idPortefeuille),cv);
        if(uriAdd == null)
            throw new OperationApplicationException(context.getString(R.string.ex_msg_save_budget));
        return uriAdd;
    }

}
