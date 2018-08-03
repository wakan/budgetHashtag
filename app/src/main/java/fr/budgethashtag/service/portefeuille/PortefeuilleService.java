package fr.budgethashtag.service.portefeuille;

import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;

public interface PortefeuilleService {
    Long getOrCreateDefaultPortefeuilleIfNotExist(final Context context)
            throws OperationApplicationException;
    Long createDefaultPortefeuille(final Context context)
            throws OperationApplicationException;

    long getIdPortefeuilleFromSharedPref(final Context context);

    ContentValues getPortefeuilleById(final Context context);

}
