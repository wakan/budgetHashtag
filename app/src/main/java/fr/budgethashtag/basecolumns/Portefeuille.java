package fr.budgethashtag.basecolumns;

import android.net.Uri;
import android.provider.BaseColumns;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;
import fr.budgethashtag.helpers.UriHelper;

public class Portefeuille implements BaseColumns {
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.portefeuille";
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.portefeuille";
    public static final String PATH_TO_DATA = "portefeuille";
    private static final Uri CONTENT_URI = Uri.parse("content://" +
            BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
    public static Uri contentUriCollection() {
        return CONTENT_URI;
    }
    public static Uri contentUriItem(long id) {
        return UriHelper.getUriForId(CONTENT_URI, id);
    }

    @SuppressWarnings("WeakerAccess")
    public static final String KEY_COL_ID = "_id";
    public static final String KEY_COL_LIB = "libelle";
}
