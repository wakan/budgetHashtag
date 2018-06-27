package fr.budgethashtag.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.os.Bundle



class MainActivityViewModel(context: Context) : ILifeCycleViewModel, BaseObservable()
{
    private val TAG: String = "MainActivityViewModel"
    private val mContext: Context = context

    override fun onCreate(extras: Bundle) {
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

}