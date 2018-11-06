/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This shows how to add a ground overlay to a map.
 */
public class Floor3 extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnGroundOverlayClickListener,
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMarkerClickListener
        {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private static final int TRANSPARENCY_MAX = 100;

    private static final LatLng nitkcse = new LatLng(13.012745, 74.791187);

    //private static final LatLng NEAR_NEWARK =
            //new LatLng(NEWARK.latitude - 0.001, NEWARK.longitude - 0.025);

    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();

    private GroundOverlay mGroundOverlay;

    private GroundOverlay mGroundOverlayRotated;

    private SeekBar mTransparencyBar;

    private int mCurrentEntry = 0;


    //Markers






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ground_overlay_demo);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Register a listener to respond to clicks on GroundOverlays.

        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        map.setOnGroundOverlayClickListener(this);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nitkcse, 20));
        addMarkersToMap();
        mMap.setOnMarkerClickListener(this);

        mImages.clear();
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.floor3_map));
        //mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_prudential_sunny));

        // Add a small, rotated overlay that is clickable by default
        // (set by the initial state of the checkbox.)
        LatLngBounds nitkcse_bounds = new LatLngBounds(
                new LatLng(13.012405, 74.790877),
                new LatLng(13.013112, 74.791489)
                );      // North east corner

        mGroundOverlayRotated = map.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.floor3_map)).anchor(0, 1)
                .positionFromBounds(nitkcse_bounds)
                .clickable(true));

        // Add a large overlay at Newark on top of the smaller overlay.
        /*mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
                .image(mImages.get(mCurrentEntry)).anchor(0, 1)
                .position(NEWARK, 8600f, 6500f));*/


        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Google Map with ground overlay.");
    }









    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        // Toggle transparency value between 0.0f and 0.5f. Initial default value is 0.0f.
        mGroundOverlayRotated.setTransparency(0.5f - mGroundOverlayRotated.getTransparency());
    }

    /**
     * Toggles the clickability of the smaller, rotated overlay based on the state of the View that
     * triggered this call.
     * This callback is defined on the CheckBox in the layout for this Activity.
     */


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private String jsonData(){

        InputStream is = getResources().openRawResource(R.raw.floor4);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()){

            builder.append(scanner.nextLine());
        }
        return builder.toString();
    }

            public LatLng markers[] = new LatLng[]{
            new LatLng(13.012622739660666, 74.79105703076675),
            new LatLng(13.012557406372515, 74.79109659334972),
            new LatLng(13.012603793008875,74.79118041238144),
            new LatLng(13.012619890857646, 74.79124644253704),
            new LatLng(13.012616950860007, 74.79134970758412),
            //new LatLng(13.012718217424984, 74.79130276892636),
            new LatLng(13.01294548585153, 74.79110195776775),
            new LatLng(13.012916085912263,74.79120187005356),
            new LatLng(13.012908899259907, 74.79126959583118),
            new LatLng(13.012907592595832, 74.79135073265388),
            //new LatLng(13.012824710606754, 74.79131115082953),
            new LatLng(13.012847486040423, 74.7910838528569),
            new LatLng(13.012756586195273,74.79138865344066),
            new LatLng(13.012758546192591, 74.79131019882698)};



    private void addMarkersToMap() {


        for (int i = 0; i < 12; i++) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(markers[i])
                    .title("Marker " + i)
                    .anchor(0.5f,0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.square_logo)));
            marker.setTag(i);

        }
    }
            private Polyline mLastPolyline = null;
            public boolean onMarkerClick(final Marker marker) {
                int i = (Integer) marker.getTag();
                if(mLastPolyline != null)
                    mLastPolyline.remove();
                if(i==0 || i==2 || i==3)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[3],markers[11],markers[10]));
                if(i==1)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[2],markers[3],markers[11],markers[10]));
                if(i==4)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[10]));
                if(i==9 || i==6 || i==7)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[7],markers[11],markers[10]));
                if(i==5)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[6],markers[7],markers[11],markers[10]));
                if(i==8 || i==11)
                    mLastPolyline = mMap.addPolyline(new PolylineOptions()
                            .color(Color.RED)
                            .width(7)
                            .add(marker.getPosition(),markers[10]));

                return false;
            }

}
