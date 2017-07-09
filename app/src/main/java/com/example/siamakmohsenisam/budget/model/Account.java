package com.example.siamakmohsenisam.budget.model;

/**
 * Created by siamakmohsenisam on 2017-07-05.
 */

public class Account {

    private String accountNumber;
    private String accountName;
    private String bankName;
    private Double balance;

    public Account() {
        this("","","",0.0);
    }

    public Account(String accountNumber, String accountName, String bankName, Double balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.bankName = bankName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        if (accountNumber.matches(MyPattern.UserName.getMyPattern())){
            accountNumber = accountNumber.substring(0,1).toUpperCase()+ accountNumber.substring(1).toLowerCase();
            this.accountNumber = accountNumber;
        }
        else throw new IllegalArgumentException("account number is not correct");
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        if (accountName.matches(MyPattern.Word.getMyPattern())) {
            accountName = accountName.substring(0, 1).toUpperCase() + accountName.substring(1).toLowerCase();
            this.accountName = accountName;
        }
        else throw new IllegalArgumentException("account name is not correct");

    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        if (bankName.matches(MyPattern.Word.getMyPattern())){
            bankName = bankName.substring(0,1).toUpperCase()+ bankName.substring(1).toLowerCase();
            this.bankName = bankName;
        }
        else throw new IllegalArgumentException("bank name is not correct");
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        if (String.valueOf(balance).matches(MyPattern.DecimalNumber.getMyPattern()))
            this.balance = balance;
        else throw new IllegalArgumentException("balance is not correct");

    }

    @Override
    public String toString() {
        return  bankName +" "+accountName + " " + accountNumber + " : " + balance ;
    }
}
