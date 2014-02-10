package com.csc411db.roomready;

import org.json.JSONObject;

/**
 * Created by danielrobertson on 10/30/13.
 */
public class Folio
{
    //Private Data members
    private String confirmationNumber = new String ("0");
    private String folioStatus = new String("New Reservation");

    private PersonalInfo personal = new PersonalInfo();
    private DateInfo dates = new DateInfo();
    private PaymentInfo payments = new PaymentInfo();


    public static JSONObject getFolioJSON(Folio incomingFolio)
    {
        JSONObject json = new JSONObject();

        PersonalInfo personal = incomingFolio.getPersonal();
        DateInfo dates = incomingFolio.getDates();
        PaymentInfo payments = incomingFolio.getPayments();

        //confirmation number
        try
        {
            json.put("reservation_id", incomingFolio.getConfirmationNumber());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }




        //Insert all personal data
        try
        {
            json.put("last_name",personal.getLastName());
            json.put("first_name",personal.getFirstName());
            json.put("home_addr",personal.getAddress());
            json.put("city",personal.getCity());
            json.put("state",personal.getState());
            json.put("zip_code",personal.getZipCode());
            json.put("home_phone",personal.getPhoneNumber());
            json.put("email",personal.getEmail());
            json.put("corporation",personal.getCompany());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Insert all dates data
        try
        {
            json.put("arrive_date", Integer.toString(dates.getCheckInYear()) + "-" +
                                    Integer.toString(dates.getCheckInMonth())+ "-" +
                                    Integer.toString(dates.getCheckInDay()));

            json.put("depart_date", Integer.toString(dates.getCheckOutYear()) + "-" +
                                     Integer.toString(dates.getCheckOutMonth()) + "-" +
                                     Integer.toString(dates.getCheckOutDay()));

            //json.put("numberOfNights", Integer.toString(dates.getNumberOfNights()));
            json.put("room_type", dates.getRoomType());
            json.put("rate_type", dates.getRateType());
            json.put("book_price", dates.getRoomRate());
            json.put("lot_id", dates.getRoomNumber());
            json.put("adults", Integer.toString(dates.getNumAdults()));
            json.put("children", Integer.toString(dates.getNumCribs()));
            json.put("rollaways", Integer.toString(dates.getNumRollaways()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Insert all payment data
        try
        {
            json.put("card_type", payments.getFormOfPayment());
            json.put("card_number", payments.getCCNumber());
            json.put("exp_date_month", payments.getCCExpMonth());
            json.put("exp_date_year", payments.getCCExpYear());

            //JSONArray paymentArray = new JSONArray();
            //for (IndividualPayment i : incomingFolio.getPayments().getFolioPayments())
            //{
              //  JSONObject onePayment = new JSONObject();
                //onePayment.put("payment_description", i.getPaymentDescription());
                //onePayment.put("payment_amount", i.getPaymentAmount());
                //paymentArray.put(onePayment);
            //}
            //json.put("payment_list", paymentArray);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }

    //Setters and Getters
    public PersonalInfo getPersonal()
    {
        return personal;
    }

    public DateInfo getDates()
    {
        return dates;
    }

    public PaymentInfo getPayments()
    {
        return payments;
    }


    public void setPersonal(PersonalInfo personal)
    {
        this.personal = personal;
    }

    public void setDates(DateInfo dates)
    {
        this.dates = dates;
    }

    public void setPayments(PaymentInfo payments)
    {
        this.payments = payments;
    }

    public String getConfirmationNumber()
    {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber)
    {
        this.confirmationNumber = confirmationNumber;
    }

    public String getFolioStatus()
    {
        return folioStatus;
    }

    public void setFolioStatus(String folioStatus)
    {
        this.folioStatus = folioStatus;
    }
}
