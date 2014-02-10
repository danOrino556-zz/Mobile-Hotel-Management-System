package com.csc411db.roomready;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabPayment extends Fragment
{
    boolean isExistingReservation = false;
    boolean isWalkInReservation = false;


    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =getActivity().getLayoutInflater().inflate(R.layout.tab_payment, container, false);

        if (((ActivityNewReservation)this.getActivity()).isExisting() == true)
        {
            isExistingReservation = true;
        }

        if (((ActivityNewReservation)this.getActivity()).isWalkIn()== true)
        {
            isWalkInReservation = true;
        }

        final Spinner paymentTypeSpinner = (Spinner)view.findViewById(R.id.spinnerPaymentTypesFrag);
        ArrayAdapter paymentAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                ((ActivityNewReservation)this.getActivity()).getPaymentTypes());
        paymentTypeSpinner.setAdapter(paymentAdapter);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int spinnerPosition = ((ActivityNewReservation)this.getActivity()).getPaymentTypes().indexOf(
                ((ActivityNewReservation)this.getActivity()).getPaymentInfo().getFormOfPayment());
        paymentTypeSpinner.setSelection(spinnerPosition);




        final Spinner expirationMonth = (Spinner)view.findViewById(R.id.spinnerExpMonths);
        ArrayAdapter monthExpAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_text,
                ((ActivityNewReservation)this.getActivity()).getMonths());
        expirationMonth.setAdapter(monthExpAdapter);
        monthExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPosition = ((ActivityNewReservation)this.getActivity()).getMonths().indexOf(
                ((ActivityNewReservation)this.getActivity()).getPaymentInfo().getCCExpMonth());
        expirationMonth.setSelection(spinnerPosition);



        final Spinner expirationYears = (Spinner)view.findViewById(R.id.spinnerExpYears);
        ArrayAdapter monthYearsAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_text,
                ((ActivityNewReservation)this.getActivity()).getYears());
        expirationYears.setAdapter(monthYearsAdapter);
        monthYearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPosition = ((ActivityNewReservation)this.getActivity()).getYears().indexOf(
                ((ActivityNewReservation)this.getActivity()).getPaymentInfo().getCCExpYear());
        expirationYears.setSelection(spinnerPosition);


        EditText ccNumber = (EditText)view.findViewById(R.id.editTextCCNumberFrag);
        ccNumber.setText(((ActivityNewReservation)this.getActivity()).getPaymentInfo().getCCNumber());

        TableLayout paymentTable = (TableLayout)view.findViewById(R.id.paymentTable);
        //addRowToTable(paymentTable, 20, "11/3/13", "American Express", "22.13");

        return view;
    }

    private void addRowToTable(TableLayout table, int rowsCount, String date, String paymentType, String paymentAmount)
    {
        for (int i = 0; i < rowsCount; i++)
        {
            TableRow row = new TableRow(getActivity());
            View spacerColumn = new View(getActivity());
            row.setBackgroundResource (R.drawable.abc_ab_bottom_transparent_light_holo);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.addView(spacerColumn, new TableRow.LayoutParams(1, 35));

            TextView tableText = new TextView(getActivity());
            tableText.setText(date);
            tableText.setPadding(5, 5, 5, 5);
            row.addView(tableText);
            TextView tableTextTwo = new TextView(getActivity());
            tableTextTwo.setText(paymentType);
            tableText.setPadding(5, 5, 5, 5);
            row.addView(tableTextTwo);
            TextView tableTextThree = new TextView(getActivity());
            tableTextThree.setText(paymentAmount);
            tableText.setPadding(5, 5, 5, 5);
            row.addView(tableTextThree);
            table.addView(row);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        ((ActivityNewReservation)this.getActivity()).setPaymentInfo();
    }
}
