package com.example.siamakmohsenisam.budget.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Created by siamakmohsenisam on 2017-07-05.
 */

public class Budget {

    private Account account;
    private Category category;
    private Calendar date;
    private double amount;

    public Budget() {
        this(new Account(),new Category(),new GregorianCalendar(),0.0);
    }

    public Budget(Account account, Category category, Calendar date, double amount) {
        this.account = account;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (String.valueOf(amount).matches(MyPattern.DecimalNumber.getMyPattern()))
            this.amount = Double.valueOf(String.format("%15.2f",amount));
        else throw new IllegalArgumentException("amount is not correct");
    }

    @Override
    public String toString() {
        return  ""+ date + "\n" + account +"\n"+ category +" : "+  amount ;
    }

    public String getStringDate(){
        String myDate="";
        myDate += date.get(Calendar.YEAR)+"-";
        if (1+date.get(Calendar.MONTH)<10)
            myDate += "0";
        myDate += (1+date.get(Calendar.MONTH));
        myDate += "-";
        if (date.get(Calendar.DATE)<10)
            myDate += "0";
        myDate += (date.get(Calendar.DATE));

        return myDate;
    }
    public String getStringDateReal(){
        String myDate="";
        myDate += date.get(Calendar.YEAR)+"-";
        if (date.get(Calendar.MONTH)<10)
            myDate += "0";
        myDate += (date.get(Calendar.MONTH));
        myDate += "-";
        if (date.get(Calendar.DATE)<10)
            myDate += "0";
        myDate += (date.get(Calendar.DATE));

        return myDate;
    }

    public void setDate(int year, int month , int day) {
        if((""+year+"-"+month+"-"+day).matches(MyPattern.Date.getMyPattern()))
            date.set(year,month,day);
        else throw new IllegalArgumentException("your birthday is not correct");
    }
    public void setDate(String date) {
        if (date.matches(MyPattern.Date.getMyPattern())) {
            StringTokenizer stringTokenizer = new StringTokenizer(date, "-");
            this.date.set(Integer.valueOf(stringTokenizer.nextToken()),
                    Integer.valueOf(stringTokenizer.nextToken()),
                    Integer.valueOf(stringTokenizer.nextToken()));
        } else throw new IllegalArgumentException("your birthday is not correct");
    }


}
