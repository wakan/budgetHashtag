package fr.budgethashtag.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import fr.budgethashtag.R
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.databinding.ActivityAddOrUpdateTransactionBinding
import fr.budgethashtag.viewmodel.AddOrUpdateTransactionViewModel

class AddOrUpdateTransactionActivity : AppCompatActivity() {
    private val TAG: String = "AddOrUpdateTransactionActivity"
    private val MY_PERMISSION_ACCESS_COURSE_LOCATION = 5142
    private lateinit var viewModel:  AddOrUpdateTransactionViewModel
    private lateinit var toolbar: Toolbar

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions( this, arrayOf( android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                android.Manifest.permission.ACCESS_FINE_LOCATION),
                                                    MY_PERMISSION_ACCESS_COURSE_LOCATION )

        val binding: ActivityAddOrUpdateTransactionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_update_transaction)
        val id = intent.getIntExtra(Transaction.KEY_COL_ID, -1)
        viewModel = AddOrUpdateTransactionViewModel(this, id)
        binding.viewModel = viewModel
        viewModel.onCreate(savedInstanceState)

        toolbar = binding.toolbarAddOrUpdateTransaction as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.transaction)
    }

    public override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    public override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  {
        when (item.itemId) {
            R.id.action_add -> {
                viewModel.onClickedBtnAddTransactionActivity(0)
            }
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
