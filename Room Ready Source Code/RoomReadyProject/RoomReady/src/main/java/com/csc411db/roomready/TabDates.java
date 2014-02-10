package com.csc411db.roomready;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Calendar;

public class TabDates extends Fragment
{
    boolean isExistingReservation = false;
    boolean isWalkInReservation = false;

    static final int REQUEST_CHECK_IN = 2;
    static final int REQUEST_CHECK_OUT = 3;

    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    ArrayAdapter roomNumberAdapt;


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        View view =getActivity().getLayoutInflater().inflate(R.layout.tab_dates, container, false);

        if (((ActivityNewReservation)this.getActivity()).isExisting() == true)
        {
            isExistingReservation = true;
        }

        if (((ActivityNewReservation)this.getActivity()).isWalkIn()== true)
        {
            isWalkInReservation = true;
        }


        final TextView arrivalDate = (TextView)view.findViewById(R.id.textArrivalValue);


        final TextView departureDate = (TextView)view.findViewById(R.id.textDepartureValue);


        TextView numNights = (TextView)view.findViewById(R.id.textNightsValue);


        setToday();
        arrivalDate.setText(Integer.toString(month+1) + "-" + Integer.toString(day) + "-" + Integer.toString(year));

        departureDate.setText(Integer.toString(month+1) + "-" + Integer.toString(day + 1) + "-" + Integer.toString(year));
        numNights.setText("1");

        final Spinner numAdultsSpinner = (Spinner)view.findViewById(R.id.spinnerNumAdultsFrag);
        ArrayAdapter adultsAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                                                ((ActivityNewReservation)this.getActivity()).getNumAdults());
        numAdultsSpinner.setAdapter(adultsAdapter);
        adultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner numOfCribsSpinner = (Spinner)view.findViewById(R.id.spinnerNumCribsFrag);
        ArrayAdapter cribsAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                                                ((ActivityNewReservation)this.getActivity()).getNumCribs());
        numOfCribsSpinner.setAdapter(cribsAdapter);
        cribsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner numRollawaysSpinner = (Spinner)view.findViewById(R.id.spinnerNumRollawaysFrag);
        ArrayAdapter rollawayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                                                ((ActivityNewReservation)this.getActivity()).getNumberOfRollaways());
        numRollawaysSpinner.setAdapter(rollawayAdapter);
        rollawayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner roomTypesSpinner = (Spinner)view.findViewById(R.id.spinnerRoomTypeFrag);
        ArrayAdapter roomTypesAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                                                ((ActivityNewReservation)this.getActivity()).getRoomTypes());
        roomTypesSpinner.setAdapter(roomTypesAdapter);
        roomTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner rateTypeSpinner = (Spinner)view.findViewById(R.id.spinnerRateTypeFrag);
        ArrayAdapter rateTypeAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                                        ((ActivityNewReservation)this.getActivity()).getRateTypes());
        rateTypeSpinner.setAdapter(rateTypeAdapter);
        rateTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner roomNumbersSpinner = (Spinner)view.findViewById(R.id.spinnerRoomNumberFrag);
        final ArrayAdapter roomNumbersAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                ((ActivityNewReservation)this.getActivity()).getRoomNumbers());
        roomNumbersSpinner.setAdapter(roomNumbersAdapter);
        roomNumbersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roomNumberAdapt = roomNumbersAdapter;


        final TextView rateValue = (TextView)view.findViewById(R.id.textRateValueFrag);


        //If it is an existing reservation we will set all the text and spinners its values
        if(isExistingReservation == true)
        {
            arrivalDate.setText(Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInMonth()) + "-" +
                    Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInDay()) + "-" +
                    Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInYear()));

            departureDate.setText(Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutMonth()) + "-" +
                    Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutDay()) + "-" +
                    Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutYear()));

            numNights.setText(Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutDay() -
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInDay()));


            rateValue.setText(((ActivityNewReservation)this.getActivity()).getDateInfo().getRoomRate());

            int spinnerPosition = ((ActivityNewReservation)this.getActivity()).getNumAdults().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getNumAdults());
            numAdultsSpinner.setSelection(spinnerPosition);

            spinnerPosition = ((ActivityNewReservation)this.getActivity()).getRateTypes().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getRateType());
            rateTypeSpinner.setSelection(spinnerPosition);


            spinnerPosition = ((ActivityNewReservation)this.getActivity()).getRoomTypes().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getRoomType());
            roomTypesSpinner.setSelection(spinnerPosition);

            spinnerPosition = ((ActivityNewReservation)this.getActivity()).getNumberOfRollaways().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getNumRollaways());
            numRollawaysSpinner.setSelection(spinnerPosition);

            spinnerPosition = ((ActivityNewReservation)this.getActivity()).getNumCribs().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getNumCribs());
            numOfCribsSpinner.setSelection(spinnerPosition);

            spinnerPosition = ((ActivityNewReservation)this.getActivity()).getRoomNumbers().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getDateInfo().getRoomNumber());
            roomNumbersSpinner.setSelection(spinnerPosition);

        }

        final String arrival = Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInYear()) + "-" +
                Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInMonth()) + "-" +
                Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckInDay());

        final String depart = Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutYear()) + "-" +
                Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutMonth()) + "-" +
                Integer.toString(((ActivityNewReservation)this.getActivity()).getDateInfo().getCheckOutDay());

        roomTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //king
                if (position == 0)
                {
                    requestRoomNumbers("RoomNumbers", "KING", arrival, depart);
                }
                //double
                else if (position == 1)
                {
                    requestRoomNumbers("RoomNumbers", "DBLE", arrival, depart);
                }
                //king couch
                else if (position == 2)
                {
                    requestRoomNumbers("RoomNumbers", "KCOU", arrival, depart);
                }
                //king handicapped
                else if (position == 3)
                {
                    requestRoomNumbers("RoomNumbers", "KHAN",arrival, depart);
                }
                //king whirlpool
                else if (position == 4)
                {
                    requestRoomNumbers("RoomNumbers", "KWHI", arrival, depart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        rateTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    rateValue.setText("$ 109.00");
                }
                else if (position == 1)
                {
                    rateValue.setText("$ 99.00");
                }
                else if (position == 2)
                {
                    rateValue.setText("$ 84.00");
                }
                else if (position == 3)
                {
                    rateValue.setText("$ 89.00");
                }
                else if (position == 4)
                {
                    rateValue.setText("$ 29.00");
                }
                else
                {
                    rateValue.setText("$ 59.00");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        final TextView checkInText = (TextView)view.findViewById(R.id.textCheckInFrag);
        final Activity parentActivity = getActivity();

        ImageButton checkInDateButton = (ImageButton)view.findViewById(R.id.imageCheckInFrag);
        checkInDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                parentActivity.showDialog(REQUEST_CHECK_IN);
            }
        });
        if(isWalkInReservation == true)
        {
            checkInDateButton.setVisibility(View.INVISIBLE);
        }


        final TextView checkOutText = (TextView)view.findViewById(R.id.textCheckOutFrag);

        ImageButton checkOutButton = (ImageButton)view.findViewById(R.id.imageCheckOutFrag);
        checkOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                parentActivity.showDialog(REQUEST_CHECK_OUT);
            }
        });

        return view;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        ((ActivityNewReservation)this.getActivity()).setDateInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

    }

    public void requestRoomNumbers(final String methodRequested, final String stringToSearch, final String arrivalDate,
                                                                                              final String departDate)
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
                    json.put("arrive_date", arrivalDate);
                    json.put("depart_date", departDate);
                    //Toast.makeText(getActivity().getApplicationContext(), "I am sending : " + json.toString(), Toast.LENGTH_LONG).show();
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        String output = loadTextFile(in);
                        //Toast.makeText(getActivity().getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity().getApplicationContext(), "Room numbers received : " + output, Toast.LENGTH_LONG).show();
                        updateRooms(output);
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
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

    public void setToday()
    {
        try
        {
            ((ActivityNewReservation)this.getActivity()).setCurrentDay();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateRooms(String incomingRoomList)
    {
        try
        {
            ((ActivityNewReservation)this.getActivity()).updateRooms(incomingRoomList);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
