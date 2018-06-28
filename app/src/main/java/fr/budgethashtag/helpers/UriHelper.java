package fr.budgethashtag.helpers;

import android.net.Uri;

import java.util.Objects;

public class UriHelper {
    public static Integer getIdFromContentUri(Uri uri) {
        return  Integer.parseInt(Objects.requireNonNull(uri).getPathSegments().get(0));
    }
}
