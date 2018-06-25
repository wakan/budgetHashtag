package fr.budgethashtag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.budgethashtag.R;
import fr.budgethashtag.contentprovider.BudgetProvider;
import fr.budgethashtag.contentprovider.PortefeuilleProvider;
import fr.budgethashtag.contentprovider.TransactionProvider;

public class BudgetHashtagDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "BudgetHashtagDbHelper";
    private static final String DB_NAME = "budgetHashtag.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_BUDGET  = "create table " + BudgetProvider.TABLE_NAME + " ("
            + BudgetProvider.Budget.KEY_COL_ID + " integer primary key autoincrement, "
            + BudgetProvider.Budget.KEY_COL_LIB + " TEXT(55) not null, "
            + BudgetProvider.Budget.KEY_COL_COLOR + " TEXT(8), "
            + BudgetProvider.Budget.KEY_COL_PREVISIONNEL + " REAL, "
            + BudgetProvider.Budget.KEY_COL_ID_PORTEFEUILLE + " integer not null)"
            ;
    private static final String CREATE_TABLE_PORTEFEUILLE  = "create table " + PortefeuilleProvider.TABLE_NAME + " ("
            + PortefeuilleProvider.Portefeuille.KEY_COL_ID + " integer primary key autoincrement, "
            + PortefeuilleProvider.Portefeuille.KEY_COL_LIB + " TEXT(55) not null)"
            ;
    private static final String CREATE_TABLE_TRANSACTION  = "create table " + TransactionProvider.TABLE_NAME + " ("
            + TransactionProvider.Transaction.KEY_COL_ID + " integer primary key autoincrement, "
            + TransactionProvider.Transaction.KEY_COL_LIB + " TEXT(55) not null, "
            + TransactionProvider.Transaction.KEY_COL_DT_VALEUR + " DATE not null, "
            + TransactionProvider.Transaction.KEY_COL_MONTANT + " REAL not null, "
            + TransactionProvider.Transaction.KEY_COL_ID_PORTEFEUILLE + " integer not null, "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_TIME + " integer, "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_PROVIDER + " TEXT(55), "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_ACCURACY + " REAL, "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_ALTITUDE + " REAL, "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_LATITUDE + " REAL, "
            + TransactionProvider.Transaction.KEY_COL_LOCATION_LONGITUDE + " REAL) "
            ;
    private static final String DROP_TABLE_BUDGET  = "DROP TABLE IF EXISTS "  + BudgetProvider.TABLE_NAME;
    private static final String DROP_TABLE_PORTEFEUILLE  = "DROP TABLE IF EXISTS "  + PortefeuilleProvider.TABLE_NAME;
    private static final String DROP_TABLE_TRANSACTION  = "DROP TABLE IF EXISTS "  + TransactionProvider.TABLE_NAME;
    private final Context context;

    public BudgetHashtagDbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUDGET);
        db.execSQL(CREATE_TABLE_PORTEFEUILLE);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, context.getResources().
                getString(R.string.warning_log_maj_bd, oldVersion, newVersion));
        db.execSQL(DROP_TABLE_BUDGET);
        db.execSQL(DROP_TABLE_PORTEFEUILLE);
        db.execSQL(DROP_TABLE_TRANSACTION);
        onCreate(db);
    }
}
