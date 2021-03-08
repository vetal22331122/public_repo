package ru.vetal22331122.ordersparserspring.model;

import lombok.Data;

@Data
public class Order {
    private double amount;
    private String currency;
    private String comment;
    private int foundOnLine;
    private String fromFile;
    private String result;

    public String getFormattedAmount()
    {
        if(this.amount == (long) this.amount)
            return String.format("%d",(long)this.amount);
        else
            return String.format("%s",this.amount);
    }
}

