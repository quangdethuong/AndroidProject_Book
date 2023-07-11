package com.codingstuff.shoeapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codingstuff.shoeapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map_Fragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_map,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.my_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng canTho = new LatLng(10.01193, 105.73156);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(canTho, 12));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(canTho)
                        .title("FPT University")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .anchor(0.5f, 1.0f); // set anchor point to bottom center of marker

                googleMap.addMarker(markerOptions);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(latLng.latitude + "KG" + latLng.longitude)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .anchor(0.5f, 1.0f); // set anchor point to bottom center of marker

                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.addMarker(markerOptions);
                    }
                });

            }
        });

        return view;
    }
}

