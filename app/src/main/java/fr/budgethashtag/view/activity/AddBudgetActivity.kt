package fr.budgethashtag.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import fr.budgethashtag.R
import fr.budgethashtag.databinding.ActivityAddBudgetBinding
import fr.budgethashtag.viewmodel.AddBudgetViewModel

class AddBudgetActivity : AppCompatActivity()
{
    private val TAG : String = "AddBudgetActivity"
    private var viewModel = AddBudgetViewModel(this)
    private lateinit var toolbar: Toolbar

    public override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding : ActivityAddBudgetBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget)
        binding.viewModel = viewModel

        toolbar = binding.toolbarAddBudget as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.budget)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  {
        when (item.itemId) {
            R.id.action_add -> {
                viewModel.onClickedBtnAddBudgetActivity(0)
            }
        }

        return super.onOptionsItemSelected(item)


    }
}
