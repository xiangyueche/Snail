package com.example.westsnow.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PersonalPage extends ActionBarActivity {

    private String username;

    public void homepage(View view){
        Intent in = new Intent(getApplicationContext(),
                HomePage.class);
        // sending pid to next activity
        in.putExtra("username", username);
        // starting new activity
        startActivity(in);
    }

    public void sendMoment(View view){
        Intent in = new Intent(getApplicationContext(),
                SendMoment.class);
        // sending pid to next activity
        in.putExtra("username", username);
        // starting new activity
        startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_page, menu);
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
