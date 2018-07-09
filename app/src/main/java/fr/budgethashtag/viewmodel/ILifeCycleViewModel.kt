package fr.budgethashtag.viewmodel

import android.os.Bundle

interface ILifeCycleViewModel {
    fun onCreate(extras: Bundle?)
    fun onPause()
    fun onResume()
    fun onDestroy()
    fun onSaveInstanceState(outState: Bundle?)
}