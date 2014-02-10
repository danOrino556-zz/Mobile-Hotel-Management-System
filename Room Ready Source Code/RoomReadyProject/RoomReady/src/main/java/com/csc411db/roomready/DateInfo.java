package com.csc411db.roomready;

import java.util.Calendar;

/**
 * Created by danielrobertson on 10/31/13.
 */
public class DateInfo
{
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    private Integer checkInMonth = new Integer(month);
    private Integer checkInDay = new Integer(day);
    private Integer checkInYear = new Integer(year);

    private Integer checkOutMonth = new Integer(month);
    private Integer checkOutDay = new Integer(day+1);
    private Integer checkOutYear = new Integer(year);

    private Integer numberOfNights = new Integer(1);
    private String roomType = new String("2 Double Beds");
    private String rateType = new String("Normal");
    private String roomRate = new String("109.00");
    private String roomNumber = new String ("112");

    private Integer numAdults = new Integer(1);
    private Integer numCribs = new Integer(0);
    private Integer numRollaways = new Integer(0);

    public Integer getCheckInMonth()
    {
        return checkInMonth;
    }

    public void setCheckInMonth(Integer checkInMonth)
    {
        this.checkInMonth = checkInMonth;
    }

    public Integer getCheckInDay()
    {
        return checkInDay;
    }

    public void setCheckInDay(Integer checkInDay)
    {
        this.checkInDay = checkInDay;
    }

    public Integer getCheckInYear()
    {
        return checkInYear;
    }

    public void setCheckInYear(Integer checkInYear)
    {
        this.checkInYear = checkInYear;
    }

    public Integer getCheckOutMonth()
    {
        return checkOutMonth;
    }

    public void setCheckOutMonth(Integer checkOutMonth)
    {
        this.checkOutMonth = checkOutMonth;
    }

    public Integer getCheckOutDay()
    {
        return checkOutDay;
    }

    public void setCheckOutDay(Integer checkOutDay)
    {
        this.checkOutDay = checkOutDay;
    }

    public Integer getCheckOutYear()
    {
        return checkOutYear;
    }

    public void setCheckOutYear(Integer checkOutYear)
    {
        this.checkOutYear = checkOutYear;
    }

    public Integer getNumberOfNights()
    {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights)
    {
        this.numberOfNights = numberOfNights;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public String getRateType()
    {
        return rateType;
    }

    public void setRateType(String rateType)
    {
        this.rateType = rateType;
    }

    public String getRoomRate()
    {
        return roomRate;
    }

    public void setRoomRate(String roomRate)
    {
        this.roomRate = roomRate;
    }

    public Integer getNumAdults()
    {
        return numAdults;
    }

    public void setNumAdults(Integer numAdults)
    {
        this.numAdults = numAdults;
    }

    public Integer getNumCribs()
    {
        return numCribs;
    }

    public void setNumCribs(Integer numCribs)
    {
        this.numCribs = numCribs;
    }

    public Integer getNumRollaways()
    {
        return numRollaways;
    }

    public void setNumRollaways(Integer numRollaways)
    {
        this.numRollaways = numRollaways;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber)
    {
        this.roomNumber = roomNumber;
    }
}
