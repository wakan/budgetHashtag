package fr.budgethashtag.view.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import fr.budgethashtag.R
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.databinding.ActivityAddOrUpdateTransactionBinding
import fr.budgethashtag.viewmodel.AddOrUpdateTransactionViewModel

class AddOrUpdateTransactionActivity : Activity() {
    private val TAG: String = "AddOrUpdateTransactionActivity"
    private lateinit var viewModel:  AddOrUpdateTransactionViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddOrUpdateTransactionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_update_transaction)
        val id = intent.getIntExtra(Transaction.KEY_COL_ID, -1)
        viewModel = AddOrUpdateTransactionViewModel(this, id)
        binding.viewModel = viewModel
        viewModel.onCreate(savedInstanceState);
    }
}
