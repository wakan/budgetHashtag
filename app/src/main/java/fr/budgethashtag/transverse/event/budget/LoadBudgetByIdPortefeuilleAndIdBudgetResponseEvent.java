package fr.budgethashtag.transverse.event.budget;

import android.content.ContentValues;

public class LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent {
    private ContentValues contentValues;

    public LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues(ContentValues contentValues) {
        this.contentValues = contentValues;
    }
}
