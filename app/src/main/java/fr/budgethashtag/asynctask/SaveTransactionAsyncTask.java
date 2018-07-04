package fr.budgethashtag.asynctask;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.budgethashtag.R;
import fr.budgethashtag.asynctask.beanwork.WorkTransactions;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.BudgetTransaction;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.helpers.PortefeuilleHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        ContentResolver cr = contextRef.get().getContentResolver();
        long idPortefeuille = PortefeuilleHelper.getIdPortefeuilleFromSharedPref(contextRef);
        long idTransaction = insertTransaction(cr, idPortefeuille);
        List<Integer> idsInsert = insertNewBudget(cr, idPortefeuille, transactions.getTransactionsNouvelles());
        insertBudgetTransaction(cr, idPortefeuille, idTransaction, idsInsert, transactions.getTransactionsExistantesAjoutees());
        deleteBudgetTransaction(cr, idTransaction, idPortefeuille, transactions.getTransactionsExistantesSupprimees());
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ((Activity) contextRef.get()).finish();
        Toast.makeText(contextRef.get(), "Ajout OK", Toast.LENGTH_SHORT).show();
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

    private long insertTransaction(ContentResolver cr, long idPortefeuille) {
        ContentValues cv = new ContentValues();
        cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
        cv.put(Transaction.KEY_COL_LIB, libelle);
        cv.put(Transaction.KEY_COL_DT_VALEUR, date.getTime());
        cv.put(Transaction.KEY_COL_MONTANT, montant);
        if(id > 0) {
            cr.update(Transaction.contentUriItem(idPortefeuille, id), cv, null, null);
            return id;
        } else {
            if(null != locationProvider) {
                cv.put(Transaction.KEY_COL_LOCATION_PROVIDER, locationProvider);
                cv.put(Transaction.KEY_COL_LOCATION_ACCURACY, accuracy);
                cv.put(Transaction.KEY_COL_LOCATION_ALTITUDE, altitude);
                cv.put(Transaction.KEY_COL_LOCATION_LATITUDE, latitude);
                cv.put(Transaction.KEY_COL_LOCATION_LONGITUDE, longitude);
            }
            Uri uriAdd = cr.insert(Transaction.contentUriCollection(idPortefeuille), cv);
            if (uriAdd == null)
                try {
                    throw new OperationApplicationException(contextRef.get().getString(R.string.ex_msg_save_budget));
                } catch (OperationApplicationException e) {
                    //TODO : Do better because it is not good here for catch exception
                    e.printStackTrace();
                }
            return Integer.parseInt(Objects.requireNonNull(uriAdd).getPathSegments().get(3));
        }
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
