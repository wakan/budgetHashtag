package fr.budgethashtag;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PortefeuilleInstrumentedTest {
    @Before
    public void SetUp() {
        RxJavaPlugins.reset();
    }
    @After
    public void tearDown(){
        RxJavaPlugins.reset();
    }

    @Test
    public void getOrCreatePortefeuilleFirstTest() throws InterruptedException {
        getOrCreatePortefeuilleTest();
    }
    @Test
    public void getOrCreatePortefeuilleSecondTest() throws InterruptedException {
        getOrCreatePortefeuilleTest();
    }
    Long idPorteuille = -1L;
    private void getOrCreatePortefeuilleTest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Observable<Long> obs = BudgetHashtagApplication.instance.getServiceManager().getPortefeuilleService()
                .getOrCreateDefaultPortefeuilleIfNotExistAsync(appContext)
                .observeOn(AndroidSchedulers.mainThread());
        TestObserver<Long> testObserver = obs.test();
        testObserver.awaitDone(15, TimeUnit.SECONDS);
        testObserver.assertValue(new Predicate<Long>() {
            @Override
            public boolean test(Long id) throws Exception {
                if (idPorteuille < 0) {
                    idPorteuille = id;
                    return idPorteuille >= 0;
                } else {
                    return idPorteuille == id;
                }
            }
        });
    }
}
