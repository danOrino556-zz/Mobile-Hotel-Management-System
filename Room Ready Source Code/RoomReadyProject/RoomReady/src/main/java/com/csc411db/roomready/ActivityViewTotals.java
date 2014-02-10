package com.csc411db.roomready;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActivityViewTotals extends Activity
{

    Totals hotelTotals = new Totals();
    JSONParser mainJSONParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_totals);

        if(getIntent().hasExtra("JSONReceived"))
        {
            Bundle JSONreceived = getIntent().getExtras();
            String incomingJSON = JSONreceived.getString("JSONReceived");
            //Toast.makeText(getApplicationContext(), incomingJSON, Toast.LENGTH_LONG).show();
            hotelTotals = mainJSONParser.parseTotalsJSON(incomingJSON);
            //toastOne("Searching by " + searchFor, "using a value of " + searchValue);
        }

        createTopLeftGUI();
        createTopRightGUI();
        createBottom();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_view_totals, menu);
        return true;
    }

    public void createTopLeftGUI()
    {
        ImageButton backButton = (ImageButton)findViewById(R.id.button_new_reservation);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                finish();
            }
        });
        backButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("New Reservation", "On click : redirection to make a new reservation");
                return true;
            }
        });
    }

    public void createTopRightGUI()
    {
        ImageButton infoButton = (ImageButton)findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityViewTotals.this, ActivityInfo.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        infoButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return true;
            }
        });

        ImageButton settingsButton = (ImageButton)findViewById(R.id.buttonSettingsMain);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        settingsButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("Settings", "Fill me out!");
                return true;
            }
        });
    }

    public void createBottom()
    {
        TextView numArrivals = (TextView)findViewById(R.id.textNumArrivalsTotals);
        numArrivals.setText(hotelTotals.getNumberOfArrivals());

        TextView numDepartures = (TextView)findViewById(R.id.textNumDeparturesTotals);
        numDepartures.setText(hotelTotals.getNumberOFDepartures());

        TextView numRoomsRemaining = (TextView)findViewById(R.id.textRoomsRemainingTotals);
        numRoomsRemaining.setText(hotelTotals.getNumberOfRoomsRemaining());

        TextView doublesRemaining = (TextView)findViewById(R.id.textDoubleBedsTotals);
        doublesRemaining.setText(hotelTotals.getDoublesRemaining());

        TextView kingsRemaining = (TextView)findViewById(R.id.textKingsTotals);
        kingsRemaining.setText(hotelTotals.getKingsRemaining());

        TextView kingsCouchRemaining = (TextView)findViewById(R.id.textKingCouchTotals);
        kingsCouchRemaining.setText(hotelTotals.getKingsCouchRemaining());

        TextView kingsWhirlpoolRemaining = (TextView)findViewById(R.id.textKingWhirlpoolTotals);
        kingsWhirlpoolRemaining.setText(hotelTotals.getKingsWhirlpoolRemaining());

        TextView kingsHandicappedRemaining = (TextView)findViewById(R.id.textKingHandicappedTotals);
        kingsHandicappedRemaining.setText(hotelTotals.getKingsHandicappedRemaining());
    }

    public void sendTotalsRequest()
    {
        final String urlForSearch = "http://headers.jsontest.com";
        Thread t = new Thread()
        {
            public void run()
            {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                //Sending a message to the server
                try
                {
                    Integer myID = 1;
                    HttpPost post = new HttpPost(urlForSearch);
                    json.put("method", "ViewTotals");
                    json.put("id", myID);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {

                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        Toast.makeText(getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), loadTextFile(in), Toast.LENGTH_LONG).show();
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "couldnt establish connection", Toast.LENGTH_LONG).show();
                }
                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();
    }


    public String loadTextFile(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0)
            byteStream.write(bytes, 0, len);
        return new String(byteStream.toByteArray(), "UTF8");
    }

    public void toastOne(String textToDisplayTitle, String description)
    {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast_layout_one,
                (ViewGroup) findViewById(R.id.relativeLayoutToast));

        Toast myToastMessage = new Toast(getApplicationContext());

        TextView toastText = (TextView) layout.findViewById(R.id.textViewToast);
        toastText.setText(textToDisplayTitle);

        TextView toastDescription = (TextView)layout.findViewById(R.id.textViewToastDescription);
        toastDescription.setText(description);
        myToastMessage.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        myToastMessage.setDuration(Toast.LENGTH_LONG);
        myToastMessage.setView(layout);
        myToastMessage.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
    
}
