package fr.budgethashtag.transverse.exception;

import android.util.Log;
import fr.budgethashtag.transverse.event.ErrorEvent;
import fr.budgethashtag.transverse.event.ExceptionManagedEvent;
import org.greenrobot.eventbus.EventBus;

public class ExceptionManager {
    public synchronized static void manage(BudgetHashtagException exception) {
        management(exception);
    }

    public synchronized static void displayAnError(String errorMessage) {
        EventBus.getDefault().post(new ErrorEvent(errorMessage));
        //You should make a feedback to the team
        Log.e("ExceptionManaged", "Error managed :" + errorMessage);
    }

    private static void management(BudgetHashtagException exc) {
        if (!exc.isManaged()) {
            exc.setManaged(true);
            EventBus.getDefault().post(new ExceptionManagedEvent(exc));
            // Should prevent the backend server
            //You should do it, This tutorial has no backend$

            //log
            Log.e("ExceptionManaged", exc.getErrorMessage(), exc);
            Log.e(exc.getRootClass().getSimpleName(), exc.getErrorMessage(), exc);
        }
    }

}
