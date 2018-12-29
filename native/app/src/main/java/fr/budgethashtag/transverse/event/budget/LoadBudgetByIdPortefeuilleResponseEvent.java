package fr.budgethashtag.transverse.event.budget;

import android.content.ContentValues;

import java.util.Collection;

public class LoadBudgetByIdPortefeuilleResponseEvent {
    Collection<ContentValues> lstContentValues;

    public LoadBudgetByIdPortefeuilleResponseEvent(Collection<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }

    public Collection<ContentValues> getContentValues() {
        return lstContentValues;
    }

    public void setContentValues(Collection<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }
}
