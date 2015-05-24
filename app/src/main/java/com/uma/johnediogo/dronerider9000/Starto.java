package com.uma.johnediogo.dronerider9000;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Starto extends ActionBarActivity {
    public final static String EXTRA_SERVERIP = "com.johnecro.DroneRider9000.SERVERIP";
    String serverIP = "";
    public final static String EXTRA_PORT = "com.johnecro.DroneRider9000.SERVERPORT";
    int serverPort = 3001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starto);
        Intent i = getIntent();
        serverIP = i.getStringExtra(Starto.EXTRA_SERVERIP);
        serverPort = i.getIntExtra(Starto.EXTRA_PORT, 3001);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_starto, menu);
        return true;
    }

    public void goToPilot(View view){
        Intent intent = new Intent(this, pilot2.class);
        intent.putExtra(EXTRA_SERVERIP, serverIP);
        intent.putExtra(EXTRA_PORT, serverPort);
        startActivity(intent);
    }

    public void goToServer(View view){
        Intent intent = new Intent(this, server.class);
        startActivity(intent);
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
