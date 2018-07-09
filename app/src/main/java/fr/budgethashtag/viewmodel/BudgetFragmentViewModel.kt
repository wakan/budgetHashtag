package fr.budgethashtag.viewmodel

import android.content.Context
import android.os.Bundle
import fr.budgethashtag.asynctask.LoadBudgetsByPortefeuilleIdAsyncTask
import fr.budgethashtag.view.activity.AddBudgetActivity
import fr.budgethashtag.view.fragment.BudgetFragment
import org.jetbrains.anko.startActivity

class BudgetFragmentViewModel(context: Context, budgetFragment: BudgetFragment) : ILifeCycleViewModel {
    private val TAG: String = "BudgetFragmentViewModel"
    private val mContext: Context = context
    private val budgetFragment : BudgetFragment = budgetFragment

    override fun onCreate(extras: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    fun onClickedBtnBudgetFragment(value: Int) {
        when (value) {
            0 -> {
                mContext.startActivity<AddBudgetActivity>()
            }
        }
    }

    fun reloadBudgets() {
        LoadBudgetsByPortefeuilleIdAsyncTask(mContext, budgetFragment).execute()
    }

}