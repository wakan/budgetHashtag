package fr.budgethashtag.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R
import fr.budgethashtag.adapter.MyBudgetAdapter
import fr.budgethashtag.basecolumns.Budget
import fr.budgethashtag.databinding.FragmentBudgetBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.view.activity.UpdateBudgetActivity
import fr.budgethashtag.viewmodel.BudgetFragmentViewModel
import org.jetbrains.anko.startActivity


class BudgetFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback, SwipeRefreshLayout.OnRefreshListener {

    private val TAG: String = "BudgetFragment"
    private lateinit var viewModel : BudgetFragmentViewModel
    private lateinit var binding: FragmentBudgetBinding
    private lateinit var recyclerView : RecyclerView

    private val contentValues: ObservableList<ContentValues> = ObservableArrayList<ContentValues>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false)
        viewModel = BudgetFragmentViewModel(activity as Activity, this)
        binding.viewModel = viewModel

        recyclerView = binding.includeContentFragmentBudget!!.budgetRecyclerView

        binding.includeContentFragmentBudget!!.swipeRefreshBudgetLayout.setOnRefreshListener(this)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recyclerView!!.adapter = MyBudgetAdapter(contentValues){
            this@BudgetFragment.activity!!.startActivity<UpdateBudgetActivity>(
                    Budget.KEY_COL_ID to it.get(Budget.KEY_COL_ID) as Int)
        }
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {
        contentValues.clear()
        contentValues.addAll(contentValuesList)
        if(isAdded && null != activity && null != view) {
            recyclerView!!.adapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        viewModel.reloadBudgets()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentBudget!!.swipeRefreshBudgetLayout.isRefreshing = false
    }

}
