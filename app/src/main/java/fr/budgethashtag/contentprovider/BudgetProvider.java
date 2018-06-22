package fr.budgethashtag.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import fr.budgethashtag.db.BudgetHashtagDbHelper;
import fr.budgethashtag.R;

import java.util.Objects;

public class BudgetProvider extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "BudgetStore.Budget";
    @SuppressWarnings("WeakerAccess")
    public static final String PATH_TO_DATA = "";
    public static final Uri CONTENT_URI = Uri.parse("content://" +
            BudgetProvider.AUTHORITY + "/" + BudgetProvider.PATH_TO_DATA);

    public static final String TABLE_NAME = "budgets";

    public class Budget implements BaseColumns {
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.contentType";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.contentType";

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
        public static final String KEY_COL_COLOR = "color";
        public static final String KEY_COL_DT_DEB = "dtDeb";
        public static final String KEY_COL_DT_FIN = "dtFin";
        public static final String KEY_COL_PREVISIONNEL = "previsionnel";
        public static final String KEY_COL_ID_PORTEFEUILLE = "idPortefeuille";
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
        case COLLECTION:
            return  Budget.MIME_COLLECTION;
        case ITEM:
            return Budget.MIME_ITEM;
        default:
            throw new IllegalArgumentException("URI not supported : " + uri);
        }
    }

    private static final int ITEM = 1;
    private static final int COLLECTION = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY , PATH_TO_DATA, COLLECTION);
        uriMatcher.addURI(AUTHORITY, PATH_TO_DATA + "/#", ITEM);
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ITEM:
                qb.appendWhere( Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Budget.KEY_COL_LIB;
        } else {
            orderBy = sortOrder;
        }
        Cursor c = qb.query(budgetHashtagDb, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return c;
    }
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues) {
        long rowId = budgetHashtagDb.insert(TABLE_NAME, "test", initialValues);
        if(rowId > 0) {
            Uri uriAdd = ContentUris.withAppendedId(CONTENT_URI, rowId);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uriAdd, null);
            return uriAdd;
        }
        throw new SQLException(Objects.requireNonNull(getContext()).getResources().
                    getString(R.string.ex_msg_add, uri));
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
