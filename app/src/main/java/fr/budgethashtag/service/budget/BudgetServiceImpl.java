package fr.budgethashtag.service.budget;

import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;

public class BudgetServiceImpl extends MotherServiceImpl implements BudgetService {
    public BudgetServiceImpl(ServiceManager srvManager) {
        super(srvManager);
    }
    @Override
    public void onDestroy() { }


}
