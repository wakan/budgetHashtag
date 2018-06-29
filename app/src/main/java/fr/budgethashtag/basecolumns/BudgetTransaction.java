package fr.budgethashtag.basecolumns;

import android.net.Uri;
import android.provider.BaseColumns;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;

public class BudgetTransaction implements BaseColumns {
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.budgetstransactions";
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.budgetstransactions";
    public static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/budgetstransactions";
    private static final Uri CONTENT_URI = Uri.parse("content://" +
            BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
    public static Uri contentUriCollection(long idPortefeuille) {
        Uri.Builder builder = new Uri.Builder();
        builder.path(Portefeuille.contentUriItem(idPortefeuille).toString());
        builder.appendPath("budgetstransactions");
        return builder.build();
    }

    public static final String KEY_COL_ID_TRANSACTION = "id_transaction";
    public static final String KEY_COL_ID_BUDGET = "id_budget";
}
