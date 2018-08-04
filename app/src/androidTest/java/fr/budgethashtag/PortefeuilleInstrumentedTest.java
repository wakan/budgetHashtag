package fr.budgethashtag;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PortefeuilleInstrumentedTest {
    @Test
    public void getOrCreatePortefeuilleFirstTest() {
        getOrCreatePortefeuilleTest();
    }
    @Test
    public void getOrCreatePortefeuilleSecondTest() {
        getOrCreatePortefeuilleTest();
    }
    Long idPorteuille = -1L;
    private void getOrCreatePortefeuilleTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        BudgetHashtagApplication.instance.getServiceManager().getPortefeuilleService()
                .getOrCreateDefaultPortefeuilleIfNotExistAsync(appContext)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long id) throws Exception {
                        if (idPorteuille < 0) {
                            idPorteuille = id;
                            assertTrue(idPorteuille >= 0);
                        }
                        else {
                            assertEquals(idPorteuille, id);
                        }
                    }
                });
    }

}
