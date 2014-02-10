package com.csc411db.roomready;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class ActivityMain extends Activity
{
    private ArrayList<MainSearchFolio>foliosSearched = new ArrayList<MainSearchFolio>();
    private ArrayList <String> spinnerActions = new ArrayList<String>();

    public static final int REQUEST_OK = 0;
    public static final int REQUEST_QUICK_LOOK = 1;

    private JSONParser mainJSONParser = new JSONParser();

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("JSONReceived"))
        {
            Bundle JSONreceived = getIntent().getExtras();
            String incomingJSON = JSONreceived.getString("JSONReceived");
            //Toast.makeText(getApplicationContext(), incomingJSON, Toast.LENGTH_LONG).show();
            foliosSearched = mainJSONParser.parseMainScreenJSON(incomingJSON);
            //toastOne("Searching by " + searchFor, "using a value of " + searchValue);
        }

        poulateSpinnerActions();
        createTopLeftGUI();
        createTopRightGUI();
        createBottomGUI();
    }

    public void createTopLeftGUI()
    {
        ImageButton newReservationButton = (ImageButton)findViewById(R.id.button_new_reservation);
        newReservationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityMain.this, ActivityNewReservation.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        newReservationButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("New Reservation", "On click : redirection to make a new reservation");
                return true;
            }
        });

        ImageButton newWalkinButton = (ImageButton)findViewById(R.id.button_new_walk_in);
        newWalkinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityMain.this, ActivityNewReservation.class);
                Bundle extras = new Bundle();
                extras.putString("WalkIn", "true");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        ImageButton viewTotalsButton = (ImageButton)findViewById(R.id.button_view_totals);
        viewTotalsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendTotalsRequest();
            }
        });
        viewTotalsButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("View Totals", "Oc click : redirection to view the status of all rooms and reservations");
                return true;
            }
        });
    }

    public void createBottomGUI()
    {
        final Spinner mainSelectionSpinner = (Spinner)findViewById(R.id.spinnerMain);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_text, spinnerActions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSelectionSpinner.setAdapter(adapter);
        mainSelectionSpinner.setMinimumHeight(30);
        if (getIntent().hasExtra("SpinnerValue"))
        {
            Bundle JSONreceived = getIntent().getExtras();
            String spinnerIndex = JSONreceived.getString("SpinnerValue");
            mainSelectionSpinner.setSelection(Integer.parseInt(spinnerIndex));
        }

        TableLayout mainTableLayout = (TableLayout)findViewById(R.id.tableLayoutMain);
        addRowToTable(mainTableLayout,
                "Last",
                "First",
                "Room",
                "Arrival",
                "Departure",
                "a",
                "",
                "",
                "");

        for(MainSearchFolio i: foliosSearched)
        {
            addRowToTable(mainTableLayout,
                          i.getLastName(),
                          i.getFirstName(),
                          i.getRoomNumber(),
                          i.getArrivalDate(),
                          i.getDepartureDate(),
                          i.getConfirmationNumber(),
                          i.getPhoneNumber(),
                          i.getEmailAddress(),
                          i.getFolioStatus());
        }



        ImageButton searchButton = (ImageButton)findViewById(R.id.buttonSearchMain);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getApplicationContext(), "Searching..... " +
                        //mainSelectionSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                int spinnerOptionSelected = mainSelectionSpinner.getSelectedItemPosition();
                if (spinnerOptionSelected == 0)
                {
                    //toastOne("Searching By", "Arrivals");
                    sendMainQueryRequest("MainSearch", "Arrivals", "0");
                }
                else if (spinnerOptionSelected == 1)
                {
                    //toastOne("Searching By", "Departures");
                    sendMainQueryRequest("MainSearch", "Departures", "1");
                }
                else if (spinnerOptionSelected == 2)
                {
                    //toastOne("Searching By", "In House");
                    sendMainQueryRequest("MainSearch", "InHouse", "2");
                }
                else
                {
                    //toastOne("Searching By", "All");
                    sendMainQueryRequest("MainSearch", "All", "3");
                }
            }
        });

        ImageButton micButton = (ImageButton)findViewById(R.id.buttonMicMain);
        micButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_OK);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });
        micButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return true;
            }
        });

        ImageButton findReservation = (ImageButton)findViewById(R.id.buttonFindReservation);
        findReservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityMain.this, ActivityFindFolio.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        findReservation.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("Find Folio", "On click : redirection to input fields that can find folios based on specified criteria");
                return true;
            }
        });

    }


    private void initializeTable(TableLayout table)
    {
        View spacerColumn = new View(getApplicationContext());
        TableRow row = new TableRow(this);
        row.setPadding(20,0,20,0);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource (R.drawable.abc_ab_bottom_transparent_light_holo);
        row.addView(spacerColumn, new TableRow.LayoutParams(1, 35));

        TextView tableText = new TextView(this);
        tableText.setText("Last");
        row.addView(tableText);
        TextView tableTextTwo = new TextView(this);
        tableTextTwo.setText("First");
        row.addView(tableTextTwo);
        TextView tableTextThree = new TextView(this);
        tableTextThree.setText("Room");
        row.addView(tableTextThree);
        TextView tableTextFour = new TextView(this);
        tableTextFour.setText("Arrives");
        row.addView(tableTextFour);
        TextView tableTextFive = new TextView(this);
        tableTextFive.setText("Departs");
        row.addView(tableTextFive);
        table.addView(row);
    }

    private void addRowToTable(TableLayout table,final String lastName,final String firstName, String roomNumber,
                                                 String arrival,String departure, final String confNumber,
                                                 final String phoneNumber, final String email, final String status)
    {
        //View spacerColumn = new View(getApplicationContext());
        final TableRow row = new TableRow(this);
        row.setPadding(0,0,0,0);
        row.setBackgroundResource (R.drawable.abc_ab_bottom_transparent_light_holo);
        row.setGravity(Gravity.CENTER_VERTICAL);
        //row.addView(spacerColumn, new TableRow.LayoutParams(1, 35));
        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(confNumber.equals("a")))
                {
                    Animation zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                    row.startAnimation(zoomIn);
                    sendFolioQueryRequest(confNumber);
                }

            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if(!(confNumber.equals("a")))
                {
                    final Dialog quickLook = new Dialog(context);
                    quickLook.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    Window dialogWindow = quickLook.getWindow();
                    dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                    quickLook.setContentView(R.layout.dialog_frag_main);

                    TextView last = (TextView)quickLook.findViewById(R.id.textDialogLast);
                    last.setText(lastName);
                    TextView first = (TextView)quickLook.findViewById(R.id.textDialogFirst);
                    first.setText(firstName);
                    TextView phone = (TextView)quickLook.findViewById(R.id.textDialogPhone);
                    phone.setText(phoneNumber);
                    TextView emailAdress = (TextView)quickLook.findViewById(R.id.textDialogEmail);
                    emailAdress.setText(email);
                    TextView folioStatus = (TextView)quickLook.findViewById(R.id.textDialogStatus);
                    folioStatus.setText("Status : " + status);

                    //toastOne("confirmation", confNumber);

                    ImageButton phoneButton = (ImageButton)quickLook.findViewById(R.id.buttonDialogPhone);
                    phoneButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent i = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+" + phoneNumber));
                            startActivity(i);
                        }
                    });

                    ImageButton textButton = (ImageButton)quickLook.findViewById(R.id.buttonDialogText);
                    textButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent i = new Intent (android.content.Intent.ACTION_VIEW);
                            i.putExtra("address", phoneNumber);
                            i.putExtra("sms_body", "Information regarding reservation : " +confNumber);
                            i.setType("vnd.android-dir/mms-sms");
                            startActivity(i);
                        }
                    });

                    ImageButton emailButton = (ImageButton)quickLook.findViewById(R.id.buttonDialogEmail);
                    emailButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Reservation #"+ confNumber);
                            i.putExtra(Intent.EXTRA_TEXT   , "Notification regarding the reservation : " + confNumber);
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(ActivityMain.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Button cancelButton = (Button)quickLook.findViewById(R.id.buttonDialogDone);
                    cancelButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            quickLook.dismiss();
                        }
                    });

                    //quickLook.setCancelable(true);
                    quickLook.show();

                }
                return true;
            }
        });

        TextView tableText = new TextView(this);
        tableText.setText(lastName);
        row.addView(tableText);
        TextView tableTextTwo = new TextView(this);
        tableTextTwo.setText(firstName);
        row.addView(tableTextTwo);
        TextView tableTextThree = new TextView(this);
        tableTextThree.setText(roomNumber);
        row.addView(tableTextThree);
        TextView tableTextFour = new TextView(this);
        tableTextFour.setText(arrival);
        row.addView(tableTextFour);
        TextView tableTextFive = new TextView(this);
        tableTextFive.setText(departure);
        row.addView(tableTextFive);
        table.addView(row);

    }

    public void createTopRightGUI()
    {
        ImageButton infoButton = (ImageButton)findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityMain.this, ActivityInfo.class);
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
                Intent i = new Intent(ActivityMain.this, ActivityLogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                finish();
            }
        });

        ImageButton conceirgeButton = (ImageButton)findViewById(R.id.buttonConceirge);
        conceirgeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityMain.this, ActivityConcierge.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void poulateSpinnerActions()
    {
        spinnerActions.add("Arrivals");
        spinnerActions.add("Departures");
        spinnerActions.add("In House");
        spinnerActions.add("All");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_OK  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String wordsSpoken = thingsYouSaid.get(0);
            if (wordsSpoken.equals("arrivals") || wordsSpoken.equals("Arrivals"))
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(0);
                findViewById(R.id.buttonSearchMain).performClick();
            }

            else if (wordsSpoken.equals("departures") || wordsSpoken.equals("Departures"))
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(1);
                findViewById(R.id.buttonSearchMain).performClick();
            }

            else if (wordsSpoken.equals("in house") || wordsSpoken.equals("In house"))
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(2);
                findViewById(R.id.buttonSearchMain).performClick();
            }
            else if (wordsSpoken.equals("all") || wordsSpoken.equals("All"))
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(3);
                findViewById(R.id.buttonSearchMain).performClick();
            }
            else if (wordsSpoken.equals("house accounts") || wordsSpoken.equals("House Accounts"))
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(4);
                findViewById(R.id.buttonSearchMain).performClick();
            }
            else
            {
                ((Spinner)findViewById(R.id.spinnerMain)).setSelection(0);
                findViewById(R.id.buttonSearchMain).performClick();
            }
        }
    }



    public void sendMainQueryRequest(final String methodRequested, final String stringToSearch, final String spinnerValue)
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
                        //Toast.makeText(getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), loadTextFile(in), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ActivityMain.this, ActivityMain.class);
                        Bundle extras = new Bundle();
                        extras.putString("JSONReceived", loadTextFile(in));
                        extras.putString("SpinnerValue", spinnerValue);
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


    public void sendFolioQueryRequest(final String confirmation)
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
                    json.put("method", "FolioSearch");
                    json.put("id", myID);
                    json.put("reservation_id", confirmation);
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
                        Intent i = new Intent(ActivityMain.this, ActivityNewReservation.class);
                        Bundle extras = new Bundle();
                        extras.putString("JSONReceived", loadTextFile(in));
                        extras.putString("Confirmation", confirmation);
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

    public void sendTotalsRequest()
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
                    json.put("method", "Totals");
                    json.put("id", myID);
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
                        Intent i = new Intent(ActivityMain.this, ActivityViewTotals.class);
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
}
