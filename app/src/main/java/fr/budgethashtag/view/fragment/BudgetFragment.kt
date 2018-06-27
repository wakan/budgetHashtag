package fr.budgethashtag.view.fragment

import android.app.Fragment
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
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


class BudgetFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback {

    private val TAG: String = "BudgetFragment"
    private lateinit var viewModel : BudgetFragmentViewModel
    private lateinit var binding: FragmentBudgetBinding
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false)
        viewModel = BudgetFragmentViewModel(activity)
        binding.viewModel = viewModel
        recyclerView = binding.testInclude!!.budgetRecyclerView

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
}
