package ro.pub.cs.systems.eim.practicaltest02v9

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ro.pub.cs.systems.eim.practicaltest02v9.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val goToBucharestButton = Button(this).apply {
            text = "Mergi la București"
            setOnClickListener {
                val bucharest = LatLng(44.4268, 26.1025)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, 15f))
            }
        }

        addContentView(
            goToBucharestButton,
            android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 50
                leftMargin = 50
            }
        )
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Marker în București
        val bucharest = LatLng(44.4268, 26.1025)
        googleMap.addMarker(
            MarkerOptions().position(bucharest).title("București, România")
        )

        // Zoom inițial pe Ghelmegioaia
        val ghelmegioaia = LatLng(44.615604, 22.831837)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 10f))
    }
}
