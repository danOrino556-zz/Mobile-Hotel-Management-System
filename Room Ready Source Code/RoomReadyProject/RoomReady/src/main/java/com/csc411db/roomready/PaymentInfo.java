package com.csc411db.roomready;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 10/31/13.
 */
public class PaymentInfo
{
    private String formOfPayment;
    private String CCNumber;
    private String CCExpMonth;
    private String CCExpYear;
    private ArrayList<IndividualPayment>folioPayments = new ArrayList<IndividualPayment>();

    public String getFormOfPayment()
    {
        return formOfPayment;
    }

    public void setFormOfPayment(String formOfPayment)
    {
        this.formOfPayment = formOfPayment;
    }

    public String getCCNumber()
    {
        return CCNumber;
    }

    public void setCCNumber(String CCNumber)
    {
        this.CCNumber = CCNumber;
    }

    public ArrayList<IndividualPayment> getFolioPayments()
    {
        return folioPayments;
    }

    public void setFolioPayments(ArrayList<IndividualPayment> folioPayments)
    {
        this.folioPayments = folioPayments;
    }

    public String getCCExpMonth()
    {
        return CCExpMonth;
    }

    public void setCCExpMonth(String CCExpMonth)
    {
        this.CCExpMonth = CCExpMonth;
    }

    public String getCCExpYear()
    {
        return CCExpYear;
    }

    public void setCCExpYear(String CCExpYear)
    {
        this.CCExpYear = CCExpYear;
    }
}
