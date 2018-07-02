package fr.budgethashtag.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import fr.budgethashtag.R
import fr.budgethashtag.adapter.MyBudgetAdapter
import fr.budgethashtag.basecolumns.Budget
import fr.budgethashtag.databinding.FragmentBudgetBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
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

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));



        class itemClickListener: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@BudgetFragment.activity, "TEST: " + position, Toast.LENGTH_SHORT).show()
            }
        }

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.adapter = MyBudgetAdapter(contentValuesList){
            Toast.makeText(this@BudgetFragment.activity, "TEST: " + it.get(Budget.KEY_COL_LIB) as? String, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRefresh() {

        viewModel.reloadBudgets()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentBudget!!.swipeRefreshBudgetLayout.isRefreshing = false

    }
}
