package com.learning.dtwallace.restaurantroulette;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    List<Place> Restaurants = new ArrayList<>();
    TextView tv_winner;
    Button chooseButton;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    GoogleApiClient mGoogleApiClient;
    String TAG = "MainActivity";
    final String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
            "location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyB_EyW-vdFNaB8pBtFATDWg5NPqZWKoyPg";
    private int MY_PERMISSIONS_ACCESS_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign button id
        chooseButton = findViewById(R.id.button_choose);

        tv_winner = findViewById(R.id.tv_winner);

        /**
         * Before proceeding, make sure location permission is granted
         */
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_LOCATION);
        }

        /**
         * Following code pulled from Google Places documentation
         */
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // TODO: Start using the Places API.

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
                placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                        int count;
                        if (likelyPlaces.getCount() < 25) {
                            count = likelyPlaces.getCount();
                        } else {
                            count = 25;
                        }

                        int i = 0;
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            Restaurants.add(placeLikelihood.getPlace());
                            i++;

                            Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        Place winnerPlace = (Restaurants.get(new Random().nextInt(Restaurants.size())));
                        tv_winner.setText(winnerPlace.getName());
                        Restaurants.clear();

                        likelyPlaces.release();
                    }
                });
            }
        });
    }

    /**
     * Toast the user if connection to API fails
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection failed", Toast.LENGTH_SHORT).show();
    }
}

