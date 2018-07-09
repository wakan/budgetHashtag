package fr.budgethashtag.view.fragment;

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R

class AddOrUpdateBudgetInTransactionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_or_update_budget_in_transaction, container, false)
    }



}
