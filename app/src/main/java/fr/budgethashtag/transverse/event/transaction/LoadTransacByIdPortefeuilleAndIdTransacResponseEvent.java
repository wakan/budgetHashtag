package fr.budgethashtag.transverse.event.transaction;

import android.content.ContentValues;

public class LoadTransacByIdPortefeuilleAndIdTransacResponseEvent {
    private ContentValues contentValues;

    public LoadTransacByIdPortefeuilleAndIdTransacResponseEvent(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues(ContentValues contentValues) {
        this.contentValues = contentValues;
    }
}
