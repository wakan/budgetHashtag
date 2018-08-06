package fr.budgethashtag.transverse.event.portefeuille;

import android.content.ContentValues;

public class GetPortefeuilleByIdResponseEvent {
    private ContentValues contentValues;

    public GetPortefeuilleByIdResponseEvent(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues(ContentValues contentValues) {
        this.contentValues = contentValues;
    }
}
