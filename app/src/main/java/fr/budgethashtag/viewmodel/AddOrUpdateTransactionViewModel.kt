package fr.budgethashtag.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.util.Log
import fr.budgethashtag.asynctask.SaveTransactionAsyncTask
import fr.budgethashtag.asynctask.beanwork.WorkTransactions
import java.util.*

class AddOrUpdateTransactionViewModel(context: Context) : ILifeCycleViewModel
{
    private val TAG: String = "AddOrUpdateTransVM"
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
                val date = Date();
                SaveTransactionAsyncTask(mContext,
                        libelle.get(),date, montant.get()!!.toDouble(),
                        WorkTransactions() ,
                        null, null, null, null, null).execute();
            }
        }
    }
}