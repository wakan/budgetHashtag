package fr.budgethashtag.service;


import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ServiceManagerImpl implements ServiceManager {

    ArrayList<MotherService> motherServices;
    PortefeuilleService portefeuilleService = null;

    public ServiceManagerImpl(BudgetHashtagApplication application) {
        if (application.serviceManagerAlreadyExist()) {
            throw new ExceptionInInitializerError();
        }
        motherServices=new ArrayList<>();
    }

    @Override
    public void unbindAndDie() {
        Log.e("ServiceManager", "UnbindAndDie is called");
        if (cancelableThreadsExecutor != null) {
            killCancelableThreadExecutor();
        }
        if (keepAliveThreadsExceutor != null) {
            killKeepAliveThreadExecutor();
        }
        for(MotherService service:motherServices){
            service.onDestroy(this);
        }
        portefeuilleService = null;
    }

    @Override
    public final PortefeuilleService getPortefeuilleService() {
        if (null == portefeuilleService) {
            portefeuilleService = new PortefeuilleServiceImpl(this);
            motherServices.add(portefeuilleService);
        }
        return portefeuilleService;
    }

    private ExecutorService cancelableThreadsExecutor = null;
    @Override
    public final ExecutorService getCancelableThreadsExecutor() {
        if (cancelableThreadsExecutor == null) {
            cancelableThreadsExecutor = Executors.newFixedThreadPool(12,
                    new CancelableThreadFactory());
        }
        return cancelableThreadsExecutor;
    }
    private class CancelableThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("CancelableThread"+((int)(Math.random()*1000)));
            return t;
        }
    }
    private void killCancelableThreadExecutor() {
       killExecutor(cancelableThreadsExecutor);
    }
    private void killExecutor( ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdownNow(); // Disable new tasks from being submitted and kill every running task using Thread.interrupt
            try {// as long as your threads hasn't finished
                while (!executorService.isTerminated()) {
                    // Wait a while for existing tasks to terminate
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        // Cancel currently executing tasks
                        executorService.shutdownNow();
                        Log.e(BudgetHashtagApplication.TAG,
                                "Probably a memory leak here");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executorService.shutdownNow();
                executorService=null;
                Log.e(BudgetHashtagApplication.TAG,
                        "Probably a memory leak here too");
            }
        }
        executorService=null;
    }

    private ExecutorService keepAliveThreadsExceutor = null;
    @Override
    public final ExecutorService getKeepAliveThreadsExecutor() {
        if (keepAliveThreadsExceutor == null) {
            keepAliveThreadsExceutor = Executors.newFixedThreadPool(12,
                    new BackgroundThreadFactory());
        }
        return keepAliveThreadsExceutor;
    }
    private class BackgroundThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("KeepAlive"+((int)(Math.random()*1000)));
            return t;
        }
    }
    private void killKeepAliveThreadExecutor() {
        killExecutor(keepAliveThreadsExceutor);
    }

}
