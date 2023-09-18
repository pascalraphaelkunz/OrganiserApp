package com.example.organisator.Helpers.Rechnung;

import androidx.annotation.NonNull;

import java.sql.Date;

public class RechnungsDetails {

    public String name;
    public Date date;
    public float amount;

    public RechnungsDetails(String name, Date date, float amount){
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    @NonNull
    public String toString(){
        return this.name + "::" + this.date.toString() + "::" + this.amount;
    }

    public String getName(){
        return this.name;
    }

    public Date getDate(){
        return this.date;
    }

    public Float getAmount(){
        return this.amount;
    }

}
