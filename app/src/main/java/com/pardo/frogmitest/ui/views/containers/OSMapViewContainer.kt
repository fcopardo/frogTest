package com.pardo.frogmitest.ui.views.containers

import android.content.Context
import android.content.SharedPreferences
import android.view.InputDevice
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.extensions.wrapWithFrame
import org.osmdroid.api.IGeoPoint
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

class OSMapViewContainer(var mapView : MapView, parent : FrameLayout) {
    private val PREFS_NAME = "org.andnav.osm.prefs"
    private val PREFS_TILE_SOURCE = "tilesource"
    private val PREFS_LATITUDE_STRING = "latitudeString"
    private val PREFS_LONGITUDE_STRING = "longitudeString"
    private val PREFS_ORIENTATION = "orientation"
    private val PREFS_ZOOM_LEVEL_DOUBLE = "zoomLevelDouble"

    private val MENU_ABOUT = Menu.FIRST + 1
    private val MENU_LAST_ID = MENU_ABOUT + 1 // Always set to last unused id


    // ===========================================================
    // Needed for the mapView
    // ===========================================================
    private var mPrefs: SharedPreferences? = null
    private var mLocationOverlay: MyLocationNewOverlay? = null
    private var mCompassOverlay: CompassOverlay? = null
    private var mMinimapOverlay: MinimapOverlay? = null
    private var mScaleBarOverlay: ScaleBarOverlay? = null
    private var mRotationGestureOverlay: RotationGestureOverlay? = null
    private var mCopyrightOverlay: CopyrightOverlay? = null

    var visibility = View.GONE
        set(value){
            if(value == View.GONE || value == View.VISIBLE || value == View.INVISIBLE){
                field = value
                mapView.visibility = value
            }
        }

    init {
        setUpMap(parent)
    }

    private fun setUpMap(parent : FrameLayout){
        mapView.setTag("mapview")
        mapView.visibility = visibility
        mapView.wrapWithFrame(parent)

        mapView.setOnGenericMotionListener(object : View.OnGenericMotionListener {
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

        val context: Context = mapView.context
        val dm = context.resources.displayMetrics

        mPrefs = context.getSharedPreferences(PREFS_NAME, ComponentActivity.MODE_PRIVATE)


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

    fun onResume(){
        mapView.onResume()
    }

    fun onPause(){
        mapView.onPause()
    }

    fun onDetach(){
        mapView.onDetach()
    }


}