package fr.budgethashtag.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.util.Log

class AddTransactionViewModel(context: Context) : ILifeCycleViewModel
{
    private val TAG: String = "AddTransactionViewModel"
    private val mContext: Context = context
    var libelle = ObservableField<String>("")
    var montant = ObservableField<String>("")

    override fun onCreate(extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onClickedBtnAddTransactionActivity(value: Int) {
        when (value) {
            0 -> {
                Log.i(TAG, "Libelle : ${libelle.get()} Montant : ${montant.get()}")
                val color = ""
                //SaveBudgetAsyncTask(mContext, libelle.get(), montant.get()!!.toDouble(), color).execute()
            }
        }
    }
}