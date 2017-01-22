package ru.mail.sporttogether.managers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.subjects.BehaviorSubject

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class LocationManager(val context: Context) :
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private var googleApiClient: GoogleApiClient? = null
    private var locationUpdateRequest: LocationRequest = LocationRequest()
    private var locationUpdating = false
    private var pendingAction = false
    private var pendingSingleEvent = false

    private var lastLocation: Location? = null
    private var onLocationAction: ((Location) -> Unit)? = null

    val locationUpdate: BehaviorSubject<Location> = BehaviorSubject.create()

    fun getLocation(): Location? = lastLocation

    override fun onLocationChanged(location: Location?) {
        location?.let {
            onLocationAction?.invoke(it)
            onLocationAction = null

            locationUpdate.onNext(it)
            lastLocation = it
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnected(p0: Bundle?) {
        update(true)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    init {
        try {
            googleApiClient = GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()

            googleApiClient?.connect()
        } catch (t: Exception) {

        }

        locationUpdateRequest.interval = 15000
        locationUpdateRequest.fastestInterval = 5000
        locationUpdateRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    @PermissionChecker.PermissionResult
    fun checkForPermissions(): Boolean {
        val applicationContext = context

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    fun startLocationUpdate() {
        locationUpdating = true

        val connected = googleApiClient?.isConnected ?: false
        if (checkForPermissions()) {
            if (googleApiClient != null && connected) {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                            locationUpdateRequest,
                            this)
                } catch (t: Throwable) {

                }
            }
        } else {
            pendingAction = true
        }
    }

    fun endLocationUpdate() {
        locationUpdating = false


        val connected = googleApiClient?.isConnected ?: false
        if (googleApiClient != null && connected) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            } catch (t: Throwable) {

            }
        }

    }

    private fun validatePermissionOrRequest(ignoreIfNoPermission: Boolean = false): Boolean {
        if (!checkForPermissions()) {
            if (ignoreIfNoPermission) {
                return false
            }
            return false
        }

        return true
    }

    fun updateWithAction(onLocation: (Location) -> Unit) {
        onLocationAction = onLocation

        update(true)
    }

    fun update() = update(false)

    fun update(ignoreIfNoPermission: Boolean) {
        val client = googleApiClient
        if (client != null) {
            val singleLocationUpdateRequest = LocationRequest()
            singleLocationUpdateRequest.interval = 10000
            singleLocationUpdateRequest.fastestInterval = 5000
            singleLocationUpdateRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            singleLocationUpdateRequest.numUpdates = 1

            if (!client.isConnected || client.isConnecting) {
                return
            }

            if (validatePermissionOrRequest(ignoreIfNoPermission)) {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(client,
                            singleLocationUpdateRequest,
                            this)
                } catch (t: Throwable) {

                }
            } else {
                if (!ignoreIfNoPermission)
                    pendingSingleEvent = true
            }
        }
    }

    fun checkGPSLocation(context: Context) =
            checkLocationByProvider(context, LocationManager.GPS_PROVIDER)

    fun checkNetLocation(context: Context) =
            checkLocationByProvider(context, LocationManager.NETWORK_PROVIDER)

    fun checkLocationEnabled(context: Context)
            = checkGPSLocation(context) || checkNetLocation(context)

    private fun checkLocationByProvider(context: Context, provider: String): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(provider)
    }


}