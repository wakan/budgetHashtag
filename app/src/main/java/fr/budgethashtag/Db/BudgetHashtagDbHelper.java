package fr.budgethashtag.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.BudgetProvider;

public class BudgetHashtagDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "BudgetHashtagDbHelper";
    private static final String DB_NAME = "budgetHashtag.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_BUDGET  = "create table " + BudgetProvider.TABLE_NAME + " ("
            + BudgetProvider.Budget.KEY_COL_ID + " integer primary key autoincrement, "
            + BudgetProvider.Budget.KEY_COL_LIB + " TEXT(55) not null, "
            + BudgetProvider.Budget.KEY_COL_COLOR + " TEXT(8), "
            + BudgetProvider.Budget.KEY_COL_DT_DEB + " DATE, "
            + BudgetProvider.Budget.KEY_COL_DT_FIN + " DATE, "
            + BudgetProvider.Budget.KEY_COL_PREVISIONNEL + " REAL, "
            + BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE + " integer)"
            ;
    private static final String DROP_TABLE_BUDGET  = "DROP TABLE IF EXISTS "  + BudgetProvider.TABLE_NAME;
    private final Context context;

    public BudgetHashtagDbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUDGET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, context.getResources().
                getString(R.string.warning_log_maj_bd, oldVersion, newVersion));
        db.execSQL(DROP_TABLE_BUDGET);
        onCreate(db);
    }
}
