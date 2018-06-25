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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import fr.budgethashtag.db.BudgetHashtagDbHelper;

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
        public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budget";
        @SuppressWarnings("WeakerAccess")
        public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budget";

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_LIB = "libelle";
        public static final String KEY_COL_COLOR = "color";
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
                qb.appendWhere( Budget.KEY_COL_ID + "=" + uri.getPathSegments().get(0));
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
        reformatLibelle(initialValues);
        setDefaultColorIfNotExit(initialValues);
        long rowId = budgetHashtagDb.insert(TABLE_NAME, "test", initialValues);
        if(rowId > 0) {
            Uri uriAdd = ContentUris.withAppendedId(CONTENT_URI, rowId);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uriAdd, null);
            return uriAdd;
        }
        throw new SQLException(Objects.requireNonNull(getContext()).getResources().
                    getString(R.string.ex_msg_add, uri));
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
            String defaultColor = getRandomDefaultColor();
            initialValues.put(Budget.KEY_COL_COLOR, defaultColor);
        }
    }
    private String getRandomDefaultColor() {
        Random random = new Random();
        return colors().get(random.nextInt(colors().size()));
    }
    @SuppressWarnings("SpellCheckingInspection")
    private List<String> colors() {
        ArrayList<String> colors = new ArrayList<>(256);
        colors.add("##F0F8FF");
        colors.add("##FAEBD7");
        colors.add("##00FFFF");
        colors.add("##7FFFD4");
        colors.add("##F0FFFF");
        colors.add("##F5F5DC");
        colors.add("##FFE4C4");
        colors.add("##000000");
        colors.add("##FFEBCD");
        colors.add("##0000FF");
        colors.add("##8A2BE2");
        colors.add("##A52A2A");
        colors.add("##DEB887");
        colors.add("##5F9EA0");
        colors.add("##7FFF00");
        colors.add("##D2691E");
        colors.add("##FF7F50");
        colors.add("##6495ED");
        colors.add("##FFF8DC");
        colors.add("##DC143C");
        colors.add("##00FFFF");
        colors.add("##00008B");
        colors.add("##008B8B");
        colors.add("##B8860B");
        colors.add("##A9A9A9");
        colors.add("##A9A9A9");
        colors.add("##006400");
        colors.add("##BDB76B");
        colors.add("##8B008B");
        colors.add("##556B2F");
        colors.add("##FF8C00");
        colors.add("##9932CC");
        colors.add("##8B0000");
        colors.add("##E9967A");
        colors.add("##8FBC8F");
        colors.add("##483D8B");
        colors.add("##2F4F4F");
        colors.add("##2F4F4F");
        colors.add("##00CED1");
        colors.add("##9400D3");
        colors.add("##FF1493");
        colors.add("##00BFFF");
        colors.add("##696969");
        colors.add("##696969");
        colors.add("##1E90FF");
        colors.add("##B22222");
        colors.add("##FFFAF0");
        colors.add("##228B22");
        colors.add("##FF00FF");
        colors.add("##DCDCDC");
        colors.add("##F8F8FF");
        colors.add("##FFD700");
        colors.add("##DAA520");
        colors.add("##808080");
        colors.add("##808080");
        colors.add("##008000");
        colors.add("##ADFF2F");
        colors.add("##F0FFF0");
        colors.add("##FF69B4");
        colors.add("##CD5C5C");
        colors.add("##4B0082");
        colors.add("##FFFFF0");
        colors.add("##F0E68C");
        colors.add("##E6E6FA");
        colors.add("##FFF0F5");
        colors.add("##7CFC00");
        colors.add("##FFFACD");
        colors.add("##ADD8E6");
        colors.add("##F08080");
        colors.add("##E0FFFF");
        colors.add("##FAFAD2");
        colors.add("##D3D3D3");
        colors.add("##D3D3D3");
        colors.add("##90EE90");
        colors.add("##FFB6C1");
        colors.add("##FFA07A");
        colors.add("##20B2AA");
        colors.add("##87CEFA");
        colors.add("##778899");
        colors.add("##778899");
        colors.add("##B0C4DE");
        colors.add("##FFFFE0");
        colors.add("##00FF00");
        colors.add("##32CD32");
        colors.add("##FAF0E6");
        colors.add("##FF00FF");
        colors.add("##800000");
        colors.add("##66CDAA");
        colors.add("##0000CD");
        colors.add("##BA55D3");
        colors.add("##9370DB");
        colors.add("##3CB371");
        colors.add("##7B68EE");
        colors.add("##00FA9A");
        colors.add("##48D1CC");
        colors.add("##C71585");
        colors.add("##191970");
        colors.add("##F5FFFA");
        colors.add("##FFE4E1");
        colors.add("##FFE4B5");
        colors.add("##FFDEAD");
        colors.add("##000080");
        colors.add("##FDF5E6");
        colors.add("##808000");
        colors.add("##6B8E23");
        colors.add("##FFA500");
        colors.add("##FF4500");
        colors.add("##DA70D6");
        colors.add("##EEE8AA");
        colors.add("##98FB98");
        colors.add("##AFEEEE");
        colors.add("##DB7093");
        colors.add("##FFEFD5");
        colors.add("##FFDAB9");
        colors.add("##CD853F");
        colors.add("##FFC0CB");
        colors.add("##DDA0DD");
        colors.add("##B0E0E6");
        colors.add("##800080");
        colors.add("##663399");
        colors.add("##FF0000");
        colors.add("##BC8F8F");
        colors.add("##4169E1");
        colors.add("##8B4513");
        colors.add("##FA8072");
        colors.add("##F4A460");
        colors.add("##2E8B57");
        colors.add("##FFF5EE");
        colors.add("##A0522D");
        colors.add("##C0C0C0");
        colors.add("##87CEEB");
        colors.add("##6A5ACD");
        colors.add("##708090");
        colors.add("##708090");
        colors.add("##FFFAFA");
        colors.add("##00FF7F");
        colors.add("##4682B4");
        colors.add("##D2B48C");
        colors.add("##008080");
        colors.add("##D8BFD8");
        colors.add("##FF6347");
        colors.add("##40E0D0");
        colors.add("##EE82EE");
        colors.add("##F5DEB3");
        colors.add("##FFFFFF");
        colors.add("##F5F5F5");
        colors.add("##FFFF00");
        colors.add("##9ACD32");
        return colors;
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
