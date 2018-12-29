package fr.budgethashtag.viewmodel

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableField
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.util.Log
import fr.budgethashtag.asynctask.SaveTransactionAsyncTask
import fr.budgethashtag.asynctask.beanwork.WorkTransactions
import fr.budgethashtag.basecolumns.Transaction
import fr.budgethashtag.helpers.LocationHelper
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
    var bestLocation:Location? = null

    override fun onCreate(extras: Bundle?) {
        if(id > 0) {
            LoadTransactionsByPortefeuilleIdAndIdTransacAsyncTask(
                    mContext, this, id).execute()
        }
        if (extras != null) {
            libelle.set(extras.getString(Transaction.KEY_COL_LIB))
            montant.set(extras.getString(Transaction.KEY_COL_MONTANT))
        }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(Transaction.KEY_COL_LIB, libelle.get())
        outState.putString(Transaction.KEY_COL_MONTANT, montant.get())
    }
    override fun onPause() {
        if(id <= 0) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(locationListener)
        }
    }
    override fun onResume() {
        if(id <= 0) {
            startLocation()
        }
    }

    private fun startLocation() {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT >= 23
                && checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, locationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListener)
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0L, 0F, locationListener)
    }

    override fun onDestroy() {
    }

    override fun onLoadTransactionsByPortefeuilleIdAndIdTransac(contentValues: ContentValues?) {
        libelle.set(contentValues!!.get(Transaction.KEY_COL_LIB) as? String)
        val montantDb = contentValues.get(Transaction.KEY_COL_MONTANT)
        montant.set(montantDb.toString())
    }

    private var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if(null == bestLocation) {
                bestLocation = location
            } else {
                if(LocationHelper.isBetterLocation(location, bestLocation)) {
                    bestLocation = location
                }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }


    fun onClickedBtnAddTransactionActivity(value: Int) {
        when (value) {
            0 -> {
                Log.i(TAG, "Libelle : ${libelle.get()} Montant : ${montant.get()}")
                val date = Date()
                SaveTransactionAsyncTask(mContext,
                        id,
                        libelle.get(), date, montant.get()!!.toDouble(),
                        WorkTransactions(),
                        bestLocation?.provider,
                        bestLocation?.longitude, bestLocation?.latitude, bestLocation?.altitude,
                        bestLocation?.accuracy?.toDouble()).execute()

            }
        }
    }


}