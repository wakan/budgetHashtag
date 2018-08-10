package fr.budgethashtag.transverse.event.budget;

import android.content.ContentValues;

import java.util.List;

public class LoadBudgetByIdPortefeuilleResponseEvent {
    List<ContentValues> lstContentValues;

    public LoadBudgetByIdPortefeuilleResponseEvent(List<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }

    public List<ContentValues> getContentValues() {
        return lstContentValues;
    }

    public void setContentValues(List<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }
}
