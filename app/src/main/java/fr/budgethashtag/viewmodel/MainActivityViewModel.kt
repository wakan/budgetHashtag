package fr.budgethashtag.viewmodel

import android.content.Context
import android.os.Bundle
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask
import fr.budgethashtag.asynctask.LoadBudgetsByPortefeuilleIdAsyncTask
import fr.budgethashtag.asynctask.LoadPortefeuilleByIdAsyncTask
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAsyncTask
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback
import fr.budgethashtag.view.activity.MainActivity


class MainActivityViewModel(context: Context) : ILifeCycleViewModel, CreateDefaultPortefeuilleIfNotExistCallback
{
    private val TAG: String = "MainActivityViewModel"
    private val mContext: Context = context

    override fun onCreate(extras: Bundle?) {
        CreateDefaultPortefeuilleIfNotExistAsyncTask(mContext, this).execute()
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

    override fun onCreateDefaultPortefeuilleIfNotExist(idPortefeuille: Int) {
        loadPortefeuilleName()
        loadTransactions()
        loadBudgets()
    }

    private fun loadPortefeuilleName() {
        LoadPortefeuilleByIdAsyncTask(mContext, mContext as MainActivity).execute()
    }

    private fun loadTransactions() {
        LoadTransactionsByPortefeuilleIdAsyncTask(mContext, mContext as MainActivity).execute()
    }

    private fun loadBudgets() {
        LoadBudgetsByPortefeuilleIdAsyncTask(mContext, mContext as MainActivity).execute()
    }

}