package fr.budgethashtag.interfacecallbackasynctask;

import android.content.ContentValues;

import java.util.List;

public interface LoadBudgetsByPortefeuilleIdCallback {
    void onLoadBudgetsByPortefeuilleIdCallback(List<ContentValues> contentValuesList);
}
