package com.csc411db.roomready;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class ActivityLogIn extends Activity
{
    private static final int ALERT_DIALOG_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        createUI();


    }

    public void createUI()
    {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        ImageView logInImage = (ImageView)findViewById(R.id.imageRRlogo);
        ImageView logInBackground = (ImageView)findViewById(R.id.imageLogInBackground);
        logInBackground.setAlpha(0.6f);
        RelativeLayout logIn = (RelativeLayout)findViewById(R.id.relLayoutLogInInfo);
        RelativeLayout logInButtons = (RelativeLayout)findViewById(R.id.relLayoutLogInButtons);

        logInImage.startAnimation(fadeInAnimation);
        logIn.startAnimation(slideInLeftAnimation);
        logInButtons.startAnimation(slideInRightAnimation);

        final TextView userID = (TextView)findViewById(R.id.textLogInUser);
        final TextView userPassword = (TextView)findViewById(R.id.textLogInPassword);

        Button cancelButton = (Button)findViewById(R.id.buttonLogInCancel);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Button logInButton = (Button)findViewById(R.id.buttonLogIn);
        logInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = userID.getText().toString();
                String password = userPassword.getText().toString();

                logInRequest(id,password);
            }
        });

    }


    public void logInRequest(final String userName, final String password)
    {
        final String urlForSearch = "http://labsoftware.org/connect_db.php";
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
                    json.put("method", "LogIn");
                    json.put("id", myID);
                    json.put("employee_id", userName);
                    json.put("login_pass", password);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        boolean logInAnswer = parseLogIn(loadTextFile(in));

                        if (logInAnswer)
                        {
                            sendMainQueryRequest("MainSearch", "Arrivals");
                        }
                        else
                        {
                             showDialog(ALERT_DIALOG_ID);
                        }
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

    public boolean parseLogIn(String IncomingJSON)
    {
        try
        {
            //Toast.makeText(getApplicationContext(), IncomingJSON, Toast.LENGTH_LONG).show();
            JSONObject incomingJSON = new JSONObject(IncomingJSON);
            String answer = incomingJSON.getString("logIn");

            if(answer.equals("true"))
            {
                return true;
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "couldnt parse the incoming", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void sendMainQueryRequest(final String methodRequested, final String stringToSearch)
    {
        final String urlForSearch = "http://labsoftware.org/connect_db.php";
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
                    json.put("method", methodRequested);
                    json.put("id", myID);
                    json.put("searchString", stringToSearch);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {

                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        Intent i = new Intent(ActivityLogIn.this, ActivityMain.class);
                        Bundle extras = new Bundle();
                        extras.putString("JSONReceived", loadTextFile(in));
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_log_in, menu);
        return true;
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }



    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case ALERT_DIALOG_ID:
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Error!");
                alert.setMessage("Incorrect User Name and/or Password");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                alert.show();
            }
        }
        return null;
    }
    
}
