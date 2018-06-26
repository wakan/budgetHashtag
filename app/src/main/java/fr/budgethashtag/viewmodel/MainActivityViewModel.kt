package fr.budgethashtag.viewmodel

import android.content.Context
import android.os.Bundle

class MainActivityViewModel : ILifeCycleViewModel
{
    private val TAG: String = "MainActivityViewModel"
    private val mContext: Context

    constructor(context: Context) {
        mContext = context
    }
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