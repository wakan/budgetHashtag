package fr.budgethashtag.helpers;

import android.net.Uri;

import java.util.Objects;

public class UriHelper {
    public static Integer getIdFromContentUri(Uri uri) {
        return  Integer.parseInt(Objects.requireNonNull(uri).getPathSegments().get(0));
    }
    public  static Uri getUriForId(Uri uri, long id)  {
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        return itemUri;
    }
}
