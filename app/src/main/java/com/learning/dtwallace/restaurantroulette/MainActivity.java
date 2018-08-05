package com.learning.dtwallace.restaurantroulette;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<String> Restaurants = new ArrayList<>();
    TextView tv_winner;
    Button chooseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseButton = findViewById(R.id.button_choose);

        String SmellyCat = new String("Smelly Cat");
        String Sabor = new String("Sabor");
        String Amelies = new String("Amelie's");
        String Cabo = new String("Cabo");
        Restaurants.add(SmellyCat);
        Restaurants.add(Sabor);
        Restaurants.add(Amelies);
        Restaurants.add(Cabo);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String winner = Restaurants.get(new Random().nextInt(Restaurants.size()));
                //winner = randomItem.toString();

                tv_winner = findViewById(R.id.tv_winner);
                tv_winner.setText(winner);

            }
        });
    }
}
