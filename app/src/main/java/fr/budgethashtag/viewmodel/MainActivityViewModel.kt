package fr.budgethashtag.viewmodel

import android.content.ContentValues
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Bundle
import fr.budgethashtag.asynctask.LoadBudgetsByPortefeuilleIdAsyncTask
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAsyncTask
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.view.fragment.BudgetFragment
import fr.budgethashtag.view.fragment.TransactionFragment
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import fr.budgethashtag.service.portefeuille.PortefeuilleObservable
import fr.budgethashtag.view.fragment.ViewPagerAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber


class MainActivityViewModel(context: AppCompatActivity) : BaseObservable(),
        ILifeCycleViewModel
{


    private val TAG: String = "MainActivityViewModel"
    private val mContext: AppCompatActivity = context
    lateinit var  adapter: ViewPagerAdapter



    override fun onCreate(extras: Bundle?) {
        PortefeuilleObservable.getOrCreateDefaultPortefeuilleIfNotExist(mContext)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    loadPortefeuilleName()
                },
                        { error -> Toast.makeText(mContext, error.message, Toast.LENGTH_LONG).show() }
                )
        createViewPager()
    }
    override fun onSaveInstanceState(outState: Bundle?) {
    }
    override fun onPause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadPortefeuilleName() {
        PortefeuilleObservable.getPortefeuilleById(mContext)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    //supportActionBar!!.title = contentValues.getAsString(Portefeuille.KEY_COL_LIB)
                },
                        { error -> Toast.makeText(mContext, error.message, Toast.LENGTH_LONG).show() }
                )
    }

    val pagerAdapter: PagerAdapter
        @Bindable get()= adapter

    private fun createViewPager() {
        adapter = ViewPagerAdapter(mContext.getSupportFragmentManager())
        adapter.addFragment(BudgetFragment(), "Budget")
        adapter.addFragment(TransactionFragment(), "Transaction")

        //notifyPropertyChanged(BR.pagerAdapter)
    }



}