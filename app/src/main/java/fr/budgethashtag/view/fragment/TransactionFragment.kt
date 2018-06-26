package fr.budgethashtag.view.fragment

import android.app.Fragment
import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R
import fr.budgethashtag.databinding.FragmentTransactionBinding
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback
import fr.budgethashtag.viewmodel.TransactionFragmentViewModel

class TransactionFragment : Fragment(), LoadTransactionsByPortefeuilleIdCallback {
    private val TAG: String = "TransactionFragment"
    private lateinit var viewModel : TransactionFragmentViewModel
    private lateinit var binding: FragmentTransactionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        viewModel = TransactionFragmentViewModel(activity)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onLoadTransactionsByPortefeuilleId(contentValuesList: List<ContentValues>) {

    }
}
