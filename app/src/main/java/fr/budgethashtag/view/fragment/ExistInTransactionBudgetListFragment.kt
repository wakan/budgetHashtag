package fr.budgethashtag.view.fragment

import android.app.Fragment
import android.content.ContentValues
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.widget.Toast
import fr.budgethashtag.adapter.MyBudgetAdapter
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback

class ExistInTransactionBudgetFragment : Fragment(), LoadBudgetsByPortefeuilleIdCallback {
    private lateinit var mAdapter: MyBudgetAdapter
    private val contentValues: ObservableList<ContentValues> = ObservableArrayList<ContentValues>()
    override fun onStart() {
        super.onStart()
        mAdapter = MyBudgetAdapter(contentValues){
            Toast.makeText(this@ExistInTransactionBudgetFragment.activity, "clicked", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onLoadBudgetsByPortefeuilleIdCallback(contentValuesList: List<ContentValues>) {
        contentValues.clear()
        contentValues.addAll(contentValuesList)
        if(isAdded && null != activity && null != view) {
            mAdapter.notifyDataSetChanged()
        }
    }

}