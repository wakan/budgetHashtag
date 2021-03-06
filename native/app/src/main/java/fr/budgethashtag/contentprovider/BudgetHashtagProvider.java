package fr.budgethashtag.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
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
import fr.budgethashtag.helpers.ColorHelper;
import fr.budgethashtag.helpers.UriHelper;

import java.util.Objects;

public class BudgetHashtagProvider extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "budget-store";
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
                qb.appendWhere( Portefeuille.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case BUDGET_ID:
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME);
                qb.appendWhere( Budget.KEY_COL_ID_PORTEFEUILLE + "=" + uri.getPathSegments().get(1)
                        + " AND " + Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(3));
                break;
            case TRANSACTION_ID:
                qb.setTables(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME);
                qb.appendWhere( Transaction.KEY_COL_ID_PORTEFEUILLE + "=" + uri.getPathSegments().get(1)
                        + " AND " + Transaction.KEY_COL_ID + "=" + uri.getPathSegments().get(3));
                break;
            case PORTEFEUILLE:
                qb.setTables(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME);
                break;
            case TRANSACTION:
                qb.setTables(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME);
                qb.appendWhere( Transaction.KEY_COL_ID_PORTEFEUILLE + "=" + uri.getPathSegments().get(1));
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
                qb.setTables(BudgetHashtagDbHelper.BUDGET_TABLE_NAME + " bud");
                qb.appendWhere( Budget.KEY_COL_ID_PORTEFEUILLE + "=" + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }
        String orderBy = setOrderByClose(uri, sortOrder);
        if(null == calculatedProjection) {
            calculatedProjection = projection;
        }
        Cursor c = qb.query(budgetHashtagDb, calculatedProjection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return c;
    }
    private String setOrderByClose(Uri uri, String sortOrder) {
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
                reformatLibelle(initialValues);
                setDefaultColorIfNotExit(initialValues);
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
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return returnUri;
    }
    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        int nbDeleteRow = 0;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.BUDGET_TABLE_NAME,
                        where, whereArgs);
                break;
            case BUDGET_ID:
                String whereContructBud =  Budget.KEY_COL_ID_PORTEFEUILLE + " = ? AND " + Budget.KEY_COL_ID + " = ?";
                String[] whereArgsConstructBud = {uri.getPathSegments().get(1), uri.getPathSegments().get(3)};
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.BUDGET_TABLE_NAME,
                        whereContructBud, whereArgsConstructBud);
                break;
            case TRANSACTION:
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME,
                        where, whereArgs);
                break;
            case TRANSACTION_ID:
                String whereContructTran =  Transaction.KEY_COL_ID_PORTEFEUILLE + " = ? AND " + Transaction.KEY_COL_ID + " = ?";
                String[] whereArgsConstructTran = {uri.getPathSegments().get(1), uri.getPathSegments().get(3)};
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.BUDGET_TABLE_NAME,
                        whereContructTran, whereArgsConstructTran);
                break;
            case BUDGET_TRANSACTION:
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME,
                        where, whereArgs);
                break;
            case PORTEFEUILLE:
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.BUDGET_TRANSACTION_TABLE_NAME,
                        where, whereArgs);
                break;
            case PORTEFEUILLE_ID:
                String whereContructPort =  Portefeuille.KEY_COL_ID + " = ?";
                String[] whereArgsConstructPort = {uri.getPathSegments().get(1)};
                nbDeleteRow = budgetHashtagDb.delete(BudgetHashtagDbHelper.PORTEFEUILLE_TABLE_NAME,
                        whereContructPort, whereArgsConstructPort);
                break;
        }
        return nbDeleteRow;
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        int nbUpdateRow = 0;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                nbUpdateRow = budgetHashtagDb.update(BudgetHashtagDbHelper.BUDGET_TABLE_NAME, values,
                        where, whereArgs);
                break;
            case BUDGET_ID:
                String whereContructBud =  Budget.KEY_COL_ID_PORTEFEUILLE + " = ? AND " + Budget.KEY_COL_ID + " = ?";
                String[] whereArgsConstructBud = {uri.getPathSegments().get(1), uri.getPathSegments().get(3)};
                nbUpdateRow = budgetHashtagDb.update(BudgetHashtagDbHelper.BUDGET_TABLE_NAME, values,
                        whereContructBud, whereArgsConstructBud);
                break;
            case TRANSACTION:
                nbUpdateRow = budgetHashtagDb.update(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME, values,
                        where, whereArgs);
                break;
            case TRANSACTION_ID:
                String whereContructTran =  Transaction.KEY_COL_ID_PORTEFEUILLE + " = ? AND " + Transaction.KEY_COL_ID + " = ?";
                String[] whereArgsConstructTran = {uri.getPathSegments().get(1), uri.getPathSegments().get(3)};
                nbUpdateRow = budgetHashtagDb.update(BudgetHashtagDbHelper.TRANSACTION_TABLE_NAME, values,
                        whereContructTran, whereArgsConstructTran);
                break;
        }
        return nbUpdateRow;
    }


    private void reformatLibelle(ContentValues initialValues) {
        String initialLibLowercase = initialValues.getAsString(Budget.KEY_COL_LIB).toLowerCase();
        String replacedBadChar = initialLibLowercase.replaceAll("[^a-z0-9]","-");
        String removedDuplicates = removeDuplicates(replacedBadChar);
        initialValues.put(Budget.KEY_COL_LIB, removedDuplicates);
    }

    private String removeDuplicates(String word) {
        StringBuilder buffer = new StringBuilder(word.length());
        for (int i = 0; i < word.length();i++) {
            char letter = word.charAt(i);
            if(letter == '-') {
                if (letter != buffer.length() -1) {
                    buffer.append(letter);
                }
            } else {
                buffer.append(letter);
            }
        }
        return  buffer.toString();
    }
    private void setDefaultColorIfNotExit(ContentValues initialValues) {
        String initialColor = initialValues.getAsString(Budget.KEY_COL_COLOR);
        if(null == initialColor || Objects.equals("", initialColor.trim())) {
            String defaultColor = ColorHelper.getRandomDefaultColor();
            initialValues.put(Budget.KEY_COL_COLOR, defaultColor);
        }
    }


}
