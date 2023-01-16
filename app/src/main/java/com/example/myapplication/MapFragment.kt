package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import com.esri.arcgisruntime.symbology.TextSymbol
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment
import com.example.myapplication.util.GlobalBox
import com.google.android.material.bottomnavigation.BottomNavigationView


class MapFragment : Fragment() {
    private lateinit var mMapView: MapView
    private lateinit var graphicLayer: GraphicsOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ArcGISRuntimeEnvironment.setLicense(resources.getString(R.string.arcgis_license_key))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = view.findViewById<MapView>(R.id.mapView);
        val latitude = 13.736717
        val longitude = 100.523186
        val levelOfDetail = 11
        val map = ArcGISMap(Basemap.Type.DARK_GRAY_CANVAS_VECTOR, latitude, longitude, levelOfDetail)
        mMapView.map = map
        mMapView.isAttributionTextVisible = false
        addGraphics()

        if (GlobalBox.longitude != 0.00 && GlobalBox.longitude != 0.00) {
            val location: Point = genFoodPoint(GlobalBox.longitude, GlobalBox.latitude)
            mMapView.setViewpointCenterAsync(location, 2200.0)
        }

    }

    private fun genFoodPoint(x: Double, y: Double): Point {
        // create a point geometry with a location and spatial reference
        // Point(latitude, longitude, spatial reference)

        val point = Point(x, y, SpatialReferences.getWgs84())
        // code for  get image in drawable folder
        val pictureMarkerSymbol = PictureMarkerSymbol.createAsync(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.marker
            ) as BitmapDrawable
        ).get()

        // set width, height, z from ground
        pictureMarkerSymbol.height = 52f
        pictureMarkerSymbol.width = 52f
        pictureMarkerSymbol.offsetY = 10f

        // create a graphic with the point geometry and symbol
        val pointGraphic = Graphic(point, pictureMarkerSymbol)

        // add the point graphic to the graphics overlay
        graphicLayer.graphics.add(pointGraphic)

        return point
    }

    private fun addGraphics() {
        // create a graphics overlay and add it to the map view
        graphicLayer = GraphicsOverlay()
        mMapView.graphicsOverlays.add(graphicLayer)
    }

    override fun onPause() {
        super.onPause()
        mMapView.pause()
    }

    override fun onResume() {
        super.onResume()
        mMapView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.dispose()
    }

}

