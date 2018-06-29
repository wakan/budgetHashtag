package fr.budgethashtag.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;

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

}
