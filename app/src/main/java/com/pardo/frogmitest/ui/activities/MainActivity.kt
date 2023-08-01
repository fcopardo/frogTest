package com.pardo.frogmitest.ui.activities

import android.R
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.platform.extensions.getActivityRootFrame
import com.pardo.frogmitest.platform.extensions.getComposeRoot
import com.pardo.frogmitest.platform.threading.Scopes
import com.pardo.frogmitest.ui.screens.StoresScreen
import com.pardo.frogmitest.ui.theme.FrogmiTestTheme
import com.pardo.frogmitest.ui.views.containers.OSMapViewContainer
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


class MainActivity : ComponentActivity() {

    lateinit var rootFrame: FrameLayout
    lateinit var map: OSMapViewContainer
    lateinit var composeView: ComposeView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        rootFrame = getActivityRootFrame()
        setContent {
            FrogmiTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StoresScreen({ item : StoreCellData ->
                        if(item.latitude!=null && item.latitude!=0.0 && item.longitude!=null && item.longitude != 0.0){
                            composeView.visibility = GONE
                            map.visibility = View.VISIBLE
                            val mapController: IMapController = map.mapView.controller
                            mapController.setZoom(9.5)
                            val startPoint = GeoPoint(item.latitude!!, item.longitude!!)
                            mapController.setCenter(startPoint)
                        }
                    })
                }
            }
        }
        composeView = getComposeRoot()
        map = OSMapViewContainer(MapView(this), rootFrame)

        onBackPressedDispatcher.addCallback {
            if(map.visibility == VISIBLE){
                map.visibility = View.GONE
                composeView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }


    override fun onPause() {
        map.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Scopes.end()
        map.onDetach()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val permissionsToRequest = ArrayList<String>()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray<String>(),
                125
            )
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray<String>(),
                125
            )
        }
    }
}