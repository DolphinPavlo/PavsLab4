package com.example.lab3;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progressBar;

    TextView currentTempTextView;
    TextView minTempTextView;
    TextView maxTempTextView;
    TextView uvTextView;
    ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityweather);

        progressBar = findViewById(R.id.progress_horizontal);

        currentTempTextView = findViewById(R.id.currentTemp);
        minTempTextView = findViewById(R.id.minTemp);
        maxTempTextView = findViewById(R.id.maxTemp);
        uvTextView = findViewById(R.id.uvRating);
        weatherImageView = findViewById(R.id.currentWeatherImage);


        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();

    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private double uv;
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap image = null;
        private String TAG = "Forcast Query";

        private String iconName;
        String responseType;

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
                xpp.setInput(inStream, "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    switch (EVENT_TYPE) {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if (tagName.equals("temperature")) {
                                currentTemp = xpp.getAttributeValue(null, "value"); //What is the String associated with message?
                                //publishProgress(25, Integer.parseInt(currentTemp));
                                publishProgress(25);

                                minTemp = xpp.getAttributeValue(null, "min");
                                //publishProgress(50, Integer.parseInt(minTemp));
                                publishProgress(50);

                                maxTemp = xpp.getAttributeValue(null, "max");
                                //publishProgress(75, Integer.parseInt(maxTemp));
                                publishProgress(75);
                            }
                            if (tagName.equals("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");

                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }


                URL imageURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");

                HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                //connection.setDoInput(true);
                connection.connect();
                //image = BitmapFactory.decodeStream(connection.getInputStream());
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }


                if (fileExistence(iconName + ".png")) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(iconName + ".png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bm = BitmapFactory.decodeStream(fis);
                    publishProgress(100);
                    Log.i(TAG, "doInBackground: looking for " + iconName + ".png. Found locally. ");
                } else {
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(100);
                    Log.i(TAG, "doInBackground: looking for " + iconName + ".png. Needed to download. ");
                }


                URL uvUrl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection uvURLConnection = (HttpURLConnection) uvUrl.openConnection();
                InputStream inputStream = uvURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jsonObject = new JSONObject(result);

                uv = jsonObject.getDouble("value");


            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (XmlPullParserException pe) {
                ret = "XML Pull exception. The XML is not properly formed";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;


        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:

            currentTempTextView.setText("Current Temp: " + currentTemp);
            minTempTextView.setText("Min Temp: " + minTemp);
            maxTempTextView.setText("Max Temp: " + maxTemp);
            uvTextView.setText("UV Rating: " + uv);

            weatherImageView.setImageBitmap(image);

            progressBar.setVisibility(View.INVISIBLE);


        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        public ForecastQuery() {

        }

        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);

            return file.exists();
        }
    }
}




















