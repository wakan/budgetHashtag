package fr.budgethashtag.asynctask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.beanwork.WorkTransactions;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.BudgetTransaction;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.helpers.TransactionHelper;

public class SaveTransactionAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Context> contextRef;
    private final int id;
    private final String libelle;
    private final Date date;
    private final Double montant;
    private final WorkTransactions transactions;
    private final String locationProvider;
    private final Double longitude;
    private final Double latitude;
    private final Double altitude;
    private final Double accuracy;



    public SaveTransactionAsyncTask(Context context,
                                    int id,
                                    String libelle,
                                    Date date, Double montant, WorkTransactions transactions,
                                    String locationProvider, Double longitude, Double latitude, Double altitude, Double accuracy) {
        this.contextRef = new WeakReference<>(context);
        this.id = id;
        this.libelle = libelle;
        this.date = date;
        this.montant = montant;
        this.transactions = transactions;
        this.locationProvider = locationProvider;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
    }

    @Override
    protected Void doInBackground(Void... params) {
        /*
        ContentResolver cr = contextRef.get().getContentResolver();
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        long idTransaction = 0;
        try {
            idTransaction = insertTransaction(cr, idPortefeuille);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        List<Integer> idsInsert = insertNewBudget(cr, idPortefeuille, transactions.getTransactionsNouvelles());
        insertBudgetTransaction(cr, idPortefeuille, idTransaction, idsInsert, transactions.getTransactionsExistantesAjoutees());
        deleteBudgetTransaction(cr, idTransaction, idPortefeuille, transactions.getTransactionsExistantesSupprimees());
        */
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ((Activity) contextRef.get()).finish();
        Toast.makeText(contextRef.get(), R.string.AjoutOK, Toast.LENGTH_SHORT).show();
    }

    private List<Integer> insertNewBudget(ContentResolver cr, long idPortefeuille, List<String> transactionsNouvelles) {
        List<Integer> idBudgetsAjoutes = new ArrayList<>(transactionsNouvelles.size());
        for(String lib : transactionsNouvelles) {
            ContentValues cv = new ContentValues();
            cv.put(Budget.KEY_COL_LIB, lib);
            cv.put(Budget.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
            Uri uriAdd = cr.insert(Budget.contentUriCollection(idPortefeuille), cv);
            if (uriAdd == null)
                try {
                    throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_save_budget));
                } catch (OperationApplicationException e) {
                    //TODO : Do better because it is not good here for catch exception
                    e.printStackTrace();
                }
            idBudgetsAjoutes.add(Integer.parseInt(Objects.requireNonNull(uriAdd).getPathSegments().get(3)));
        }
        return idBudgetsAjoutes;
    }

    private long insertTransaction(ContentResolver cr, long idPortefeuille) throws OperationApplicationException {
        Uri uri = TransactionHelper.createTransaction(contextRef.get(), id, idPortefeuille, libelle, montant, date,
                locationProvider, accuracy, altitude, latitude, longitude);
        return Integer.parseInt(Objects.requireNonNull(uri).getPathSegments().get(3));
    }

    private void insertBudgetTransaction(ContentResolver cr, long idTransaction, long idPortefeuille,
                                         List<Integer> idsInsert, List<Integer> budgetExistantsAjoutes) {
        for(Integer id : idsInsert) {
            insertOneBudgetTransaction(cr, idPortefeuille, idTransaction);
        }
        for(Integer id : budgetExistantsAjoutes) {
            insertOneBudgetTransaction(cr, idPortefeuille, idTransaction);
        }
    }

    private void insertOneBudgetTransaction(ContentResolver cr, long idPortefeuille, long idTransaction) {
        ContentValues cv = new ContentValues();
        cv.put(BudgetTransaction.KEY_COL_ID_TRANSACTION, idTransaction);
        cv.put(BudgetTransaction.KEY_COL_ID_BUDGET, libelle);
        Uri uriAdd = cr.insert(BudgetTransaction.contentUriCollection(idPortefeuille), cv);
        if(uriAdd == null)
            try {
                throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_save_budget));
            } catch (OperationApplicationException e) {
                //TODO : Do better because it is not good here for catch exception
                e.printStackTrace();
            }
    }
    private void deleteBudgetTransaction(ContentResolver cr, long idPortefeuille, long idTransaction, List<Integer> ids) {
        for(Integer id : ids) {

        }
    }
}
