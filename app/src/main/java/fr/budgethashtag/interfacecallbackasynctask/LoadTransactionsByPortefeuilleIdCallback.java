package fr.budgethashtag.interfacecallbackasynctask;

import android.content.ContentValues;
import java.util.List;

public interface LoadTransactionsByPortefeuilleIdCallback {
    void onLoadTransactionsByPortefeuilleId(List<ContentValues> contentValuesList);
}
