package com.george.eleftheriou.carplateidentifier.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.george.eleftheriou.carplateidentifier.Models.RegionModel;
import com.george.eleftheriou.carplateidentifier.R;
import com.george.eleftheriou.carplateidentifier.helpers.JSONHelper;
import com.george.eleftheriou.carplateidentifier.helpers.LogHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class DetailViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map = null;
    private LinearLayout progressIndicator = null;
    private TextView coordinatesTextView = null;
    private TextView regionTextView = null;
    private TextView countryTextView = null;

    private RegionModel model = null;
    private LatLng location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressIndicator = findViewById(R.id.detailViewProgressBar);
        coordinatesTextView = findViewById(R.id.cordinatesText);
        regionTextView = findViewById(R.id.regionText);
        countryTextView = findViewById(R.id.countryText);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Intent intent = getIntent();
        model = JSONHelper.fromJSON(intent.getStringExtra("regionJSON"), RegionModel.class);

        if (model != null) {
            new UpdateTask().execute();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            final Geocoder geocoder = new Geocoder(DetailViewActivity.this, Locale.getDefault());

            try {
                final List<Address> addresses = geocoder.getFromLocationName(model.getName(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                }
            } catch (Exception e) {
                LogHelper.logStackTrace(e);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressIndicator != null) {
                progressIndicator.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (model != null && location != null) {
                // UPDATE VALUES
                if (coordinatesTextView != null) {
                    coordinatesTextView.setText(String.format("%f, %f", location.latitude, location.longitude));
                }

                if (regionTextView != null) {
                    regionTextView.setText(model.getName());
                }

                if (countryTextView != null) {
                    countryTextView.setText(model.getCountry().getName());
                }

                // SET UP MAP
                map.addMarker(new MarkerOptions().position(location).title(model.getName()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
            }

            if (progressIndicator != null) {
                progressIndicator.setVisibility(View.GONE);
            }
        }
    }


}
