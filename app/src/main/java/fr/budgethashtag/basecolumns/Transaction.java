package fr.budgethashtag.basecolumns;

import android.net.Uri;
import android.provider.BaseColumns;
import fr.budgethashtag.contentprovider.BudgetHashtagProvider;
import fr.budgethashtag.helpers.UriHelper;

public class Transaction implements BaseColumns {
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vdn.budgethashtag.transaction";
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_ITEM  = "vnd.android.cursor.item/vdn.budgethashtag.transaction";
    public static final String PATH_TO_DATA = Portefeuille.PATH_TO_DATA + "/#/transaction";
    private static final Uri CONTENT_URI = Uri.parse("content://" +
            BudgetHashtagProvider.AUTHORITY + "/" + PATH_TO_DATA);
    public static Uri contentUriCollection(long idPortefeuille) {
        Uri.Builder builder = new Uri.Builder();
        builder.path(Portefeuille.contentUriItem(idPortefeuille).toString());
        builder.appendPath("transaction");
        return builder.build();
    }
    public static Uri contentUriItem(long idPortefeuille, long id) {
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
