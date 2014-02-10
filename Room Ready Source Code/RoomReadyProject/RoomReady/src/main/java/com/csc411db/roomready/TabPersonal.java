package com.csc411db.roomready;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TabPersonal extends Fragment
{
    boolean isExistingReservation = false;
    boolean isWalkInReservation = false;


    public static final int REQUEST_LAST = 1;
    public static final int REQUEST_FIRST = 2;
    public static final int REQUEST_ADDRESS = 3;
    public static final int REQUEST_CITY = 4;
    public static final int REQUEST_ZIP = 5;
    public static final int REQUEST_PHONE = 6;
    public static final int REQUEST_EMAIL = 7;
    public static final int REQUEST_COMPANY = 8;


    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =getActivity().getLayoutInflater().inflate(R.layout.tab_personal, container, false);

        if (((ActivityNewReservation)this.getActivity()).isExisting() == true)
        {
            isExistingReservation = true;
        }

        if (((ActivityNewReservation)this.getActivity()).isWalkIn()== true)
        {
            isWalkInReservation = true;
        }

        EditText lastName = (EditText)view.findViewById(R.id.textLastNameFrag);
        ImageButton lastNameMic = (ImageButton)view.findViewById(R.id.imageButtonLastFrag);
        lastNameMic.setOnClickListener(new View.OnClickListener()
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
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText firstName = (EditText)view.findViewById(R.id.textFirstNameFrag);
        ImageButton firstNameMic = (ImageButton)view.findViewById(R.id.imageButtonFirstFrag);
        firstNameMic.setOnClickListener(new View.OnClickListener()
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
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText address = (EditText)view.findViewById(R.id.textAddressFrag);
        ImageButton addressMic = (ImageButton)view.findViewById(R.id.imageButtonAddressFrag);
        addressMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_ADDRESS);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText city = (EditText)view.findViewById(R.id.textCityFrag);
        ImageButton cityMic = (ImageButton)view.findViewById(R.id.imageButtonCityFrag);
        cityMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_CITY);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }

            }
        });

        EditText zipCode = (EditText)view.findViewById(R.id.textZipFrag);
        ImageButton zipCodeMic = (ImageButton)view.findViewById(R.id.imageButtonZipFrag);
        zipCodeMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_ZIP);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText phoneNumber = (EditText)view.findViewById(R.id.textPhoneFrag);
        ImageButton phoneNumberMic = (ImageButton)view.findViewById(R.id.imageButtonPhoneFrag);
        phoneNumberMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_PHONE);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText emailAddress = (EditText)view.findViewById(R.id.textEmailFrag);
        ImageButton emailAddressMic = (ImageButton)view.findViewById(R.id.imageButtonEmailFrag);
        emailAddressMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_EMAIL);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText company = (EditText)view.findViewById(R.id.textCompanyFrag);
        ImageButton companyMic = (ImageButton)view.findViewById(R.id.imageButtonCompanyFrag);
        companyMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_COMPANY);
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Spinner stateSpinner = (Spinner)view.findViewById(R.id.spinnerStatesFrag);
        ArrayAdapter statesAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_text,
                                          ((ActivityNewReservation)this.getActivity()).getStates());
        stateSpinner.setAdapter(statesAdapter);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        if(isExistingReservation)
        {
            lastName.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getLastName());
            firstName.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getFirstName());
            address.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getAddress());
            city.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getCity());
            zipCode.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getZipCode());
            phoneNumber.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getPhoneNumber());
            emailAddress.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getEmail());
            company.setText(((ActivityNewReservation)this.getActivity()).getPersonalInfo().getCompany());


            int spinnerPosition = ((ActivityNewReservation)this.getActivity()).getStates().indexOf(
                    ((ActivityNewReservation)this.getActivity()).getPersonalInfo().getState());
            stateSpinner.setSelection(spinnerPosition);
        }


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_LAST)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText lastText = (EditText) getView().findViewById(R.id.textLastNameFrag);
            lastText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_FIRST)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText firstText = (EditText) getView().findViewById(R.id.textFirstNameFrag);
            firstText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_ADDRESS)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText addressText = (EditText) getView().findViewById(R.id.textAddressFrag);
            addressText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_CITY)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText cityText = (EditText) getView().findViewById(R.id.textCityFrag);
            cityText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_ZIP)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText zipText = (EditText) getView().findViewById(R.id.textZipFrag);
            zipText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_PHONE)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText phoneText = (EditText) getView().findViewById(R.id.textPhoneFrag);
            phoneText.setText(thingsYouSaid.get(0));
        }
        else if (requestCode== REQUEST_COMPANY)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText companyText = (EditText) getView().findViewById(R.id.textCompanyFrag);
            companyText.setText(thingsYouSaid.get(0));
        }
        else
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText emailText = (EditText) getView().findViewById(R.id.textEmailFrag);
            emailText.setText(thingsYouSaid.get(0));
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();
        ((ActivityNewReservation)this.getActivity()).setPersonalInfo();
    }
}
