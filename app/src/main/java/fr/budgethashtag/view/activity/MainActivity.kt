package fr.budgethashtag.view.activity

import android.app.Activity
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
import fr.budgethashtag.R
import fr.budgethashtag.asynctask.CreateDefaultPortefeuilleIfNotExistAsyncTask
import fr.budgethashtag.asynctask.LoadBudgetsByPortefeuilleIdAsyncTask
import fr.budgethashtag.asynctask.LoadPortefeuilleByIdAsyncTask
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAsyncTask
import fr.budgethashtag.contentprovider.PortefeuilleProvider
import fr.budgethashtag.databinding.ActivityMainBinding
import fr.budgethashtag.interfacecallbackasynctask.CreateDefaultPortefeuilleIfNotExistCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.view.fragment.BudgetFragment
import fr.budgethashtag.view.fragment.TransactionFragment
import fr.budgethashtag.viewmodel.MainActivityViewModel

class MainActivity : Activity(), CreateDefaultPortefeuilleIfNotExistCallback, LoadPortefeuilleByIdCallback, LoadTransactionsByPortefeuilleIdCallback, LoadBudgetsByPortefeuilleIdCallback {

    private val TAG: String = "MainActivity"
    private var viewModel = MainActivityViewModel(this)
    lateinit var binding: ActivityMainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        CreateDefaultPortefeuilleIfNotExistAsyncTask(this, this).execute()
    }

    override fun onCreateDefaultPortefeuilleIfNotExist(idPortefeuille: Int) {
        loadPortefeuilleName()
        loadTransactions()
        loadBudgets()
    }

    private fun loadPortefeuilleName() {
        LoadPortefeuilleByIdAsyncTask(this, this).execute()
    }

    private fun loadTransactions() {
        LoadTransactionsByPortefeuilleIdAsyncTask(this, this).execute()
    }

    private fun loadBudgets() {
        LoadBudgetsByPortefeuilleIdAsyncTask(this, this).execute()
    }

    override fun onLoadPortefeuilleById(contentValues: ContentValues) {
        binding.toolbar.title = contentValues.getAsString(PortefeuilleProvider.Portefeuille.KEY_COL_LIB)
    }

    override fun onLoadTransactionsByPortefeuilleId(contentValuesList: List<ContentValues>) {
        val transactionFragment = fragmentManager.findFragmentById(R.id.frg_transaction) as TransactionFragment
        transactionFragment.onLoadTransactionsByPortefeuilleId(contentValuesList)
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {
        val budgetFragment = fragmentManager.findFragmentById(R.id.frg_budget) as BudgetFragment
        budgetFragment.onLoadBudgetsByPortefeuilleIdCallback(contentValuesList)
    }

}
