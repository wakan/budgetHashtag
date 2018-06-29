package fr.budgethashtag.helpers;

import android.content.ContentUris;
import android.net.Uri;

public class UriHelper {
    public  static Uri getUriForId(Uri uri, long id)  {
        return ContentUris.withAppendedId(uri, id);
    }
}
