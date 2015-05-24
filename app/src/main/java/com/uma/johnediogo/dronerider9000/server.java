package com.uma.johnediogo.dronerider9000;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class server extends ActionBarActivity {
    public final static String EXTRA_SERVERIP = "com.johnecro.DroneRider9000.SERVERIP";
    String serverIP = "";
    public final static String EXTRA_PORT = "com.johnecro.DroneRider9000.SERVERPORT";
    int serverPort = 3001;
    EditText ip, port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

    }

    public void confirm(View view){
        ip =(EditText) findViewById(R.id.ipET);
        port = (EditText) findViewById(R.id.portET);
        serverIP = ip.getText().toString();
        int myNum = 0;

        try {
            myNum = Integer.parseInt(port.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        serverPort = myNum;
        Intent intent = new Intent(this, pilot2.class);
        intent.putExtra(EXTRA_SERVERIP, serverIP);
        intent.putExtra(EXTRA_PORT, serverPort);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
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
