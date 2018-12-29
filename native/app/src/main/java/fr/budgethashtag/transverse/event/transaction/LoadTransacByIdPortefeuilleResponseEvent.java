package fr.budgethashtag.transverse.event.transaction;

import android.content.ContentValues;

import java.util.Collection;

public class LoadTransacByIdPortefeuilleResponseEvent {
    Collection<ContentValues> lstContentValues;

    public LoadTransacByIdPortefeuilleResponseEvent(Collection<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }

    public Collection<ContentValues> getLstContentValues() {
        return lstContentValues;
    }

    public void setLstContentValues(Collection<ContentValues> lstContentValues) {
        this.lstContentValues = lstContentValues;
    }
}
