package com.csc411db.roomready;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import java.util.ArrayList;

public class ActivityFindFolio extends Activity
{

    public static final int REQUEST_LAST = 1;
    public static final int REQUEST_FIRST = 2;
    public static final int REQUEST_CONF = 3;
    public static final int REQUEST_ROOM = 4;
    public static final int REQUEST_CC = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_folio);

        createTopLeftGUI();
        createTopRightGUI();
        createBottomGUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_find_folio, menu);
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
                Intent i = new Intent(ActivityFindFolio.this, ActivityInfo.class);
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
                Intent i = new Intent(ActivityFindFolio.this, ActivityLogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }

    public void createBottomGUI()
    {
        final EditText lastNameField = (EditText)findViewById(R.id.textFindLastName);
        final EditText confirmationNumberField = (EditText)findViewById(R.id.textFindConfirmation);
        final EditText roomNumberField = (EditText)findViewById(R.id.textFindRoomNumber);
        final EditText firstNameField = (EditText) findViewById(R.id.textFindFirstName);
        final EditText CCField = (EditText)findViewById(R.id.textFindCC);

        //Search By Last Name
        ImageButton searchLastButton = (ImageButton)findViewById(R.id.buttonFindLast);
        searchLastButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String lastNameSearched = lastNameField.getText().toString();
                //toastOne("Searching Last Name : ", lastNameSearched);
                sendMainQueryDetailedRequest("MainSearchSpecific" , "last_name", lastNameSearched);

            }
        });
        ImageButton searchLastMic = (ImageButton)findViewById(R.id.buttonFindLastMic);
        searchLastMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_LAST);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Search By First Name
        ImageButton searchFirstButton = (ImageButton)findViewById(R.id.buttonFindFirst);
        searchFirstButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String firstNameSearched = firstNameField.getText().toString();
                //toastOne("Searching First Name : ", firstNameSearched);
                sendMainQueryDetailedRequest("MainSearchSpecific", "first_name", firstNameSearched);
            }
        });
        ImageButton searchByFirstMic = (ImageButton)findViewById(R.id.buttonFindFirstMic);
        searchByFirstMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_FIRST);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Search by Confirmation Number
        ImageButton searchConfirmationButton = (ImageButton)findViewById(R.id.buttonFindConf);
        searchConfirmationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String confirmationSearched = confirmationNumberField.getText().toString();
                //toastOne("Searching Confirmation Number : ", confirmationSearched);
                sendMainQueryDetailedRequest("MainSearchSpecific", "reservation_id", confirmationSearched);
            }
        });
        ImageButton searchConfirmationMic = (ImageButton)findViewById(R.id.buttonFindConfMic);
        searchConfirmationMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_CONF);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Search by Room Number
        ImageButton searchRoomNumberButton = (ImageButton)findViewById(R.id.buttonFindRoom);
        searchRoomNumberButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String roomNumberSearched = roomNumberField.getText().toString();
                //toastOne("Searching Room Number :", roomNumberSearched);
                sendMainQueryDetailedRequest("MainSearchSpecific", "lot_id", roomNumberSearched);
            }
        });
        ImageButton searchRoomNumberMic = (ImageButton)findViewById(R.id.buttonFindRoomMic);
        searchRoomNumberMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_ROOM);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton searchCCButton = (ImageButton)findViewById(R.id.buttonFindCC);
        searchCCButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ccValue = CCField.getText().toString();
                //toastOne("Searching CC :", ccValue);
                sendMainQueryDetailedRequest("MainSearchSpecific", "card_number", ccValue);
            }
        });
        ImageButton searchCCMic = (ImageButton)findViewById(R.id.buttonFindCCMic);
        searchCCMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_CC);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_LAST  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText last = (EditText)findViewById(R.id.textFindLastName);
            last.setText(thingsYouSaid.get(0));
            ImageButton lastButton = (ImageButton)findViewById(R.id.buttonFindLast);
            lastButton.performClick();
            finish();
        }
        else if (requestCode== REQUEST_FIRST  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText first = (EditText)findViewById(R.id.textFindFirstName);
            first.setText(thingsYouSaid.get(0));
            ImageButton firstButton = (ImageButton)findViewById(R.id.buttonFindFirst);
            firstButton.performClick();
        }
        else if (requestCode== REQUEST_CC  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText cc = (EditText)findViewById(R.id.textFindCC);
            cc.setText(thingsYouSaid.get(0));
            ImageButton ccButton = (ImageButton)findViewById(R.id.buttonFindCC);
            ccButton.performClick();
        }
        else if (requestCode== REQUEST_CONF  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText confirmation = (EditText)findViewById(R.id.textFindConfirmation);
            confirmation.setText(thingsYouSaid.get(0));
            ImageButton confButton = (ImageButton)findViewById(R.id.buttonFindConf);
            confButton.performClick();
        }
        if (requestCode== REQUEST_ROOM  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText room = (EditText)findViewById(R.id.textFindRoomNumber);
            room.setText(thingsYouSaid.get(0));
            ImageButton roomButton = (ImageButton)findViewById(R.id.buttonFindRoom);
            roomButton.performClick();
        }
    }

    public void sendMainQueryDetailedRequest(final String methodRequested, final String searchFor,
                                             final String searchBy)
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
                    json.put("searchFor", searchFor);
                    json.put("searchBy", searchBy);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        //Toast.makeText(getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), loadTextFile(in), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(ActivityFindFolio.this, ActivityMain.class);
                        Bundle extras = new Bundle();
                        extras.putString("JSONReceived", loadTextFile(in));
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();
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
}
