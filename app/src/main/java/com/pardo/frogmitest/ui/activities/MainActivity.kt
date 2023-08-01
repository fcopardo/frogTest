package com.pardo.frogmitest.ui.activities

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.InputDevice
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.OnGenericMotionListener
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
import com.pardo.frogmitest.platform.threading.Scopes
import com.pardo.frogmitest.ui.screens.StoresScreen
import com.pardo.frogmitest.ui.theme.FrogmiTestTheme
import org.osmdroid.api.IGeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MainActivity : ComponentActivity() {

    private val PREFS_NAME = "org.andnav.osm.prefs"
    private val PREFS_TILE_SOURCE = "tilesource"
    private val PREFS_LATITUDE_STRING = "latitudeString"
    private val PREFS_LONGITUDE_STRING = "longitudeString"
    private val PREFS_ORIENTATION = "orientation"
    private val PREFS_ZOOM_LEVEL_DOUBLE = "zoomLevelDouble"

    private val MENU_ABOUT = Menu.FIRST + 1
    private val MENU_LAST_ID = MENU_ABOUT + 1 // Always set to last unused id


    // ===========================================================
    // Fields
    // ===========================================================
    private var mPrefs: SharedPreferences? = null
    private var mLocationOverlay: MyLocationNewOverlay? = null
    private var mCompassOverlay: CompassOverlay? = null
    private var mMinimapOverlay: MinimapOverlay? = null
    private var mScaleBarOverlay: ScaleBarOverlay? = null
    private var mRotationGestureOverlay: RotationGestureOverlay? = null
    private var mCopyrightOverlay: CopyrightOverlay? = null

    lateinit var rootFrame: FrameLayout
    lateinit var mapView: MapView
    lateinit var composeView: ComposeView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        rootFrame = findViewById<View>(R.id.content) as FrameLayout
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
                            mapView.visibility = View.VISIBLE
                            val mapController: IMapController = mapView.controller
                            mapController.setZoom(9.5)
                            val startPoint = GeoPoint(item.latitude!!, item.longitude!!)
                            mapController.setCenter(startPoint)
                        }
                    })
                }
            }
        }
        composeView = (window.decorView.findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ComposeView)
        setUpMap()

        onBackPressedDispatcher.addCallback {
            if(mapView.visibility == VISIBLE){
                mapView.visibility = View.GONE
                composeView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }


    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Scopes.end()
        mapView.onDetach()
    }


    fun setUpMap(){
        mapView = MapView(this)
        mapView.setTag("mapview")
        mapView.visibility = View.GONE
        setLayoutParams(mapView)
        rootFrame.addView(mapView)

        mapView.setOnGenericMotionListener(object : OnGenericMotionListener{
            override fun onGenericMotion(v: View?, event: MotionEvent?): Boolean {
                if (0 != event!!.source and InputDevice.SOURCE_CLASS_POINTER) {
                    when (event!!.action) {
                        MotionEvent.ACTION_SCROLL -> {
                            if (event!!.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) mapView.controller
                                .zoomOut() else {
                                //this part just centers the map on the current mouse location before the zoom action occurs
                                val iGeoPoint: IGeoPoint = mapView.projection.fromPixels(
                                    event!!.x.toInt(),
                                    event!!.y.toInt()
                                )
                                mapView.controller.animateTo(iGeoPoint)
                                mapView.controller.zoomIn()
                            }
                            return true
                        }
                    }
                }
                return false
            }
        })

        val context: Context = this
        val dm = context.resources.displayMetrics

        mPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)


        //My Location
        //note you have handle the permissions yourself, the overlay did not do it for you


        //My Location
        //note you have handle the permissions yourself, the overlay did not do it for you
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        mLocationOverlay!!.enableMyLocation()
        mapView.overlays.add(this.mLocationOverlay)


        //Mini map


        //Mini map
        mMinimapOverlay = MinimapOverlay(context, mapView.tileRequestCompleteHandler)
        mMinimapOverlay!!.width = dm.widthPixels / 5
        mMinimapOverlay!!.height = dm.heightPixels / 5
        mapView.overlays.add(this.mMinimapOverlay)


        //Copyright overlay


        //Copyright overlay
        mCopyrightOverlay = CopyrightOverlay(context)
        //i hate this very much, but it seems as if certain versions of android and/or
        //device types handle screen offsets differently
        //i hate this very much, but it seems as if certain versions of android and/or
        //device types handle screen offsets differently
        mapView.overlays.add(this.mCopyrightOverlay)


        //On screen compass


        //On screen compass
        mCompassOverlay = CompassOverlay(
            context, InternalCompassOrientationProvider(context),
            mapView
        )
        mCompassOverlay!!.enableCompass()
        mapView.overlays.add(this.mCompassOverlay)


        //map scale


        //map scale
        mScaleBarOverlay = ScaleBarOverlay(mapView)
        mScaleBarOverlay!!.setCentred(true)
        mScaleBarOverlay!!.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(this.mScaleBarOverlay)


        //support for map rotation


        //support for map rotation
        mRotationGestureOverlay = RotationGestureOverlay(mapView)
        mRotationGestureOverlay!!.isEnabled = true
        mapView.overlays.add(this.mRotationGestureOverlay)


        //needed for pinch zooms


        //needed for pinch zooms
        mapView.setMultiTouchControls(true)

        //scales tiles to the current screen's DPI, helps with readability of labels

        //scales tiles to the current screen's DPI, helps with readability of labels
        mapView.isTilesScaledToDpi = true

        //the rest of this is restoring the last map location the user looked at

        //the rest of this is restoring the last map location the user looked at
        mPrefs?.let { prefs->
            val zoomLevel: Float = prefs.getFloat(PREFS_ZOOM_LEVEL_DOUBLE, 1F)
            mapView.controller.setZoom(zoomLevel.toDouble())
            val orientation: Float = prefs.getFloat(PREFS_ORIENTATION, 0F)
            mapView.setMapOrientation(orientation, false)
            val latitudeString: String? = prefs.getString(PREFS_LATITUDE_STRING, "1.0")
            val longitudeString: String? = prefs.getString(PREFS_LONGITUDE_STRING, "1.0")
            val latitude = java.lang.Double.valueOf(latitudeString)
            val longitude = java.lang.Double.valueOf(longitudeString)
            mapView.setExpectedCenter(GeoPoint(latitude, longitude))
        }
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

    fun setLayoutParams(view : View){
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = layoutParams
    }
}