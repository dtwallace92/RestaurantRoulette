package com.learning.dtwallace.restaurantroulette;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<String> Restaurants = new ArrayList<>();
    TextView tv_winner;
    Button chooseButton;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    GoogleApiClient mGoogleApiClient;
    String TAG = "MainActivity";
    private int MY_PERMISSIONS_ACCESS_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign button id
        chooseButton = findViewById(R.id.button_choose);


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
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // TODO: Start using the Places API.

        // Choose restaurant when user clicks the button
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place> options = getPlaceData();
                if (!options.isEmpty()) {
                    chooseWinner(options);
                } else {
                    Toast.makeText(MainActivity.this, "List is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public int chooseWinner(List<Place> choices) {
        //Pick random restaurant from list of restaurants
        int position = choices.indexOf(new Random().nextInt(choices.size()));
        Place winner = choices.get(position);

        //Set winning restaurant to the appropriate text view
        tv_winner = findViewById(R.id.tv_winner);
        tv_winner.setText(winner.getName());
        System.out.println(position);
        return position;
    }

    @SuppressLint("MissingPermission")
    private List<Place> getPlaceData() {
        final List<Place> placesList = new ArrayList<>();
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.
                getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                Log.d(TAG, "current location places info");

                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    placesList.add(placeLikelihood.getPlace().freeze());
                }

                likelyPlaces.release();

            }
        });
        return placesList;

    }

    public void findPlaces() {

        List<Place> foundPlaces =
    }

}
