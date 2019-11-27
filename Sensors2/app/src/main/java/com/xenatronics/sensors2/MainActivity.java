package com.xenatronics.sensors2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    com.xenatronics.sensors2.WidgetGauge gaugeTemp;
    com.xenatronics.sensors2.WidgetGauge gaugeHum;
    TextView txtTemp;
    TextView txtHum;
    Timer timer;
    SensorTimerTask tasktimer;

    private ReceiveMessageTask receiveTask=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gaugeTemp =(WidgetGauge)findViewById(R.id.gauge1);
        gaugeHum =(WidgetGauge)findViewById(R.id.gauge2);
        gaugeHum.setValue(44);
        gaugeTemp.setValue(29);

        txtTemp=(TextView)findViewById(R.id.txtTemp);
        txtHum=(TextView)findViewById(R.id.txtHumidity);
        txtTemp.setText("__ °C");
        txtHum.setText("__ %");

        if(timer!=null)
            timer.cancel();

        timer=new Timer();
        tasktimer=new SensorTimerTask();
        timer.schedule(tasktimer,1000,5000);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //receiveTask=new ReceiveMessageTask();
                //receiveTask.execute();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
            }
        });
    }

    private class ReceiveMessageTask extends AsyncTask<Void, Void, Boolean> {

        String strLine;
        String temp;
        String hum;

        public ReceiveMessageTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Socket client = new Socket("192.168.1.20", 1155);// connect to the server
                //////  RECEPTION D'UN MESSAGE ///////
                Log.v("Connexion", "Reception");
                //Thread.sleep(2000);
                strLine="";
                BufferedReader in_bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (strLine!=null) {
                    try {
                        strLine = in_bufferedReader.readLine();

                        if (strLine!=null )
                        {
                            if (!strLine.contains(";"))
                                return false;

                            StringTokenizer tokens = new StringTokenizer(strLine, ";");
                            temp = tokens.nextToken();// this will contain "Fruit"
                            hum = tokens.nextToken();// this will contain " they taste good"
                            Log.v("Connexion", strLine);
                            return  true;
                        }
                        else
                        {
                            return false;
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                        client.close();
                        return  false;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
            finally {
                return true;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            receiveTask = null;

            if (success) {
                // finish();
                if (strLine!=null)
                    if (temp!=null) {
                        StringTokenizer tokens = new StringTokenizer(temp, ".");
                        String entier = tokens.nextToken();
                        txtTemp.setText(entier+" °C");
                        gaugeTemp.setValue(Integer.parseInt(entier));
                        StringTokenizer item = new StringTokenizer(hum, ".");
                        entier = item.nextToken();
                        txtHum.setText(entier+" %");
                        gaugeHum.setValue(Integer.parseInt(entier));
                    }

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            receiveTask = null;
        }
    }

    private class SensorTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            receiveTask=new ReceiveMessageTask();
            receiveTask.execute();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
