package fr.budgethashtag.asynctask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.helpers.BudgetHelper;
import fr.budgethashtag.helpers.PathHelper;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.helpers.TransactionHelper;

public class AllDataExportAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> contextRef;

    public AllDataExportAsyncTask(WeakReference<Context> contextRef) {
        this.contextRef = contextRef;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentResolver cr = contextRef.get().getContentResolver();
        try {
            writeBudgetToCsv(cr);
            writeTransactionToCsv(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeBudgetToCsv(ContentResolver cr) throws IOException {
        try (Cursor cport = cr.query(Portefeuille.contentUriCollection(),
                null, null, null, null)) {
            while (Objects.requireNonNull(cport).moveToNext()) {
                StringBuilder sb = new StringBuilder();
                ContentValues cvPort = PortefeuilleHelper.extractContentValueFromCursor(cport);
                Integer idPortefeuille = cvPort.getAsInteger(Budget.KEY_COL_ID_PORTEFEUILLE);
                try (Cursor cbudg = cr.query(Budget.contentUriCollection(idPortefeuille),
                        null, null, null, null)) {
                    while (Objects.requireNonNull(cbudg).moveToNext()) {
                        ContentValues cvBudg = BudgetHelper.extractContentValueFromCursor(cbudg);
                        sb.append(cvBudg.getAsString(Budget.KEY_COL_LIB));
                        sb.append(';');
                        sb.append(cvBudg.getAsFloat(Budget.KEY_COL_PREVISIONNEL));
                        sb.append(';');
                        sb.append(cvBudg.getAsFloat(Budget.KEY_COL_COLOR));
                        sb.append(System.lineSeparator());
                    }
                }
                String libPortefeuille = cvPort.getAsString(Portefeuille.KEY_COL_ID);
                String nameOfFile = PathHelper.getAndCreateIfNotExistBaseAppPathInExternalStorage() + "/" +
                        PathHelper.createCsvFileNameBudget(libPortefeuille);
                writeCsvFile(nameOfFile, sb);
            }
        }
    }

    private void writeTransactionToCsv(ContentResolver cr) throws IOException {
        try (Cursor cport = cr.query(Portefeuille.contentUriCollection(),
                null, null, null, null)) {
            while (Objects.requireNonNull(cport).moveToNext()) {
                StringBuilder sb = new StringBuilder();
                ContentValues cvPort = PortefeuilleHelper.extractContentValueFromCursor(cport);
                Integer idPortefeuille = cvPort.getAsInteger(Budget.KEY_COL_ID_PORTEFEUILLE);
                try (Cursor ctran = cr.query(Transaction.contentUriCollection(idPortefeuille),
                        null, null, null, null)) {
                    while (Objects.requireNonNull(ctran).moveToNext()) {
                        ContentValues cvBudg = TransactionHelper.extractContentValueFromCursor(ctran);
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LIB));
                        sb.append(';');
                        sb.append(cvBudg.getAsFloat(Transaction.KEY_COL_MONTANT));
                        sb.append(';');
                        sb.append(cvBudg.getAsLong(Transaction.KEY_COL_DT_VALEUR));
                        sb.append(';');
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LOCATION_PROVIDER));
                        sb.append(';');
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LOCATION_ACCURACY));
                        sb.append(';');
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LOCATION_LONGITUDE));
                        sb.append(';');
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LOCATION_LATITUDE));
                        sb.append(';');
                        sb.append(cvBudg.getAsString(Transaction.KEY_COL_LOCATION_ALTITUDE));
                        sb.append(System.lineSeparator());
                    }
                }
                String libPortefeuille = cvPort.getAsString(Portefeuille.KEY_COL_ID);
                String nameOfFile = PathHelper.getAndCreateIfNotExistBaseAppPathInExternalStorage() + "/" +
                        PathHelper.createCsvFileNameTransaction(libPortefeuille);
                writeCsvFile(nameOfFile, sb);
            }
        }
    }

    private void writeCsvFile(String nameOfFile, StringBuilder sb) throws IOException {
        File f = new File(nameOfFile);
        if(!f.exists()) {
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(sb.toString());
        bw.close();
        fw.close();
    }

    @Override
    protected void onPostExecute(Void params){
        super.onPostExecute(params);
        Toast.makeText(contextRef.get(),
                contextRef.get().getString(R.string.toast_export_done),
                Toast.LENGTH_LONG).show();
    }

}
