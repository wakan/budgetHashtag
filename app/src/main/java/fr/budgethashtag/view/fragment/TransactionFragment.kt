package fr.budgethashtag.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R
import fr.budgethashtag.databinding.FragmentTransactionBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.viewmodel.TransactionFragmentViewModel

class TransactionFragment : Fragment(), LoadTransactionsByPortefeuilleIdCallback, SwipeRefreshLayout.OnRefreshListener {

    private val TAG: String = "TransactionFragment"
    private lateinit var viewModel : TransactionFragmentViewModel
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var recyclerView : RecyclerView

    private val contentValues: ObservableList<ContentValues> = ObservableArrayList<ContentValues>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        recyclerView = binding.includeContentFragmentTransaction!!.transactionRecyclerView
        viewModel = TransactionFragmentViewModel(activity as Activity, this)
        binding.viewModel = viewModel

        recyclerView = binding.includeContentFragmentTransaction!!.transactionRecyclerView

        binding.includeContentFragmentTransaction!!.swipeRefreshTransactionLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onLoadTransactionsByPortefeuilleId(contentValuesList: List<ContentValues>) {
        contentValues.clear()
        contentValues.addAll(contentValuesList)
    }

    override fun onRefresh() {

        viewModel.reloadTransactions()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentTransaction!!.swipeRefreshTransactionLayout.isRefreshing = false

    }
}
