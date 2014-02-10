package com.csc411db.roomready;

/**
 * Created by danielrobertson on 11/3/13.
 */
public class IndividualPayment
{
    private String paymentDescription = new String("");
    private String paymentAmount = new String("");

    public String getPaymentDescription()
    {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription)
    {
        this.paymentDescription = paymentDescription;
    }

    public String getPaymentAmount()
    {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount)
    {
        this.paymentAmount = paymentAmount;
    }
}
