package fr.budgethashtag.asynctask;

import android.content.*;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.constantes.SharedPrefConstantes;
import fr.budgethashtag.helpers.BudgetHelper;
import fr.budgethashtag.helpers.PathHelper;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.helpers.TransactionHelper;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;


public class AllDataImportAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> contextRef;

    public AllDataImportAsyncTask(WeakReference<Context> contextRef) {
        this.contextRef = contextRef;
    }

    @Override
    protected Void doInBackground(Void... params) {
        /*
        ContentResolver cr = contextRef.get().getContentResolver();
        try {
            createPortefeuilleFromFolder();
            setFirstPortefeuilleInDefaultIfNotExit(cr);
            readBudgetToCsv(cr);
            readTransactionToCsv(cr);
        } catch (IOException | OperationApplicationException e) {
            e.printStackTrace();
        }
        */
        return null;
    }
    /*
    private void createPortefeuilleFromFolder() throws OperationApplicationException {
        File[] files = getFilesImportExport();
        List<String> portefeuilles = PathHelper.retreivePortefeuilleNameFromImportExportFile(files);
        for(String portefeuille : portefeuilles) {
            int idPortefeuille = PortefeuilleHelper.createPortefeuille(contextRef.get(), portefeuille);
            mapPortefeuillesIdLib.put(idPortefeuille, portefeuille);
        }
    }
    Map<Integer, String> mapPortefeuillesIdLib = new HashMap<>();

    private File[] getFilesImportExport() {
        String appDirectory = PathHelper.getAndCreateIfNotExistBaseAppPathInExternalStorage();
        File f = new File(appDirectory);
        if(!f.exists() || !f.isDirectory())
            return new File[0];
        return f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith("b.csv")
                       || file.getName().endsWith("t.csv");
            }
        });
    }

    private void setFirstPortefeuilleInDefaultIfNotExit(ContentResolver cr) {
        SharedPreferences appSharedPref = contextRef.get().getSharedPreferences(SharedPrefConstantes.NAME_APP_SHARED_PREF, Context.MODE_PRIVATE);
        if (!appSharedPref.contains(ID_PORTEFEULLE_SELECTED)) {
            try (Cursor cport = cr.query(Portefeuille.contentUriCollection(),
                    null, null, null, null)) {
                Objects.requireNonNull(cport).moveToNext();
                ContentValues cvPort = PortefeuilleHelper.extractContentValueFromCursor(cport);
                Integer idPortefeuille = cvPort.getAsInteger(Budget.KEY_COL_ID_PORTEFEUILLE);
                SharedPreferences.Editor editor = appSharedPref.edit();
                editor.putLong(ID_PORTEFEULLE_SELECTED, idPortefeuille);
                editor.apply();
            }
        }
    }

    private void readBudgetToCsv(ContentResolver cr) throws IOException, OperationApplicationException {
        File[] files = getFilesImportExport();
        for(File f : files) {
            if(f.getName().endsWith(PathHelper.suffixBudgetFileName())) {
                String portefeuilleName = PathHelper.retreivePortefeuilleNameFromImportExportFile(f);
                int idPortefeulle = retreiveIdPortefeuille(portefeuilleName);
                List<String[]> csvValues = readCsvFile(f.getAbsolutePath());
                for (String[] champs : csvValues) {
                    BudgetHelper.createBudget(contextRef.get(), idPortefeulle, champs[0], Double.parseDouble(champs[1]), champs[2]);
                }
            }
        }
    }

    private int retreiveIdPortefeuille(String portefeuilleName) throws OperationApplicationException {
        Set cles = mapPortefeuillesIdLib.keySet();
        for (Object cle1 : cles) {
            int cle = (int) cle1;
            String valeur = mapPortefeuillesIdLib.get(cle);
            if (valeur.equals(portefeuilleName)) {
                return cle;
            }
        }
        throw new OperationApplicationException("retreiveIdPortefeuille : " + portefeuilleName);
    }

    private void readTransactionToCsv(ContentResolver cr) throws IOException, OperationApplicationException {
        File[] files = getFilesImportExport();
        for(File f : files) {
            if(f.getName().endsWith(PathHelper.suffixTransacFileName())) {
                String portefeuilleName = PathHelper.retreivePortefeuilleNameFromImportExportFile(f);
                int idPortefeulle = retreiveIdPortefeuille(portefeuilleName);
                List<String[]> csvValues = readCsvFile(f.getAbsolutePath());
                for (String[] champs : csvValues) {
                    Date dt = new Date();
                    dt.setTime(Long.parseLong(champs[2]));
                    TransactionHelper.createTransaction(contextRef.get(),-1, idPortefeulle, champs[0], Double.parseDouble(champs[1]),
                            dt, champs[3],
                            Double.parseDouble(champs[4]),
                            Double.parseDouble(champs[5]),
                            Double.parseDouble(champs[6]),
                            Double.parseDouble(champs[7]));
                }
            }
        }
    }

    private List<String[]> readCsvFile(String nameOfFile) throws IOException {
        List<String[]> ret = new ArrayList<>();
        String sCurrentLine;
        try (BufferedReader br = new BufferedReader(new FileReader(nameOfFile))) {
            while (null != (sCurrentLine = br.readLine())) {
                ret.add(sCurrentLine.split(";"));
            }
        }
        return ret;
    }
*/
    @Override
    protected void onPostExecute(Void params){
        super.onPostExecute(params);
        Toast.makeText(contextRef.get(),
                contextRef.get().getString(R.string.toast_export_done),
                Toast.LENGTH_LONG).show();
    }

}
