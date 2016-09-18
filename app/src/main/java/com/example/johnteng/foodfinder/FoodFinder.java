package com.example.johnteng.foodfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FoodFinder extends AppCompatActivity {
    public static String json = "";
    public static TextView t;
    public static boolean authenticate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        final jsonParser jp = new jsonParser();

        setSupportActionBar(toolbar);
        Button b = new Button(this);
        b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        b.setText("Press me to start server");
        t = new TextView(this);
        t.setText("HELLO UNIVERSE");
        ll.addView(b);

        ll.addView(t);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAPI a = new readAPI(jp);
                a.stopThread();
                a.start();
                //t.setText(a.JSONresponse);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
