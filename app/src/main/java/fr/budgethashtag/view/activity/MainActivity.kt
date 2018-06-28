package fr.budgethashtag.view.activity

import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toolbar
import fr.budgethashtag.R
import fr.budgethashtag.contentprovider.PortefeuilleProvider
import fr.budgethashtag.databinding.ActivityMainBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadPortefeuilleByIdCallback
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.view.fragment.BudgetFragment
import fr.budgethashtag.view.fragment.TransactionFragment
import fr.budgethashtag.view.fragment.ViewPagerAdapter
import fr.budgethashtag.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity(), LoadPortefeuilleByIdCallback, LoadTransactionsByPortefeuilleIdCallback, LoadBudgetsByPortefeuilleIdCallback {

    private val TAG: String = "MainActivity"
    private var viewModel = MainActivityViewModel(this)
    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private lateinit var transactionFragment : TransactionFragment
    private lateinit var budgetFragment : BudgetFragment

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        viewPager = binding.viewpager
        tabLayout = binding.tabs
        tabLayout.setupWithViewPager(viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        budgetFragment = BudgetFragment()
        transactionFragment = TransactionFragment()
        setupViewPager(binding.viewpager)

        viewModel.onCreate(savedInstanceState)

    }

    override fun onLoadPortefeuilleById(contentValues: ContentValues) {
        binding.toolbar.title = contentValues.getAsString(PortefeuilleProvider.Portefeuille.KEY_COL_LIB)
    }

    override fun onLoadTransactionsByPortefeuilleId(contentValuesList: List<ContentValues>) {

        transactionFragment.onLoadTransactionsByPortefeuilleId(contentValuesList)
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {
        budgetFragment.onLoadBudgetsByPortefeuilleIdCallback(contentValuesList)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(budgetFragment, "Budget")
        adapter.addFragment(transactionFragment, "Transaction")
        viewPager.adapter = adapter
    }

}
