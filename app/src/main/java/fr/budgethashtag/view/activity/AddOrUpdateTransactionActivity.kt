package fr.budgethashtag.view.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import fr.budgethashtag.R
import fr.budgethashtag.databinding.ActivityAddOrUpdateTransactionBinding
import fr.budgethashtag.viewmodel.AddOrUpdateTransactionViewModel

class AddOrUpdateTransactionActivity : Activity() {
    private val TAG: String = "AddOrUpdateTransactionActivity"
    private var viewModel = AddOrUpdateTransactionViewModel(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddOrUpdateTransactionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_update_transaction)
        binding.viewModel = viewModel
    }
}
