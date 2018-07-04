package fr.budgethashtag.viewmodel

import android.content.ContentValues
import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.util.Log
import fr.budgethashtag.asynctask.LoadTransactionsByPortefeuilleIdAndIdTransacAsyncTask
import fr.budgethashtag.asynctask.SaveTransactionAsyncTask
import fr.budgethashtag.asynctask.beanwork.WorkTransactions
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdAndIdTransacCallback
import java.util.*

class AddOrUpdateTransactionViewModel(context: Context, id: Int)
    : ILifeCycleViewModel,
        LoadTransactionsByPortefeuilleIdAndIdTransacCallback
{

    private val TAG: String = "AddOrUpdateTransVM"
    private val mContext: Context = context
    private val id = id
    var libelle = ObservableField<String>("")
    var montant = ObservableField<String>("")

    override fun onCreate(extras: Bundle?) {
        if(id > 0) {montant
            LoadTransactionsByPortefeuilleIdAndIdTransacAsyncTask(
                    mContext, this, id).execute()
        }
    }
    override fun onPause() {
    }
    override fun onResume() {
    }
    override fun onDestroy() {
    }

    override fun onLoadTransactionsByPortefeuilleIdAndIdTransac(contentValues: ContentValues?) {
        libelle.set(contentValues!!.get(Transaction.KEY_COL_LIB) as? String)
        val montantDb = contentValues.get(Transaction.KEY_COL_MONTANT)
        montant.set(montantDb.toString())
    }

    fun onClickedBtnAddTransactionActivity(value: Int) {
        when (value) {
            0 -> {
                Log.i(TAG, "Libelle : ${libelle.get()} Montant : ${montant.get()}")
                val date = Date()
                SaveTransactionAsyncTask(mContext,
                        id,
                        libelle.get(),date, montant.get()!!.toDouble(),
                        WorkTransactions() ,
                        null, null, null, null, null).execute()

            }
        }
    }


}