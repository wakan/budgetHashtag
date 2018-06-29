package fr.budgethashtag.contentprovider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.BudgetTransaction;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.db.BudgetHashtagDbHelper;
import fr.budgethashtag.helpers.UriHelper;

import java.util.Objects;

public class BudgetHashtagProvider extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "budgetStore";
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
        uriMatcher.addURI(AUTHORITY , Budget.PATH_TO_DATA, BUDGET);
        uriMatcher.addURI(AUTHORITY, Budget.PATH_TO_DATA + "/#", BUDGET_ID);
        uriMatcher.addURI(AUTHORITY , Portefeuille.PATH_TO_DATA, PORTEFEUILLE);
        uriMatcher.addURI(AUTHORITY, Portefeuille.PATH_TO_DATA + "/#", PORTEFEUILLE_ID);
        uriMatcher.addURI(AUTHORITY , Transaction.PATH_TO_DATA, TRANSACTION);
        uriMatcher.addURI(AUTHORITY, Transaction.PATH_TO_DATA + "/#", TRANSACTION_ID);
        uriMatcher.addURI(AUTHORITY , BudgetTransaction.PATH_TO_DATA, BUDGET_TRANSACTION);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] calculatedProjection = null;
        switch (uriMatcher.match(uri)) {
            case PORTEFEUILLE_ID:
                qb.setTables(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            case BUDGET_ID:
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0)
                        + "AND" + Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case TRANSACTION_ID:
                qb.setTables(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0)
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
                if(null == projection) {
                    calculatedProjection = new String[]{
                            Budget.KEY_COL_ID,
                            Budget.KEY_COL_LIB,
                            Budget.KEY_COL_COLOR,
                            Budget.KEY_COL_PREVISIONNEL,
                            Budget.KEY_COL_ID_PORTEFEUILLE,
                            " ( select count(" + Transaction.KEY_COL_MONTANT + ") " +
                                    " from " + BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME +
                                    " inner join " + BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME + " tran " +
                                    "     on tran." + Transaction.KEY_COL_ID + " = id_transaction " +
                                    " where id_budget = bud." + Budget.KEY_COL_ID + " ) as " + Budget.KEY_COL_EXP_SUM_MNT,
                            " ( select count(" + Transaction.KEY_COL_MONTANT + ") " +
                                    " from " + BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME +
                                    " inner join " + BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME + " tran " +
                                    "     on tran." + Transaction.KEY_COL_ID + " = id_transaction " +
                                    " where id_budget = bud." + Budget.KEY_COL_ID + ") as " + Budget.KEY_COL_EXP_COUNT_MNT
                    };
                }
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
                break;
            default:
                break;
        }
        String orderBy = setOrderbyClose(uri, sortOrder);
        if(null == calculatedProjection) {
            calculatedProjection = projection;
        }
        Cursor c = qb.query(budgetHashtagDb, calculatedProjection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return c;
    }
    private String setOrderbyClose(Uri uri, String sortOrder) {
        String orderBy = null;
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
        long idAdded = 0L;
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
                    Objects.requireNonNull(getContext()).getString(R.string.ex_msg_insert) + uri);
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
