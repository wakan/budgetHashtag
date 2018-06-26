package fr.budgethashtag.view.fragment

import android.app.Fragment
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R
import fr.budgethashtag.databinding.FragmentBudgetBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.viewmodel.BudgetFragmentViewModel


class BudgetFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback {

    private val TAG: String = "BudgetFragment"
    private lateinit var viewModel : BudgetFragmentViewModel
    private lateinit var binding: FragmentBudgetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false)
        viewModel = BudgetFragmentViewModel(activity)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {

    }
}
