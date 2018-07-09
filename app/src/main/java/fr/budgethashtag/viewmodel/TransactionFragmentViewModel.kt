package fr.budgethashtag.viewmodel

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAsyncTask
import fr.budgethashtag.view.activity.AddOrUpdateTransactionActivity
import fr.budgethashtag.view.fragment.TransactionFragment
import org.jetbrains.anko.startActivity

class TransactionFragmentViewModel(context: Context,  transactionFragment: TransactionFragment) : ILifeCycleViewModel
{
    private val TAG: String = "TransactionFragmentViewModel"
    private val mContext: Context = context
    private val transactionFragment : TransactionFragment = transactionFragment

    override fun onCreate(extras: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onSaveInstanceState(outState: Bundle?) {
    }
    override fun onPause() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onClickedBtnTransactionFragment(value:Int) {
        when(value) {
            0 -> {
                mContext.startActivity<AddOrUpdateTransactionActivity>()
            }
        }
    }

    fun reloadTransactions() {
        LoadTransactionsByPortefeuilleIdAsyncTask(mContext, transactionFragment).execute()
    }

}