package fr.budgethashtag.view.fragment

import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fr.budgethashtag.R
import fr.budgethashtag.adapter.MyBudgetAdapter
import fr.budgethashtag.databinding.FragmentAddOrUpdateBudgetInTransactionBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback
import fr.budgethashtag.viewmodel.AddOrUpdateBudgetInTransactionViewModel

class AddOrUpdateBudgetInTransactionFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback {

    private lateinit var viewModel : AddOrUpdateBudgetInTransactionViewModel
    private lateinit var binding: FragmentAddOrUpdateBudgetInTransactionBinding
    private lateinit var recyclerView : RecyclerView

    private val contentValues: ObservableList<ContentValues> = ObservableArrayList<ContentValues>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_or_update_budget_in_transaction, container, false)
        viewModel = AddOrUpdateBudgetInTransactionViewModel()
        binding.viewModel = viewModel

        recyclerView = binding.existInTransBudgetRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this@AddOrUpdateBudgetInTransactionFragment.activity)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recyclerView.adapter = MyBudgetAdapter(contentValues){
            Toast.makeText(this@AddOrUpdateBudgetInTransactionFragment.activity, "clicked !! ", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: MutableList<ContentValues>) {
        contentValues.clear()
        contentValues.addAll(contentValuesList)
        if(isAdded && null != activity && null != view) {
            recyclerView.adapter.notifyDataSetChanged()
        }
    }


}
