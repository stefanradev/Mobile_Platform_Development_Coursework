/************************************
 * Name: Stefan Radev
 * Matric number: S1631258
 *
 */

package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView rawDataDisplay;

    private String result = "";
    private String printResult = "";

    private Button roadworksButton;
    private Button plannedroadworksButton;
    private Button currentincidentsButton;

    // Traffic Scotland URLs
    private String roadworksUrlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedroadworksUrlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentincidentsUrlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlSource = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);

        roadworksButton = (Button)findViewById(R.id.roadworksButton);
        roadworksButton.setOnClickListener(this);
;
        plannedroadworksButton = (Button)findViewById(R.id.plannedroadworksButton);
        plannedroadworksButton.setOnClickListener(this);

        currentincidentsButton = (Button)findViewById(R.id.currentincidentsButton);
        currentincidentsButton.setOnClickListener(this);
    }

    public void onClick(View aview)
    {
        switch (aview.getId()) {
            case R.id.roadworksButton:
                urlSource = roadworksUrlSource;
                result = "";
                rawDataDisplay.setText("");
                startProgress(urlSource);
                break;
            case R.id.plannedroadworksButton:
                urlSource = plannedroadworksUrlSource;
                result = "";
                rawDataDisplay.setText("");
                startProgress(urlSource);
                break;
            case R.id.currentincidentsButton:
                urlSource = currentincidentsUrlSource;
                result = "";
                rawDataDisplay.setText("");
                startProgress(urlSource);
                break;
        }
    }

    public void startProgress( String urlSource1)
    {
        new Thread(new Task(urlSource1)).start();
    }

    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            //Log.e("MyTag","in run");

            try
            {
                //Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            ItemClass item = new ItemClass();
            LinkedList <ItemClass> alist = null;
            try
            {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            alist  = new LinkedList<ItemClass>();
                        }
                        else if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            item = new ItemClass();
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            String temp = xpp.nextText();
                            String temp1 = "Title: " + temp + "\n";
                            item.setTitle(temp1);
                        }
                        else if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            String temp = xpp.nextText();
                            String temp1 = "Description: " + temp + "\n";
                            item.setDescription(temp1);
                        }
                        else if (xpp.getName().equalsIgnoreCase("link"))
                        {
                            String temp = xpp.nextText();
                            String temp1 = "Link: " + temp + "\n";
                            item.setLink(temp1);
                        }
                            /*GeoRSS will be usefull when a map representation is implemented
                            else if (xpp.getName().equalsIgnoreCase("georss:point"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                String temp1 = "Georss: " + temp + "\n";
                                // Do something with text
                                //Log.e("MyTag","Georss is " + temp);
                                item.setGeorss(temp1);
                            }*/
                        else if (xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            String temp = xpp.nextText();
                            String temp1 = "Date Published: " + temp + "\n";
                            item.setPubDate(temp1);
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG)
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            alist.add(item);
                        }
                        else if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            int size;
                            size = alist.size();
                        }
                    }
                    eventType = xpp.next();
                } // End of while
            }
            catch (XmlPullParserException ae1)
            {
                Log.e("MyTag","Parsing error" + ae1.toString());
            }
            catch (IOException ae1)
            {
                Log.e("MyTag","IO error during parsing");
            }

            //Print String to help print in the app
            printResult = "";
            for(int i=0; i< alist.size(); i++)
            {
                printResult = printResult + alist.get(i) + "\n";
            }
            printResult = printResult.replace("<br />", " ");


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    //Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(printResult);
                }
            });

        }
    }

} // End of MainActivity
