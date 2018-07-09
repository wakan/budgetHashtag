package fr.budgethashtag.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.util.Log
import fr.budgethashtag.asynctask.SaveBudgetAsyncTask
import fr.budgethashtag.basecolumns.Budget


class AddBudgetViewModel(context: Context) : ILifeCycleViewModel
{
    private val TAG: String = "AddBudgetViewModel"
    private val mContext: Context = context
    var libelle = ObservableField<String>("")
    var montant = ObservableField<String>("")

    override fun onCreate(extras: Bundle?) {
        if (extras != null) {
            libelle.set(extras.getString(Budget.KEY_COL_LIB))
            montant.set(extras.getString(Budget.KEY_COL_PREVISIONNEL))
        }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(Budget.KEY_COL_LIB, libelle.get())
        outState.putString(Budget.KEY_COL_PREVISIONNEL, montant.get())
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

    fun onClickedBtnAddBudgetActivity(value: Int) {
        when (value) {
            0 -> {
                Log.i(TAG, "Libelle : ${libelle.get()} Montant : ${montant.get()}")
                val color = ""
                SaveBudgetAsyncTask(mContext, libelle.get(), montant.get()!!.toDouble(), color).execute()
            }
        }
    }

}