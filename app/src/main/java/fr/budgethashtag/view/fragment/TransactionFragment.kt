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
import fr.budgethashtag.adapter.MyTransactionAdapter
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.databinding.FragmentTransactionBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.view.activity.AddOrUpdateTransactionActivity
import fr.budgethashtag.viewmodel.TransactionFragmentViewModel

class TransactionFragment : Fragment(), LoadTransactionsByPortefeuilleIdCallback, SwipeRefreshLayout.OnRefreshListener {

    private val TAG: String = "TransactionFragment"
    private lateinit var viewModel : TransactionFragmentViewModel
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        viewModel = TransactionFragmentViewModel(activity as Activity, this)
        binding.viewModel = viewModel

        recyclerView = binding.includeContentFragmentTransaction!!.transactionRecyclerView

        binding.includeContentFragmentTransaction!!.swipeRefreshTransactionLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onLoadTransactionsByPortefeuilleId(contentValuesList: List<ContentValues>) {


        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.adapter = MyTransactionAdapter(contentValuesList){
            val intent = Intent(this@TransactionFragment.activity, AddOrUpdateTransactionActivity::class.java)
            intent.putExtra(Transaction.KEY_COL_ID,  it.get(Transaction.KEY_COL_ID) as Int)
            this@TransactionFragment.activity!!.startActivity(intent)
        }
    }

    override fun onRefresh() {

        viewModel.reloadTransactions()
        recyclerView.adapter.notifyDataSetChanged()
        binding.includeContentFragmentTransaction!!.swipeRefreshTransactionLayout.isRefreshing = false

    }
}
