package fr.budgethashtag.view.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import fr.budgethashtag.R
import fr.budgethashtag.databinding.ActivityAddTransactionBinding
import fr.budgethashtag.viewmodel.AddTransactionViewModel

class AddTransactionActivity : Activity() {
    private val TAG: String = "AddTransactionActivity"
    private var viewModel = AddTransactionViewModel(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityAddTransactionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction)
        binding.viewModel = viewModel
    }
}
