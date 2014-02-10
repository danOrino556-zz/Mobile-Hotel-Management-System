package com.csc411db.roomready;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Calendar;

public class ActivityNewReservation extends Activity
{
    boolean isExistingReservation = false;
    boolean isWalkInReservation = false;

    public boolean isExisting()
    {
        return isExistingReservation;
    }

    public boolean isWalkIn()
    {
        return isWalkInReservation;
    }


    JSONParser folioParser = new JSONParser();

    //Holds all folio info entered by the user
    private Folio currentFolio = new Folio();
    public Folio getCurrentFolio()
    {
        return currentFolio;
    }
    private DateInfo folioDate = new DateInfo();
    private PersonalInfo folioPersonal = new PersonalInfo();
    private PaymentInfo folioPayment = new PaymentInfo();
    public DateInfo getDateInfo()
    {
        return folioDate;
    }
    public PersonalInfo getPersonalInfo()
    {
        return folioPersonal;
    }

    public PaymentInfo getPaymentInfo()
    {
        return folioPayment;
    }


    //Fragment Management! Tabs!
    final TabDates datesFrag = new TabDates();
    final TabPayment paymentFrag = new TabPayment();
    final TabPersonal personalFrag = new TabPersonal();

    //Used to update the folio fragment
    FragmentManager fragmentManager = getFragmentManager();

    private static final int ALERT_DIALOG_ID = 1;
    private static final int REQUEST_CHECK_IN = 2;
    private static final int REQUEST_CHECK_OUT = 3;


    //Holds enum spinner values
    private ArrayList<String> roomTypes = new ArrayList<String>();
    private ArrayList<String>rateType = new ArrayList<String>();
    private ArrayList<Integer>numberOfAdults = new ArrayList<Integer>();
    private ArrayList<Integer>numberOfCribs = new ArrayList<Integer>();
    private ArrayList<Integer>numberOfRollaways = new ArrayList<Integer>();
    private ArrayList<String> paymentTypes = new ArrayList<String>();
    private ArrayList<String> states = new ArrayList<String>();
    private ArrayList<String>months = new ArrayList<String>();
    private ArrayList<String>years = new ArrayList<String>();
    public ArrayList<String>roomNumbers = new ArrayList<String>();

    //Getters for the above arrays
    public ArrayList<String>getRoomNumbers()
    {
        return roomNumbers;
    }

    public ArrayList<String> getRoomTypes()
    {
        return roomTypes;
    }
    public ArrayList<String> getRateTypes()
    {
        return rateType;
    }
    public ArrayList<Integer> getNumAdults()
    {
        return numberOfAdults;
    }
    public ArrayList<Integer> getNumCribs()
    {
        return numberOfCribs;
    }
    public ArrayList<Integer> getNumberOfRollaways()
    {
        return numberOfRollaways;
    }
    public ArrayList<String> getPaymentTypes()
    {
        return paymentTypes;
    }
    public ArrayList<String> getStates()
    {
        return states;
    }

    public ArrayList<String> getMonths()
    {
        return months;
    }

    public ArrayList<String>getYears()
    {
        return years;
    }

    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_reservation);

        //If it has a "JSONReceived then it is an existing reservation
        if(getIntent().hasExtra("JSONReceived"))
        {
            isExistingReservation = true;
            Bundle JSONreceived = getIntent().getExtras();
            String folioInfo = JSONreceived.getString("JSONReceived");
            String confirmation = JSONreceived.getString("Confirmation");
            //Toast.makeText(getApplicationContext(), folioInfo, Toast.LENGTH_LONG).show();

            currentFolio = folioParser.parseFolioJSON(folioInfo);
            folioDate = currentFolio.getDates();
            folioPayment = currentFolio.getPayments();
            folioPersonal = currentFolio.getPersonal();

            currentFolio.setConfirmationNumber(confirmation);
        }

        if(getIntent().hasExtra("WalkIn"))
        {
            isWalkInReservation = true;
        }

        createTopLeftGUI();
        createTopRightGUI();
        createBottomGUI();
        fillSpinners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_new_reservation, menu);
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

        ImageButton newWalkinButton = (ImageButton)findViewById(R.id.button_new_walk_in);
        newWalkinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAllFragments();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getCurrentFolio().getPersonal().getEmail()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Reservation #"+ getCurrentFolio().getConfirmationNumber());
                i.putExtra(Intent.EXTRA_TEXT   , "Confirmation Number : " + getCurrentFolio().getConfirmationNumber() + "\n" +
                 getCurrentFolio().getPersonal().getFirstName() + " " + getCurrentFolio().getPersonal().getLastName() + "\n" +
                 "Arrival : " + getCurrentFolio().getDates().getCheckInDay() + "-" +
                                getCurrentFolio().getDates().getCheckInMonth() + "-" +
                                getCurrentFolio().getDates().getCheckInYear() + "\n" +
                "Departure : " + getCurrentFolio().getDates().getCheckOutDay() + "-" +
                                 getCurrentFolio().getDates().getCheckOutMonth() + "-" +
                                 getCurrentFolio().getDates().getCheckOutYear() + "\n" +
                "Card on file : " + getCurrentFolio().getPayments().getFormOfPayment() + "\n" +
                "Card Number : " + getCurrentFolio().getPayments().getCCNumber() + "\n" +
                "Expiration Month : " + getCurrentFolio().getPayments().getCCExpMonth() + "\n" +
                "Expiration Year : " + getCurrentFolio().getPayments().getCCExpYear() + "\n" + "\n" +
                "Thanks for staying with us, Mr./Ms." + getCurrentFolio().getPersonal().getLastName());

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        newWalkinButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("New Walk-in", "On click : redirection to make a new walk-in");
                return true;
            }
        });

        ImageButton cancelReservationButton = (ImageButton)findViewById(R.id.button_view_totals);
        cancelReservationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAllFragments();

                sendFolioStatusUpdate("cancel", getCurrentFolio().getConfirmationNumber());

                final Dialog cancellationDialog = new Dialog(context);
                cancellationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = cancellationDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                cancellationDialog.setContentView(R.layout.notification_box);

                TextView alertText = (TextView)cancellationDialog.findViewById(R.id.textDialogAlert);
                alertText.setText("Reservation #" + getCurrentFolio().getConfirmationNumber() + " has been cancelled");

                Button cancelButton = (Button)cancellationDialog.findViewById(R.id.buttonDialogAlert);
                cancelButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        cancellationDialog.dismiss();
                        finish();
                    }
                });

                //cancellationDialog.setCancelable(false);
                cancellationDialog.show();

            }
        });
        cancelReservationButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                toastOne("View Totals", "On click : redirection to view the status of all rooms and reservations");
                return true;
            }
        });

        if (!isExistingReservation)
        {
            cancelReservationButton.setVisibility(View.INVISIBLE);
            newWalkinButton.setVisibility(View.INVISIBLE);
        }

    }

    public void createTopRightGUI()
    {
        ImageButton infoButton = (ImageButton)findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityNewReservation.this, ActivityInfo.class);
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
                Intent i = new Intent(ActivityNewReservation.this, ActivityLogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
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

        TextView guestStatus = (TextView)findViewById(R.id.textGuestStatus);
        if(isExistingReservation)
        {
            guestStatus.setText("#" + currentFolio.getConfirmationNumber()+" - "+currentFolio.getFolioStatus());
        }
        else
        {
            guestStatus.setText(currentFolio.getFolioStatus());
        }

    }

    public void createBottomGUI()
    {
        //Initialize with personal info
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.relLayoutThreeFrag, personalFrag, "personal");
        fragmentTransaction.commit();


        final ImageView personalTab = (ImageView)findViewById(R.id.imagePersonalTab);
        personalTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                personalTab.setAlpha(1.0f);
                Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                personalTab.startAnimation(bounce);
                ImageView datesTab = (ImageView)findViewById(R.id.imageDatesTab);
                ImageView paymentTab = (ImageView)findViewById(R.id.imagePaymentTab);
                datesTab.setAlpha(0.4f);
                paymentTab.setAlpha(0.4f);
                FragmentTransaction fragmentTransactionPersonal = fragmentManager.beginTransaction();
                fragmentTransactionPersonal.replace(R.id.relLayoutThreeFrag, personalFrag, "personal");
                fragmentTransactionPersonal.addToBackStack("personal");
                fragmentTransactionPersonal.commit();
            }
        });

        final ImageView datesTab = (ImageView)findViewById(R.id.imageDatesTab);
        datesTab.setAlpha(0.4f);
        datesTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datesTab.setAlpha(1.0f);
                Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                datesTab.startAnimation(bounce);
                final ImageView personalTab = (ImageView)findViewById(R.id.imagePersonalTab);
                final ImageView paymentTab = (ImageView)findViewById(R.id.imagePaymentTab);
                personalTab.setAlpha(0.4f);
                paymentTab.setAlpha(0.4f);
                FragmentTransaction fragmentTransactionDates = fragmentManager.beginTransaction();
                fragmentTransactionDates.replace(R.id.relLayoutThreeFrag, datesFrag, "dates");
                fragmentTransactionDates.addToBackStack("dates");
                fragmentTransactionDates.commit();
            }
        });

        final ImageView paymentTab = (ImageView)findViewById(R.id.imagePaymentTab);
        paymentTab.setAlpha(0.4f);
        paymentTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                paymentTab.setAlpha(1.0f);
                Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                paymentTab.startAnimation(bounce);
                final ImageView datesTab = (ImageView)findViewById(R.id.imageDatesTab);
                final ImageView personalTab = (ImageView)findViewById(R.id.imagePersonalTab);
                datesTab.setAlpha(0.4f);
                personalTab.setAlpha(0.4f);
                FragmentTransaction fragmentTransactionPayments = fragmentManager.beginTransaction();
                fragmentTransactionPayments.replace(R.id.relLayoutThreeFrag, paymentFrag, "payments");
                fragmentTransactionPayments.addToBackStack("payments");
                fragmentTransactionPayments.commit();
            }
        });


        //Once changes/entries have been made to a folio
        //These two button save/execute those changes
        ImageView saveReservationButton = (ImageView)findViewById(R.id.imageReservationSave);
        saveReservationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAllFragments();
                if(areAnyFolioFieldsNull())
                {
                    sendFolioStatusUpdate();


                    final Dialog cancellationDialog = new Dialog(context);
                    cancellationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    Window dialogWindow = cancellationDialog.getWindow();
                    dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                    cancellationDialog.setContentView(R.layout.notification_box);

                    TextView alertText = (TextView)cancellationDialog.findViewById(R.id.textDialogAlert);
                    alertText.setText("Reservation #" + getCurrentFolio().getConfirmationNumber() + " has been saved");

                    Button cancelButton = (Button)cancellationDialog.findViewById(R.id.buttonDialogAlert);
                    cancelButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            cancellationDialog.dismiss();
                            finish();
                        }
                    });
                    //cancellationDialog.setCancelable(false);
                    cancellationDialog.show();

                }
            }
        });
        saveReservationButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {


                return true;
            }
        });


        ImageView executeFolio = (ImageView)findViewById(R.id.imageReservationExecute);
        executeFolio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAllFragments();
                sendFolioStatusUpdate("checkIn", getCurrentFolio().getConfirmationNumber());

                final Dialog cancellationDialog = new Dialog(context);
                cancellationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = cancellationDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                cancellationDialog.setContentView(R.layout.notification_box);

                TextView alertText = (TextView)cancellationDialog.findViewById(R.id.textDialogAlert);

                if(currentFolio.getFolioStatus().equals("In House"))
                {

                    alertText.setText("Reservation #" + getCurrentFolio().getConfirmationNumber() + " has been checked out");
                }
                if(currentFolio.getFolioStatus().equals("Reserved") || currentFolio.getFolioStatus().equals("Checked Out"))
                {
                    alertText.setText("Reservation #" + getCurrentFolio().getConfirmationNumber() + " has been checked in");
                }


                Button cancelButton = (Button)cancellationDialog.findViewById(R.id.buttonDialogAlert);
                cancelButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        cancellationDialog.dismiss();
                        finish();
                    }
                });

                cancellationDialog.setCancelable(false);
                cancellationDialog.show();


            }
        });
        executeFolio.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return true;
            }
        });

        if(!isExistingReservation)
        {
            executeFolio.setVisibility(View.INVISIBLE);
        }
    }

    public void checkAllFragments()
    {
        checkPersonalVisible();
        checkPaymentsVisible();
        checkDatesVisible();
    }

    public void checkPersonalVisible()
    {
        try
        {
            TabPersonal personal = (TabPersonal)getFragmentManager().findFragmentByTag("personal");
            if(personal.isVisible())
            {
                setPersonalInfo();
                //toastOne("personal", "is visible");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void checkPaymentsVisible()
    {
        try
        {
            TabPayment payments = (TabPayment)getFragmentManager().findFragmentByTag("payments");
            if(payments.isVisible())
            {
                setPaymentInfo();
                //toastOne("payments", "is visible");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void checkDatesVisible()
    {
        try
        {
            TabDates dates = (TabDates)getFragmentManager().findFragmentByTag("dates");
            if(dates.isVisible())
            {
                setDateInfo();
                //toastOne("dates", "is visible");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setPersonalInfo()
    {
        try
        {
            TextView last = (TextView)personalFrag.getView().findViewById(R.id.textLastNameFrag);
            TextView first = (TextView)personalFrag.getView().findViewById(R.id.textFirstNameFrag);
            TextView address = (TextView)personalFrag.getView().findViewById(R.id.textAddressFrag);
            TextView city = (TextView)personalFrag.getView().findViewById(R.id.textCityFrag);
            Spinner state = (Spinner)personalFrag.getView().findViewById(R.id.spinnerStatesFrag);
            TextView zip = (TextView)personalFrag.getView().findViewById(R.id.textZipFrag);
            TextView phone = (TextView)personalFrag.getView().findViewById(R.id.textPhoneFrag);
            TextView email = (TextView)personalFrag.getView().findViewById(R.id.textEmailFrag);
            TextView company = (TextView)personalFrag.getView().findViewById(R.id.textCompanyFrag);

            folioPersonal.setLastName(last.getText().toString());
            folioPersonal.setFirstName(first.getText().toString());
            folioPersonal.setAddress(address.getText().toString());
            folioPersonal.setCity(city.getText().toString());
            folioPersonal.setState(state.getSelectedItem().toString());
            folioPersonal.setZipCode(zip.getText().toString());
            folioPersonal.setPhoneNumber(phone.getText().toString());
            folioPersonal.setEmail(email.getText().toString());
            folioPersonal.setCompany(company.getText().toString());
        }

        catch(Exception e)
        {
            e.printStackTrace();
            toastOne("Error", "setting personalInfo");
        }


        currentFolio.setPersonal(folioPersonal);
    }

    public void setDateInfo()
    {
        try
        {
            Spinner roomType = (Spinner)datesFrag.getView().findViewById(R.id.spinnerRoomTypeFrag);
            Spinner rateType = (Spinner)datesFrag.getView().findViewById(R.id.spinnerRateTypeFrag);
            TextView roomRate = (TextView)datesFrag.getView().findViewById(R.id.textRateValueFrag);
            Spinner numAdults = (Spinner)datesFrag.getView().findViewById(R.id.spinnerNumAdultsFrag);
            Spinner numCribs = (Spinner)datesFrag.getView().findViewById(R.id.spinnerNumCribsFrag);
            Spinner numRollaways = (Spinner)datesFrag.getView().findViewById(R.id.spinnerNumRollawaysFrag);
            TextView numNights = (TextView)datesFrag.getView().findViewById(R.id.textNightsValue);
            Spinner roomNumber = (Spinner)datesFrag.getView().findViewById(R.id.spinnerRoomNumberFrag);


            folioDate.setRoomNumber(roomNumber.getSelectedItem().toString());
            folioDate.setNumberOfNights(Integer.parseInt(numNights.getText().toString()));
            folioDate.setRoomType(roomType.getSelectedItem().toString());
            folioDate.setRateType(rateType.getSelectedItem().toString());
            folioDate.setRoomRate(roomRate.getText().toString());
            folioDate.setNumAdults(Integer.parseInt(numAdults.getSelectedItem().toString()));
            folioDate.setNumCribs(Integer.parseInt(numCribs.getSelectedItem().toString()));
            folioDate.setNumRollaways(Integer.parseInt(numRollaways.getSelectedItem().toString()));
        }

        catch(Exception e)
        {
            e.printStackTrace();
            toastOne("Error", "setting dateInfo");
        }


        currentFolio.setDates(folioDate);
    }

    public void setPaymentInfo()
    {
        try
        {
            Spinner ccType = (Spinner)paymentFrag.getView().findViewById(R.id.spinnerPaymentTypesFrag);
            TextView ccNumber = (TextView)paymentFrag.getView().findViewById(R.id.editTextCCNumberFrag);
            Spinner expMonth = (Spinner)paymentFrag.getView().findViewById(R.id.spinnerExpMonths);
            Spinner expYears = (Spinner)paymentFrag.getView().findViewById(R.id.spinnerExpYears);

            folioPayment.setFormOfPayment(ccType.getSelectedItem().toString());
            folioPayment.setCCNumber(ccNumber.getText().toString());
            folioPayment.setCCExpMonth(expMonth.getSelectedItem().toString());
            folioPayment.setCCExpYear(expYears.getSelectedItem().toString());
        }

        catch (Exception e)
        {
            e.printStackTrace();
            toastOne("Error", "Setting paymentInfo");
        }

        currentFolio.setPayments(folioPayment);
    }

    public boolean areAnyFolioFieldsNull()
    {
        ArrayList <String> unfilledFields = new ArrayList<String>();


        if(getCurrentFolio().getPersonal().getLastName() == null ||
           getCurrentFolio().getPersonal().getLastName().equals(""))
        {
            unfilledFields.add("Last Name");
        }
        if(getCurrentFolio().getPersonal().getFirstName() == null ||
                getCurrentFolio().getPersonal().getFirstName().equals(""))
        {
            unfilledFields.add("First Name");
        }
        if(getCurrentFolio().getPersonal().getAddress() == null ||
                getCurrentFolio().getPersonal().getAddress().equals(""))
        {
            unfilledFields.add("Address");
        }
        if(getCurrentFolio().getPersonal().getCity() == null ||
                getCurrentFolio().getPersonal().getCity().equals("") )
        {
            unfilledFields.add("City");
        }
        if(getCurrentFolio().getPersonal().getState() == null)
        {
            unfilledFields.add("State");
        }
        if(getCurrentFolio().getPersonal().getZipCode() == null ||
                getCurrentFolio().getPersonal().getZipCode().equals(""))
        {
            unfilledFields.add("Zip Code");
        }
        if(getCurrentFolio().getPersonal().getPhoneNumber() == null ||
                getCurrentFolio().getPersonal().getPhoneNumber().equals("") )
        {
            unfilledFields.add("Phone Number");
        }
        if(getCurrentFolio().getPersonal().getEmail() == null ||
                getCurrentFolio().getPersonal().getEmail().equals("") )
        {
            unfilledFields.add("Email");
        }
        if(getCurrentFolio().getPersonal().getCompany() == null ||
                getCurrentFolio().getPersonal().getCompany().equals(""))
        {
            unfilledFields.add("Company");
        }
        if(getCurrentFolio().getPayments().getCCNumber() == null ||
                getCurrentFolio().getPayments().getCCNumber().equals("CC Number"))
        {
            unfilledFields.add("Credit Card Number");
        }
        if(unfilledFields.isEmpty())
        {
            return true;
        }



        else
        {
            String fieldsThatNeedAttention = new String("Please Fill Out the Following Fields : \n \n");
            for(String i:unfilledFields)
            {
                fieldsThatNeedAttention = fieldsThatNeedAttention + i + "\n";
            }

            final Dialog cancellationDialog = new Dialog(context);
            cancellationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            Window dialogWindow = cancellationDialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
            cancellationDialog.setContentView(R.layout.notification_box);

            TextView alertText = (TextView)cancellationDialog.findViewById(R.id.textDialogAlert);
            alertText.setText(fieldsThatNeedAttention);

            Button cancelButton = (Button)cancellationDialog.findViewById(R.id.buttonDialogAlert);
            cancelButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    cancellationDialog.dismiss();
                }
            });

            //quickLook.setCancelable(true);
            cancellationDialog.show();
        }
        return false;
    }


    public void fillSpinners()
    {
        //dates tab
        numberOfCribs.add(0);
        numberOfCribs.add(1);
        numberOfCribs.add(2);

        for (int i = 1; i<6; i++)
        {
            numberOfAdults.add(i);
        }

        numberOfRollaways.add(0);
        numberOfRollaways.add(1);
        numberOfRollaways.add(2);

        roomTypes.add("1 King Bed");
        roomTypes.add("2 Double Beds");
        roomTypes.add("King w/Couch");
        roomTypes.add("King Handicapped");
        roomTypes.add("King w/Whirlpool");

        rateType.add("Normal");
        rateType.add("Corporate");
        rateType.add("Government");
        rateType.add("State");
        rateType.add("Employee");
        rateType.add("Family");

        //Room Numbers
        roomNumbers.add("101");roomNumbers.add("102");roomNumbers.add("103");roomNumbers.add("104");
        roomNumbers.add("105");roomNumbers.add("106");roomNumbers.add("107");roomNumbers.add("108");
        roomNumbers.add("109");roomNumbers.add("110");roomNumbers.add("111");roomNumbers.add("112");
        roomNumbers.add("113");roomNumbers.add("114");roomNumbers.add("115");roomNumbers.add("116");
        roomNumbers.add("117");roomNumbers.add("118");roomNumbers.add("119");roomNumbers.add("120");

        roomNumbers.add("201");roomNumbers.add("202");roomNumbers.add("203");roomNumbers.add("204");
        roomNumbers.add("205");roomNumbers.add("206");roomNumbers.add("207");roomNumbers.add("208");
        roomNumbers.add("209");roomNumbers.add("210");roomNumbers.add("211");roomNumbers.add("212");
        roomNumbers.add("213");roomNumbers.add("214");roomNumbers.add("215");roomNumbers.add("216");
        roomNumbers.add("217");roomNumbers.add("218");roomNumbers.add("219");roomNumbers.add("220");


        //payment tab
        paymentTypes.add("American Express");
        paymentTypes.add("Discover");
        paymentTypes.add("Mastercard");
        paymentTypes.add("Visa");

        //personal tab
        states.add("AK");states.add("AL");states.add("AR");states.add("AZ");states.add("CA");states.add("CO");
        states.add("CT");states.add("DC");states.add("DE");states.add("FL");states.add("GA");states.add("HI");
        states.add("IA");states.add("ID");states.add("IL");states.add("IN");states.add("KS");states.add("KY");
        states.add("LA");states.add("MA");states.add("MD");states.add("ME");states.add("MI");states.add("MN");
        states.add("MO");states.add("MS");states.add("MT");states.add("NC");states.add("ND");states.add("NE");
        states.add("NH");states.add("NJ");states.add("NM");states.add("NV");states.add("NY");states.add("OH");
        states.add("OK");states.add("OR");states.add("PA");states.add("RI");states.add("SC");states.add("SD");
        states.add("TN");states.add("TX");states.add("UT");states.add("VA");states.add("VT");states.add("WA");
        states.add("WI");states.add("WV");states.add("WY");

        //Expiration months
        months.add("01");months.add("02");months.add("03");months.add("04");months.add("05");months.add("06");
        months.add("07");months.add("08");months.add("09");months.add("10");months.add("11");months.add("12");

        //Expiration years
        years.add("2013");years.add("2014");years.add("2015");years.add("2016");years.add("2017");years.add("2018");
        years.add("2019");years.add("2020");years.add("2021");years.add("2022");years.add("2023");years.add("2024");
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
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case REQUEST_CHECK_IN:
            {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH)+1;

                DatePickerDialog datePicker = new DatePickerDialog(this, dateListenerCheckIn, year, month, day);
                return datePicker;
            }

            case REQUEST_CHECK_OUT:
            {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH) + 2;

                DatePickerDialog datePicker = new DatePickerDialog(this, dateListenerCheckOut, year, month, day);
                return datePicker;
            }
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener dateListenerCheckIn = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            TextView checkOutDate = (TextView)datesFrag.getView().findViewById(R.id.textArrivalValue);
            checkOutDate.setText((monthOfYear+1) + "-" + (dayOfMonth)+ "-" + year);
            folioDate.setCheckInDay(dayOfMonth);
            folioDate.setCheckInYear(year);
            folioDate.setCheckInMonth(monthOfYear+1);
        }
    };

    DatePickerDialog.OnDateSetListener dateListenerCheckOut = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            TextView checkOutDate = (TextView)datesFrag.getView().findViewById(R.id.textDepartureValue);
            checkOutDate.setText( (monthOfYear+1) + "-" + (dayOfMonth) + "-" + year);
            folioDate.setCheckOutDay(dayOfMonth);
            folioDate.setCheckOutYear(year);
            folioDate.setCheckOutMonth(monthOfYear+1);

            TextView numNights = (TextView)datesFrag.getView().findViewById(R.id.textNightsValue);
            Integer nights = folioDate.getCheckOutDay() - folioDate.getCheckInDay();
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(isWalkInReservation == true)
            {
                numNights.setText(Integer.toString(folioDate.getCheckOutDay() - day));
            }
            else
            {
                numNights.setText(nights.toString());
            }

            //folioDate.setNumberOfNights((folioDate.getCheckOutDay() - folioDate.getCheckInDay()));
        }
    };

    public void setCurrentDay()
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        folioDate.setCheckInDay(day+1);
        folioDate.setCheckInYear(year);
        folioDate.setCheckInMonth(month+1);

        folioDate.setCheckOutDay(day+2);
        folioDate.setCheckOutYear(year);
        folioDate.setCheckOutMonth(month+1);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void sendFolioStatusUpdate()
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
                JSONObject json = Folio.getFolioJSON(getCurrentFolio());

                //Sending a message to the server
                try
                {
                    Integer myID = 1;
                    HttpPost post = new HttpPost(urlForSearch);
                    json.put("method", "FolioUpdate");
                    json.put("id", myID);
                    //Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {

                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        //Toast.makeText(getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        //toastOne("Answer", loadTextFile(in));
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

    public void sendFolioStatusUpdate(final String methodRequested, final String valueToUse)
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
                JSONObject json = Folio.getFolioJSON(getCurrentFolio());

                //Sending a message to the server
                try
                {
                    Integer myID = 1;
                    HttpPost post = new HttpPost(urlForSearch);
                    json.put("method", methodRequested);
                    json.put("id", myID);
                    json.put("reservation_id", valueToUse);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                                /*Checking response */
                    if(response!=null)
                    {

                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        //toastOne("Answer", loadTextFile(in));
                        //Toast.makeText(getApplicationContext(), "established a connection and received a response", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), loadTextFile(in), Toast.LENGTH_LONG).show();
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


    public void updateRooms(String incomingRoomList)
    {
        if (!isExistingReservation)
        {
            JSONParser roomParser = new JSONParser();
            getRoomNumbers().clear();
            ArrayList<String> newRoomNumbers  = roomParser.parseRoomList(incomingRoomList);
            for(String i: newRoomNumbers)
            {
                roomNumbers.add(i);
                //datesFrag.updateRoomList();
            }
        }
    }

}
