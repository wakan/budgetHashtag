package fr.budgethashtag.view.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import fr.budgethashtag.R
import fr.budgethashtag.databinding.ActivityAddBudgetBinding
import fr.budgethashtag.viewmodel.AddBudgetViewModel

class AddBudgetActivity : Activity()
{
    private val TAG : String = "AddBudgetActivity"
    private var viewModel = AddBudgetViewModel(this)

    public override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding : ActivityAddBudgetBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget)
        binding.viewModel = viewModel

    }
}
