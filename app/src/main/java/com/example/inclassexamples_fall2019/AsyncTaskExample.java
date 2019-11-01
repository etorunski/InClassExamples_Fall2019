package com.example.inclassexamples_fall2019;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;


public class AsyncTaskExample extends AppCompatActivity {
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
            this.runQuery(query, SNACK);
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
        theQuery.execute(query);
    }

            //                                      Type1, Type2   Type3
    private class MyNetworkQuery extends AsyncTask<String, String, String>{
        String responseType;
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://torunski.ca/CST2335_XML.xml";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("AMessage"))
                            {
                                String message = xpp.getAttributeValue(null, "message"); //What is the String associated with message?
                                publishProgress("Message", message);
                            }
                            else if(tagName.equals("Weather"))
                            {
                                String outlook = xpp.getAttributeValue(null, "outlook");
                                publishProgress("Outlook", outlook);

                                String windy = xpp.getAttributeValue(null, "windy");
                                publishProgress("Windy", windy);
                            }
                            else if(tagName.equals("Temperature"))
                            {
                                xpp.next(); //There is a text right after the opening of <Temperature> so move the pointer next
                                String text = xpp.getText();
                                publishProgress("Temperature", text);
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:

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
