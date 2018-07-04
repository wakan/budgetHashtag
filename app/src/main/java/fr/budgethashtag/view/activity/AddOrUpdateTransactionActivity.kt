package fr.budgethashtag.view.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import fr.budgethashtag.R
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.databinding.ActivityAddOrUpdateTransactionBinding
import fr.budgethashtag.viewmodel.AddOrUpdateTransactionViewModel

class AddOrUpdateTransactionActivity : Activity() {
    private val TAG: String = "AddOrUpdateTransactionActivity"
    private val MY_PERMISSION_ACCESS_COURSE_LOCATION = 5142
    private lateinit var viewModel:  AddOrUpdateTransactionViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions( this, arrayOf( android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                android.Manifest.permission.ACCESS_FINE_LOCATION),
                                                    MY_PERMISSION_ACCESS_COURSE_LOCATION );

        val binding: ActivityAddOrUpdateTransactionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_update_transaction)
        val id = intent.getIntExtra(Transaction.KEY_COL_ID, -1)
        viewModel = AddOrUpdateTransactionViewModel(this, id)
        binding.viewModel = viewModel
        viewModel.onCreate(savedInstanceState)
    }

    public override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    public override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
