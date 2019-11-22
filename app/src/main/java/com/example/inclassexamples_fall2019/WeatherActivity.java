package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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


public class WeatherActivity extends AppCompatActivity {

    String temp, min, max;
    TextView tempText, minText, maxText;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        tempText = findViewById(R.id.tempText);
        minText = findViewById(R.id.minText);
        maxText = findViewById(R.id.maxText);
        weatherIcon = findViewById(R.id.weatherImage);

        MyNetworkQuery query = new MyNetworkQuery();
        query.execute();
    }



    //                                      Type1, Type2   Type3
    private class MyNetworkQuery extends AsyncTask<String, String, String> {

        Bitmap image;
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

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
                            if(tagName.equals("temperature"))
                            {
                                temp = xpp.getAttributeValue(null, "value");
                                min = xpp.getAttributeValue(null, "min");
                                max = xpp.getAttributeValue(null, "max");
                            }
                            if(tagName.equals("weather"))
                            {
                                String icon = xpp.getAttributeValue(null, "icon");
                                String iconURL = "http://openweathermap.org/img/w/" + icon + ".png";
                                image = null;
                                url = new URL(iconURL);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.connect();
                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    image = BitmapFactory.decodeStream(connection.getInputStream());
                                }
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
            tempText.setText("Current temp:"+temp);
            minText.setText("Min temp:" + min);
            maxText.setText("Max temp:" + max);
            weatherIcon.setImageBitmap(image);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:

        }
    }
}
