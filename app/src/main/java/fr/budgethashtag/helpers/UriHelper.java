package fr.budgethashtag.helpers;

import android.content.ContentUris;
import android.net.Uri;

import java.util.Objects;

public class UriHelper {
    public  static Uri getUriForId(Uri uri, long id)  {
        return ContentUris.withAppendedId(uri, id);
    }
}
