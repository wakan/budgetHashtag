package fr.budgethashtag.service;

public abstract class MotherServiceImpl implements MotherService {
    public MotherServiceImpl(ServiceManager srvManager) {
    }

    public void onDestroy(ServiceManager srvManager){
        onDestroy();
    }
}
