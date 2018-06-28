package fr.budgethashtag.contentprovider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import fr.budgethashtag.R;
import fr.budgethashtag.db.BudgetHashtagDbHelper;

import java.util.Objects;

import static fr.budgethashtag.contentprovider.BudgetProvider.Budget.KEY_COL_ID;

public class BudgetHashtagAsyncTask extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "budgetStore";
    @SuppressWarnings("WeakerAccess")
    public static final String PATH_TO_DATA_BUDGET = "budget";
    public static final String PATH_TO_DATA_PORTEFEUILLE = "portefeuille";
    public static final String PATH_TO_DATA_TRANSACTION = "transaction";
    public static final String PATH_TO_DATA_BUDGET_TRANSACTION = "budgetstransactions";
    public class Budget implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budget";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budget";

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
        public static final String KEY_COL_COLOR = "color";
        public static final String KEY_COL_PREVISIONNEL = "previsionnel";
        public static final String KEY_COL_ID_PORTEFEUILLE = "idPortefeuille";
        public static final String KEY_COL_EXP_SUM_MNT = "sum_mnt";
        public static final String KEY_COL_EXP_COUNT_MNT = "count_mnt";
    }
    public class Portefeuille implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.portefeuille";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.portefeuille";

        @SuppressWarnings("WeakerAccess")
        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
    }
    public class Transaction implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.transaction";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.transaction";

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
        public static final String KEY_COL_DT_VALEUR = "dtValeur";
        public static final String KEY_COL_MONTANT = "montant";
        public static final String KEY_COL_ID_PORTEFEUILLE = "idPortefeuille";
        public static final String KEY_COL_LOCATION_TIME = "locationTime";
        public static final String KEY_COL_LOCATION_PROVIDER = "locationProvider";
        public static final String KEY_COL_LOCATION_ACCURACY = "locationAccuracy";
        public static final String KEY_COL_LOCATION_ALTITUDE = "locationAltitude";
        public static final String KEY_COL_LOCATION_LATITUDE = "locationLatitude";
        public static final String KEY_COL_LOCATION_LONGITUDE = "locationLongitude";
    }
    public class BudgetTransaction implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budgetstransactions";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budgetstransactions";

        public static final String KEY_COL_ID_TRANSACTION = "id_transaction";
        public static final String KEY_COL_ID_BUDGET = "id_budget";
    }

    private SQLiteDatabase budgetHashtagDb;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        BudgetHashtagDbHelper dbHelper = new BudgetHashtagDbHelper(context, null);
        budgetHashtagDb = dbHelper.getWritableDatabase();
        return budgetHashtagDb != null;
    }
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                return  Budget.MIME_COLLECTION;
            case BUDGET_ID:
                return Budget.MIME_ITEM;
            case PORTEFEUILLE:
                return  Portefeuille.MIME_COLLECTION;
            case PORTEFEUILLE_ID:
                return Portefeuille.MIME_ITEM;
            case TRANSACTION:
                return  Transaction.MIME_COLLECTION;
            case TRANSACTION_ID:
                return Transaction.MIME_ITEM;
            case BUDGET_TRANSACTION:
                return  BudgetTransaction.MIME_COLLECTION;
            case BUDGET_TRANSACTION_ID:
                return BudgetTransaction.MIME_ITEM;
            default:
                throw new IllegalArgumentException("URI not supported : " + uri);
        }
    }
    private static final UriMatcher uriMatcher;
    private static final int BUDGET = 1;
    private static final int BUDGET_ID = 2;
    private static final int PORTEFEUILLE = 3;
    private static final int PORTEFEUILLE_ID = 4;
    private static final int TRANSACTION = 5;
    private static final int TRANSACTION_ID = 6;
    private static final int BUDGET_TRANSACTION = 7;
    private static final int BUDGET_TRANSACTION_ID = 8;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_BUDGET, BUDGET);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_BUDGET + "/#", BUDGET_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_PORTEFEUILLE, PORTEFEUILLE);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_PORTEFEUILLE + "/#", PORTEFEUILLE_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_TRANSACTION, TRANSACTION);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_TRANSACTION + "/#", TRANSACTION_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_BUDGET_TRANSACTION, BUDGET_TRANSACTION);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_BUDGET_TRANSACTION + "/#", BUDGET_TRANSACTION_ID);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case PORTEFEUILLE_ID:
                qb.appendWhere( KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            default:
                break;
        }
        Cursor c = null;
        return c;
    }
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues) {
        return Uri.parse("http://google.fr/");
    }
    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        throw new UnsupportedOperationException();
    }

}
