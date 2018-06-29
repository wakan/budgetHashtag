package fr.budgethashtag.basecolumns;

import android.net.Uri;
import android.provider.BaseColumns;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;
import fr.budgethashtag.helpers.UriHelper;

public class Budget implements BaseColumns {
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budget";
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budget";
    public static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/budget";
    private static final Uri CONTENT_URI = Uri.parse("content://" +
            BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
    public static Uri contentUriCollection(long idPortefeuille) {
        Uri.Builder builder = new Uri.Builder();
        builder.path(Portefeuille.contentUriItem(idPortefeuille).toString());
        builder.appendPath("budget");
        return builder.build();
    }
    public static Uri contentUriItem(long idPortefeuille, long id) {
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
