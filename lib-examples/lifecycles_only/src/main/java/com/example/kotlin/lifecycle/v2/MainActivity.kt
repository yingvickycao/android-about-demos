package com.example.kotlin.lifecycle.v2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.lifecycle.R

/**
 * Update the count to screen
 */
class MainActivity : AppCompatActivity() {
    private lateinit var locationListener: LocationListener
    private lateinit var positionView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        positionView = findViewById(R.id.positionView)

        locationListener = LocationListener(this, lifecycle) { location: Location ->
            // Update UI
            location.position(object : Location.LocationCallback {
                override fun update(position: Int) {
                    runOnUiThread() {
                        positionView.text = position.toString()
                    }
                }
            })
        }

        // check user status is enabled
        locationListener.enabled()
    }
}
