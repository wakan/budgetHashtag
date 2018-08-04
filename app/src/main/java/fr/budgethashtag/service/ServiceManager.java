package fr.budgethashtag.service;

import fr.budgethashtag.service.portefeuille.PortefeuilleService;

import java.util.concurrent.ExecutorService;

public interface ServiceManager {
    void unbindAndDie();

    PortefeuilleService getPortefeuilleService();

    ExecutorService getCancelableThreadsExecutor();
    ExecutorService getKeepAliveThreadsExecutor();
}
