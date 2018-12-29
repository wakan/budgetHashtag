package fr.budgethashtag.helpers;

import android.content.OperationApplicationException;
import android.os.Environment;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathHelper {
    public static String getAndCreateIfNotExistBaseAppPathInExternalStorage() {
        String sBaseAppExternalStorage = Environment.getExternalStorageDirectory() + "/budgetHashtag";
        File fBaseAppExternalStorage = new File(sBaseAppExternalStorage);
        if(!fBaseAppExternalStorage.exists()) {
            fBaseAppExternalStorage.mkdirs();
        }
        return sBaseAppExternalStorage;
    }

    public static String createCsvFileNameBudget(String portefeuilleName) {
        return portefeuilleName + suffixBudgetFileName();
    }
    public static String suffixBudgetFileName(){
        return "b.csv";
    }
    public static String createCsvFileNameTransaction(String portefeuilleName) {
        return portefeuilleName +  suffixTransacFileName();
    }
    public static String suffixTransacFileName() {
        return "t.csv";
    }

    public static List<String> retreivePortefeuilleNameFromImportExportFile(File[] files) throws OperationApplicationException {
        List<String> ret = new ArrayList<>(files.length);
        for(File file : files) {
            String portefeuilleName = retreivePortefeuilleNameFromImportExportFile(file);
            if(!ret.contains(portefeuilleName))
                ret.add(portefeuilleName);
        }
        return ret;
    }

    @NotNull
    public static String retreivePortefeuilleNameFromImportExportFile(File file) throws OperationApplicationException {
        String portefeuilleName = null;
        if(file.getName().contains(suffixBudgetFileName()))
         portefeuilleName = file.getName().substring(0,
                file.getName().length() - suffixBudgetFileName().length());
        else if(file.getName().contains(suffixTransacFileName()))
            portefeuilleName = file.getName().substring(0,
                    file.getName().length() - suffixTransacFileName().length());
        if(null == portefeuilleName)
            throw  new OperationApplicationException("retreivePortefeuilleNameFromImportExportFile " + file.getName());
        return portefeuilleName;
    }

}
