package com.example.toshiba.airbnb.Profile.BecomeAHost.BasicQuestions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.toshiba.airbnb.Profile.BecomeAHost.BecomeAHostActivity;
import com.example.toshiba.airbnb.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Owner on 2017-07-07.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    public static final String BASIC_QUESTIONS_COMPLETED = "BASIC_QUESTIONS_COMPLETED";
    SupportMapFragment mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBar basicProgressBar = (ProgressBar) getActivity().findViewById(R.id.basicProgressBar);
        basicProgressBar.setProgress(100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Location is disabled, turn it on in your settings,", Toast.LENGTH_LONG).show();
//        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);

        view.findViewById(R.id.bNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.progressFragment, new AmenitiesItemFragment()).addToBackStack(null).commit();
            }
        });

    }

    //Google Map interface
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("GoogleMaps", "OnMapReady");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LocationFilterAdapter.LOCATION_SP, Context.MODE_PRIVATE);

        Double LAT = Double.parseDouble(sharedPreferences.getString(LocationFilterAdapter.LAT,"0.000000"));
        Double LNG = Double.parseDouble(sharedPreferences.getString(LocationFilterAdapter.LNG,"0.000000"));
        LatLng latLng = new LatLng(LAT, LNG);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(11)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(markerOptions);
        googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(300)
                .strokeWidth(0f)
                .fillColor(0x550000FF));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //need to remove child fragment when destroyed or else "error loading xml file would occur"
        if (mapFragment != null)
        {
            //get rid of child when activity is already saved
            getChildFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
        }

        mapFragment = null;
    }

}
