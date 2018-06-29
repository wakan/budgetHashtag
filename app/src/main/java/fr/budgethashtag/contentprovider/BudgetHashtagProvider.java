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

public class BudgetHashtagProvider extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "budgetStore";
    public class Budget implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budget";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budget";
        private static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/budget";
        private static final Uri CONTENT_URI = Uri.parse("content://" +
                                    BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
        public static final Uri contentUriCollection(long idPortefeuille) {
            return ContentUri.withAppendedPath(Portefeuille.contentUriItem(idPortefeuille), "budget");
        }
        public static final Uri contentUriItem(long idPortefeuille, long id) {
            return UriHelper.getUriForId(contentUriCollection(idPortefeuille), id);
        }

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
        private static final String PATH_TO_DATA = "portefeuille";
        private static final Uri CONTENT_URI = Uri.parse("content://" +
                BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
        public static final Uri contentUriCollection() {
            return CONTENT_URI;
        }
        public static final Uri contentUriItem(long id) {
            return UriHelper.getUriForId(CONTENT_URI, id);
        }

        @SuppressWarnings("WeakerAccess")
        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
    }
    public class Transaction implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.transaction";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.transaction";
        private static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/transaction";
        private static final Uri CONTENT_URI = Uri.parse("content://" +
                BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
        public static final Uri contentUriCollection(long idPortefeuille) {
            return ContentUri.withAppendedPath(Portefeuille.contentUriItem(idPortefeuille), "transaction");
        }
        public static final Uri contentUriItem(long idPortefeuille, long id) {
            return UriHelper.getUriForId(contentUriCollection(idPortefeuille), id);
        }

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
        private static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/budgetstransactions";
        private static final Uri CONTENT_URI = Uri.parse("content://" +
                BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
        public static final Uri contentUriCollection(long idPortefeuille) {
            return ContentUri.withAppendedPath(Portefeuille.contentUriItem(idPortefeuille), "budgetstransactions");
        }

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
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_BUDGET, BUDGET);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_BUDGET + "/#", BUDGET_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_PORTEFEUILLE, PORTEFEUILLE);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_PORTEFEUILLE + "/#", PORTEFEUILLE_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_TRANSACTION, TRANSACTION);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA_TRANSACTION + "/#", TRANSACTION_ID);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA_BUDGET_TRANSACTION, BUDGET_TRANSACTION);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case PORTEFEUILLE_ID:
                qb.setTables(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            case BUDGET_ID:
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments(0)
                        + "AND" + Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case TRANSACTION_ID:
                qb.setTables(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments(0)
                        + "AND" + Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case PORTEFEUILLE:
                qb.setTables(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME);
                break;
            case TRANSACTION:
                qb.setTables(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            case BUDGET:
                SQLiteQueryBuilder.appendColumns(qb, {
                        Budget.KEY_COL_ID,
                        Budget.KEY_COL_LIB,
                        Budget.KEY_COL_COLOR,
                        Budget.KEY_COL_PREVISIONNEL,
                        Budget.KEY_COL_ID_PORTEFEUILLE,
                        " ( select count("+ TransactionProvider.Transaction.KEY_COL_MONTANT + ") " +
                                " from " + BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME +
                                " inner join " + BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME + " tran " +
                                "     on tran." + TransactionProvider.Transaction.KEY_COL_ID + " = id_transaction " +
                                " where id_budget = bud." + Budget.KEY_COL_ID + " ) as " + Budget.KEY_COL_EXP_SUM_MNT,
                        " ( select count("+ TransactionProvider.Transaction.KEY_COL_MONTANT + ") " +
                                " from " + BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME +
                                " inner join " + BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME + " tran " +
                                "     on tran." + TransactionProvider.Transaction.KEY_COL_ID + " = id_transaction " +
                                " where id_budget = bud." + Budget.KEY_COL_ID + ") as " + Budget.KEY_COL_EXP_COUNT_MNT
                });
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            default:
                break;
        }
        String orderBy = setOrderbyClose(sortOrder);
        Cursor c = qb.query(budgetHashtagDb, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return c;
    }
    private final String setOrderbyClose(String sortOrder) {
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            switch (uriMatcher.match(uri)) {
                case PORTEFEUILLE:
                    orderBy = Portefeuille.KEY_COL_LIB;
                    break;
                case BUDGET:
                    orderBy = Budget.KEY_COL_LIB;
                    break;
                case TRANSACTION:
                    orderBy = Transaction.KEY_COL_LIB;
                    break;
            }
        } else {
            orderBy = sortOrder;
        }
        return orderBy;
    }
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues) {
        Uri returnUri;
        long idAdded;
        switch (uriMatcher.match(uri)) {
            case PORTEFEUILLE:
                idAdded = budgetHashtagDb.insert(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME, null, initialValues);
                break;
            case BUDGET:
                idAdded = budgetHashtagDb.insert(BudgetHashtagDbHelper.BUDGET_TABLE_NAME, null, initialValues);
                break;
            case TRANSACTION:
                idAdded = budgetHashtagDb.insert(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME, null, initialValues);
                break;
            case BUDGET_TRANSACTION:
                idAdded = budgetHashtagDb.insert(BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME, null, initialValues);
                break;
        }
        if(idAdded <= 0) {
            throw new SQLException(
                    getContext().getString(R.string.ex_msg_insert) + uri);
        }
        returnUri = UriHelper.getUriForId(uri, idAdded);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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
