package com.csc411db.roomready;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by danielrobertson on 10/31/13.
 */
public class JSONParser
{
    public ArrayList<MainSearchFolio> parseMainScreenJSON(String incomingJSONString)
    {
        ArrayList <MainSearchFolio> folioListToReturn = new ArrayList<MainSearchFolio>();
        try
        {
            JSONArray incomingJSONArray = new JSONArray(incomingJSONString);

            // looping through the information for each application
            for(int i = 0; i < incomingJSONArray.length(); i++)
            {
                JSONObject folioInJSONArray = incomingJSONArray.getJSONObject(i);

                MainSearchFolio shownFolio = new MainSearchFolio();

                // Storing each json item in variable
                shownFolio.setFolioStatus(folioInJSONArray.getString("guest_status"));
                shownFolio.setLastName(folioInJSONArray.getString("last_name"));
                shownFolio.setFirstName(folioInJSONArray.getString("first_name"));
                shownFolio.setRoomNumber(folioInJSONArray.getString("lot_id"));
                shownFolio.setArrivalDate(folioInJSONArray.getString("arrive_date"));
                shownFolio.setDepartureDate(folioInJSONArray.getString("depart_date"));
                shownFolio.setConfirmationNumber(folioInJSONArray.getString("reservation_id"));
                shownFolio.setPhoneNumber(folioInJSONArray.getString("home_phone"));
                shownFolio.setEmailAddress(folioInJSONArray.getString("email"));
                shownFolio.setCompanyName(folioInJSONArray.getString("corporation"));
                folioListToReturn.add(shownFolio);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return folioListToReturn;
    }

    //User has selected an app from the ListView
    //Now we will get more specific information for the app thats selected
    public Folio parseFolioJSON(String incomingJSONString)
    {
        Folio folioToReturn = new Folio();
        try
        {

            JSONArray incomingJSONArrayNot = new JSONArray(incomingJSONString);
            JSONObject folioDetailJSON = incomingJSONArrayNot.getJSONObject(0);

            folioToReturn.setFolioStatus(folioDetailJSON.getString("guest_status"));
            folioToReturn.getPersonal().setLastName(folioDetailJSON.getString("last_name"));
            folioToReturn.getPersonal().setFirstName(folioDetailJSON.getString("first_name"));
            folioToReturn.getPersonal().setAddress(folioDetailJSON.getString("home_addr"));
            folioToReturn.getPersonal().setCity(folioDetailJSON.getString("city"));
            folioToReturn.getPersonal().setState(folioDetailJSON.getString("state"));
            folioToReturn.getPersonal().setZipCode(folioDetailJSON.getString("zip_code"));
            folioToReturn.getPersonal().setPhoneNumber(folioDetailJSON.getString("home_phone"));
            folioToReturn.getPersonal().setCompany(folioDetailJSON.getString("corporation"));
            folioToReturn.getPersonal().setEmail(folioDetailJSON.getString("email"));


            //Parse Arrive
            String tempArrival = folioDetailJSON.getString("arrive_date");

            StringTokenizer st = new StringTokenizer(tempArrival, "-");
            ArrayList<String> parsedDate = new ArrayList<String>();
            while (st.hasMoreElements())
            {
                parsedDate.add((String)st.nextElement());

            }
            folioToReturn.getDates().setCheckInYear(Integer.parseInt(parsedDate.get(0)));
            folioToReturn.getDates().setCheckInMonth(Integer.parseInt(parsedDate.get(1)));
            folioToReturn.getDates().setCheckInDay(Integer.parseInt(parsedDate.get(2)));



            //Parse Depart
            String tempDepart = folioDetailJSON.getString("depart_date");

            st = new StringTokenizer(tempDepart, "-");
            parsedDate = new ArrayList<String>();
            while (st.hasMoreElements())
            {
                parsedDate.add((String)st.nextElement());
            }
            folioToReturn.getDates().setCheckOutYear(Integer.parseInt(parsedDate.get(0)));
            folioToReturn.getDates().setCheckOutMonth(Integer.parseInt(parsedDate.get(1)));
            folioToReturn.getDates().setCheckOutDay(Integer.parseInt(parsedDate.get(2)));


            folioToReturn.getDates().setRoomType(folioDetailJSON.getString("description"));
            folioToReturn.getDates().setRateType(folioDetailJSON.getString("rate_type"));
            folioToReturn.getDates().setRoomRate(folioDetailJSON.getString("book_price"));
            folioToReturn.getDates().setRoomNumber(folioDetailJSON.getString("lot_id"));
            //folioToReturn.getDates().setNumberOfNights(Integer.parseInt(folioDetailJSON.getString("lastName")));
            folioToReturn.getDates().setNumCribs(Integer.parseInt(folioDetailJSON.getString("children")));
            folioToReturn.getDates().setNumRollaways(Integer.parseInt(folioDetailJSON.getString("rollaways")));
            folioToReturn.getDates().setNumAdults(Integer.parseInt(folioDetailJSON.getString("adults")));


            folioToReturn.getPayments().setFormOfPayment(folioDetailJSON.getString("card_type"));
            folioToReturn.getPayments().setCCNumber(folioDetailJSON.getString("card_number"));
            folioToReturn.getPayments().setCCExpMonth(folioDetailJSON.getString("exp_date_month"));
            folioToReturn.getPayments().setCCExpYear(folioDetailJSON.getString("exp_date_year"));


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return folioToReturn;
    }

    public Totals parseTotalsJSON(String incomingJSONString)
    {
        Totals totalsToReturn = new Totals();
        try
        {
            JSONObject incomingJSON = new JSONObject(incomingJSONString);

            totalsToReturn.setNumberOfArrivals(incomingJSON.getString("num_arrivals"));
            totalsToReturn.setNumberOFDepartures(incomingJSON.getString("num_departures"));
            totalsToReturn.setDoublesRemaining(incomingJSON.getString("num_doubles"));
            totalsToReturn.setKingsRemaining(incomingJSON.getString("num_kings"));
            totalsToReturn.setKingsCouchRemaining(incomingJSON.getString("num_kings_couch"));
            totalsToReturn.setKingsHandicappedRemaining(incomingJSON.getString("num_king_handicapped"));
            totalsToReturn.setKingsWhirlpoolRemaining(incomingJSON.getString("num_kings_whirlpool"));
            totalsToReturn.setNumberOfRoomsRemaining(incomingJSON.getString("room_remaining"));
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return totalsToReturn;
    }

    public JSONObject sendFolio(Folio foilioToSend)
    {
        JSONObject foilioJSON = new JSONObject();
        return foilioJSON;
    }

    public ArrayList<String> parseRoomList(String incomingRoomList)
    {
        ArrayList<String> correctRoomNumbersByType = new ArrayList<String>();

        try
        {

            JSONArray incomingJSONArray = new JSONArray(incomingRoomList);

            for(int i = 0; i < incomingJSONArray.length(); i++)
            {
                JSONObject roomNumber = incomingJSONArray.getJSONObject(i);
                correctRoomNumbersByType.add(roomNumber.getString("lot_id"));
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return correctRoomNumbersByType;
    }

}
