package com.uma.johnediogo.dronerider9000;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.uma.johnediogo.dronerider9000.mjpeg.MjpegInputStream;
import com.uma.johnediogo.dronerider9000.mjpeg.MjpegView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class pilot2 extends ActionBarActivity{
    private static final String TAG = "Pilot";

    Button liftButton;
    Socket socket = null;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    String serverIPAddress = "";
    int serverPort = 3001;
    byte[] response = new byte[256];
    String state;


    MjpegView videoView;
    ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        serverIPAddress = i.getStringExtra(Starto.EXTRA_SERVERIP);
        serverPort = i.getIntExtra(Starto.EXTRA_PORT, 3001);
        String videoURL = "http://"+ serverIPAddress +":3002/nodecopter.mjpeg";
        MjpegInputStream mjp = new MjpegInputStream(null);
        mjp = mjp.read(videoURL);
        Log.i(TAG, "stream created");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        videoView = new MjpegView(this);
        setContentView(R.layout.activity_pilot2);

        RelativeLayout l = (RelativeLayout) findViewById(R.id.vid);
        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.relLay);
        try {
            l.addView(videoView);
            videoView.setSource(mjp);
            videoView.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            videoView.startPlayback();
            videoView.showFps(true);
        }catch (Exception ecp){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Video server isn't working!");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }


        liftButton = (Button) findViewById(R.id.liftButton);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    socket = new Socket(serverIPAddress, serverPort);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

        liftButton.setOnClickListener(liftHandler);
    }

    View.OnClickListener liftHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if(liftButton.getText()=="LAND!") {
                liftButton.setText("LIFT OFF!");
                new CommandWorkerThread("[\"stop\",[],1]\n").start();
                new CommandWorkerThread("[\"land\",[],1]\n").start();
            } else {
                liftButton.setText("LAND!");
                new CommandWorkerThread("[\"takeoff\",[],1]\n").start();
            }

        }
    };

    public void onLeftClick(View v){
        if(state !="left") {
            new CommandWorkerThread("[\"left\",[0.0],2]\n").start();
            state = "left";
        }else {
            new CommandWorkerThread("[\"left\",[0.2],2]\n").start();
            state = "left";
        }
    }


    public void onRightClick(View v){
        if(state !="right"){
            new CommandWorkerThread("[\"right\",[0.0],2]\n").start();
            state = "right";
        }else {
            new CommandWorkerThread("[\"right\",[0.2],2]\n").start();
            state = "right";
        }
    }

    public void onCalibrateDroneClick(View v){
        new CommandWorkerThread("[\"calibrate\",[],1]\n").start();
    }

    public void onUpClick(View v){
        if(state !="up") {
            new CommandWorkerThread("[\"up\",[0.0],2]\n").start();
            state = "up";
        }else {
            new CommandWorkerThread("[\"up\",[0.2],2]\n").start();
            state = "up";
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            //up arrow - front
            case 19:
                new CommandWorkerThread("[\"front\",[0.2],2]\n").start();
                break;
            //down arrow - back
            case 20:
                new CommandWorkerThread("[\"back\",[0.2],2]\n").start();
                break;
            //left arrow - left
            case 21:
                new CommandWorkerThread("[\"left\",[0.2],2]\n").start();
                break;
            //right arow - right
            case 22:
                new CommandWorkerThread("[\"right\",[0.2],2]\n").start();
                break;
            //X - up
            case 96:
                    new CommandWorkerThread("[\"up\",[0.2],2]\n").start();
                break;
            //Delta - down
            case 99:
                new CommandWorkerThread("[\"animateLeds\",[blinkGreenRed,5,2],2]\n").start();
                break;
            //Delta - down
            case 100:
                new CommandWorkerThread("[\"up\",[-0.2],2]\n").start();
                break;
            //L1 - turn counter clockwise
            case 102:
                new CommandWorkerThread("[\"clockwise\",[-0.2],2]\n").start();
                break;
            //R1 - turn clockwise
            case 103:
                new CommandWorkerThread("[\"clockwise\",[0.2],1]\n").start();
                break;
                //L2 - land
            case 104:
                new CommandWorkerThread("[\"stop\",[],1]\n").start();
                new CommandWorkerThread("[\"land\",[],1]\n").start();
                break;
            // R2 - takeoff
            case 105:
                new CommandWorkerThread("[\"takeoff\",[],1]\n").start();
                break;

            default:
                break;
        }
        return true;
    }

    public void onDownClick(View v){
        if(state !="down") {
            new CommandWorkerThread("[\"up\",[0.0],1]\n").start();
            state = "down";
        }else {
            new CommandWorkerThread("[\"up\",[-0.2],1]\n").start();
            state = "down";
        }
    }

    public void onFrontClick(View v){
        if(state !="front") {
            new CommandWorkerThread("[\"front\",[0.0],2]\n").start();
            state = "front";
        }else {
            new CommandWorkerThread("[\"front\",[0.2],2]\n").start();
            state = "front";
        }

    }

    public void onBackClick(View v){
        if(state !="back") {
            new CommandWorkerThread("[\"back\",[0.0],2]\n").start();
            state = "back";
        }else {
            new CommandWorkerThread("[\"back\",[0.2],2]\n").start();
            state = "back";
        }

    }

    public void onStopClick(View v){
        new CommandWorkerThread("[\"stop\",[],1]\n").start();
    }

    public void onLRotateClick(View v){
        if(state !="clockwise") {
            new CommandWorkerThread("[\"back\",[0.0],1]\n").start();
            state = "clockwise";
        }else {
        new CommandWorkerThread("[\"clockwise\",[0.2],1]\n").start();
            state = "clockwise";
        }
    }

    public void onRRotateClick(View v){
        if(state !="counterClockwise") {
            new CommandWorkerThread("[\"counterClockwise\",[0.0],1]\n").start();
            state = "counterClockwise";
        }else {
            new CommandWorkerThread("[\"counterClockwise\",[0.2],1]\n").start();
            state = "counterClockwise";
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pilot2, menu);
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

    private class CommandWorkerThread extends Thread{

        private String _command="";

        public CommandWorkerThread(String command){
            _command = command;
        }

        @Override
        public void run(){
            try {

                Log.i(TAG, "sending request");
                dataOutputStream.writeBytes(_command);
                dataInputStream.readFully(response);
                dataOutputStream.flush();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }
}
