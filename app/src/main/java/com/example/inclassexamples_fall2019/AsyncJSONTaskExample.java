package com.example.inclassexamples_fall2019;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;


public class AsyncJSONTaskExample extends AppCompatActivity {
    private Context thisApp ;
    private static final String TOAST = "TOAST";
    private static final String SNACK = "SNACK";
    private static final String ALERT = "ALERT";
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_layout);
        search = (EditText)findViewById(R.id.searchBar);
        thisApp = this;

        Button runToast = (Button)findViewById(R.id.runToast);
        runToast.setOnClickListener( click -> {
            //Uncomment this to make the GUI freeze:
            //try{Thread.sleep(10000);} catch(Exception e){ Log.i("ASYNC FROZE", "Frozen"); }


            String query = Uri.encode(search.getText().toString() ); //Have to encode strings to send in URL
            this.runQuery(query, TOAST);
        });

        Button runSnackbar = (Button)findViewById(R.id.runSnackbar);
        runSnackbar.setOnClickListener( cl -> {
            String query = Uri.encode(search.getText().toString() ); //Have to encode strings to send in URL
            this.runQuery(query, SNACK);
        });

        Button runAlert = (Button)findViewById(R.id.runAlert);
        runAlert.setOnClickListener( cl -> {
            String query = Uri.encode(search.getText().toString() ); //Have to encode strings to send in URL
            this.runQuery(query, ALERT);
        });
    }

    private void runQuery(String query, String responseType)
    {
        MyNetworkQuery theQuery = new MyNetworkQuery(responseType);
        theQuery.execute( );
    }

            //                                      Type1, Type2   Type3
    private class MyNetworkQuery extends AsyncTask<String, String, String>{
        String responseType;
        @Override                       //Type 1
        protected String doInBackground(String ... strings) {

            String ret = null;
            String queryURL = "http://torunski.ca/CST2335_XML.xml";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:

            search.setText("Finished background thread");
        }

        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:
            switch (responseType)
            {
                case TOAST:
                    Toast.makeText(thisApp, values[0] + " is " + values[1], Toast.LENGTH_LONG).show();
                    break;
                case SNACK:
                    Snackbar.make(search, values[0] + " is " + values[1], Snackbar.LENGTH_LONG).show();
               break;
                case ALERT:
                    AlertDialog.Builder b = new AlertDialog.Builder(thisApp);
                    b.setTitle("Message:")
                            .setMessage(values[0] + " is " + values[1])
                            .setPositiveButton("I'm positive", (clk, btn) -> { /* do this when clicked */ })
                            .setNegativeButton("I'm negative", (clk, btn) -> { /* do this when clicked */ })
                            .create()
                            .show();
            }
        }

        public MyNetworkQuery(String s)
        {
            responseType = s;
        }
    }
}
