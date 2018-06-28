package fr.budgethashtag.view.fragment

import android.app.Fragment
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R
import fr.budgethashtag.adapter.MyBudgetAdapter
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
        viewModel = BudgetFragmentViewModel(activity, this)
        binding.viewModel = viewModel

        recyclerView = binding.includeContentFragmentBudget!!.budgetRecyclerView

        binding.includeContentFragmentBudget!!.swipeRefreshLayout.setOnRefreshListener(this)
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        return binding.root
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {


        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.adapter = MyBudgetAdapter(contentValuesList)
    }

    override fun onRefresh() {

        viewModel.reloadBudgets()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentBudget!!.swipeRefreshLayout.isRefreshing = false
       /* mSwipeRefreshLayout.postDelayed(Runnable {
            //appellé après 2000 ms

            //vide la liste
            strings.clear()

            //puis ajoute les nouveaux elements
            for (i in 0..19)
                strings.add("NouvelElement $i")

            //annonce à l'adapter que les données ont changés
            mAdapter.notifyDataSetChanged()

            //avertie le SwipeRefreshLayout que la mise à jour a été effectuée
            mSwipeRefreshLayout.setRefreshing(false)
        }, 2000)*/
    }
}
