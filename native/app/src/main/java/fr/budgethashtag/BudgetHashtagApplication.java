package fr.budgethashtag;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.ServiceManagerImpl;
import net.danlew.android.joda.JodaTimeAndroid;

import java.util.concurrent.atomic.AtomicInteger;

public class BudgetHashtagApplication extends Application {
    public static final String TAG = "BudHashApplication";
    public static BudgetHashtagApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate is called");
        instance = this;
        JodaTimeAndroid.init(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "onTerminate is called");
    }

    private boolean servcieManagerAlreadyExist = false;
    //TODO Voir pour implementer un Singleton
    private ServiceManager serviceManager;
    public ServiceManager getServiceManager(){
        if(null == serviceManager){
            //TODO Inject dependency for testing
            serviceManager = new ServiceManagerImpl(this);
            servcieManagerAlreadyExist = true;
        }
        return serviceManager;
    }

    public final boolean serviceManagerAlreadyExist() {
        return servcieManagerAlreadyExist;
    }

    public final Context getContext(){
        return getApplicationContext();
    }

    private AtomicInteger isActivityAlive=new AtomicInteger(0);
    public void onStartActivity() {
        Log.e(TAG, "onStartActivity() called with: " + "");
        isActivityAlive.set(isActivityAlive.get()+1);
        mServiceKillerHandler.postDelayed(mServiceKiller, 1000);
    }
    public void onStopActivity() {
        Log.e(TAG, "onStopActivity() called with: " + "");
        isActivityAlive.set(isActivityAlive.get()-1);
        mServiceKillerHandler.postDelayed(mServiceKiller, 1000);
    }
    Runnable mServiceKiller;
    Handler mServiceKillerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "in the Handler isActivityAlive==" + isActivityAlive.get());
            if(isActivityAlive.get()==0) {
                applicationShouldDie();
            }
        }
    };
    private void applicationShouldDie(){
        Log.e(TAG,"applicationShouldDie is called");
        killServiceManager();
    }
    
    @Override
    public void onLowMemory() {
        Log.w(TAG,"onLowMemory is called");
        super.onLowMemory();
        killServiceManager();
    }
    private void killServiceManager() {
        Log.e("MyApplication", "killServiceManager is called");
        if (null != serviceManager) {
            serviceManager.unbindAndDie();
            serviceManager = null;
            servcieManagerAlreadyExist = false;
        }
    }



}
