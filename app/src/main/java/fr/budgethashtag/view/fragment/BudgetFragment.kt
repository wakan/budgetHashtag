package fr.budgethashtag.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.databinding.DataBindingUtil
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


class BudgetFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback, SwipeRefreshLayout.OnRefreshListener {

    private val TAG: String = "BudgetFragment"
    private lateinit var viewModel : BudgetFragmentViewModel
    private lateinit var binding: FragmentBudgetBinding
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false)
        viewModel = BudgetFragmentViewModel(activity as Activity, this)
        binding.viewModel = viewModel

        recyclerView = binding.includeContentFragmentBudget!!.budgetRecyclerView

        binding.includeContentFragmentBudget!!.swipeRefreshBudgetLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {


        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.adapter = MyBudgetAdapter(contentValuesList){
            val intent = Intent(this@BudgetFragment.activity, UpdateBudgetActivity::class.java)
            intent.putExtra(Budget.KEY_COL_ID,  it.get(Budget.KEY_COL_ID) as Int)
            this@BudgetFragment.activity!!.startActivity(intent)
        }
    }

    override fun onRefresh() {

        viewModel.reloadBudgets()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentBudget!!.swipeRefreshBudgetLayout.isRefreshing = false

    }
}
