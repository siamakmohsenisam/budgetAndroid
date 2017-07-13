package com.example.siamakmohsenisam.budget.model;

/**
 * Created by siamakmohsenisam on 2017-06-25.
 */

public enum DatabaseSchema {

     DATABASE_NAME("budget_database"),

    /**
     *   table of Account
     */

    TABLE_NAME_ACCOUNT("account"),

    ID_ACCOUNT("_id"),
    ACCOUNT_NAME("account_name"),
    BANK_NAME("bank_name"),
    BALANCE("balance"),
    ACCOUNT_NUMBER("account_number"),
    ACCOUNT_COLUMNS("_id,account_name,bank_name,balance,account_number"),

    /**
     *   table of category
     */

    TABLE_NAME_CATEGORY("category"),

    ID_CATEGORY("_id"),
    CATEGORY_NAME("category_name"),
    CATEGORY_COLUMNS("_id,category_name"),

    /**
     *  table of budget
     */

    TABLE_NAME_BUDGET("budget"),

    ID_BUDGET("_id"),
    ID_ACCOUNT_FOREIGN_KEY("account_id"),
    ID_CATEGORY_FOREIGN_KEY("category_id"),
    BUDGET_DATE("budget_date"),
    AMOUNTH("amounth"),
    BUDGET_COLUMNS("_id,account_id,category_id,date,amounth"),

    CREATE_ACCOUNT(createAccount()),
    CREATE_CATEGORY(createCategory()),
    CREATE_BUDGET(createBudget()),

    VERSION("1");

    private final String value;

    DatabaseSchema(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static String createAccount(){

        String string="";
        string += "CREATE TABLE "+ TABLE_NAME_ACCOUNT.getValue()+"(";
        string +=  ID_ACCOUNT.getValue()+ " INTEGER PRIMARY KEY,";
        string +=  ACCOUNT_NAME.getValue()+ " TEXT , ";
        string +=  ACCOUNT_NUMBER.getValue() + " TEXT ,";
        string +=  BALANCE.getValue() + " DOUBLE ,";
        string +=  BANK_NAME.getValue() + " TEXT );";

        return string;
    }
    private static String createCategory(){

        String string="";
        string += "CREATE TABLE "+TABLE_NAME_CATEGORY.getValue()+"(";
        string +=  ID_CATEGORY.getValue()+ " INTEGER PRIMARY KEY,";
        string +=  CATEGORY_NAME.getValue()+ " TEXT unique );";

        return string;
    }
    private static String createBudget(){

        String string="";
        string += "CREATE TABLE "+TABLE_NAME_BUDGET.getValue()+"(";
        string +=  ID_BUDGET.getValue()+ " INTEGER PRIMARY KEY,";
        string +=  ID_CATEGORY_FOREIGN_KEY.getValue()+ " INTEGER ,";
        string +=  ID_ACCOUNT_FOREIGN_KEY.getValue() + " INTEGER ,";
        string +=  BUDGET_DATE.getValue() + " DATE ,";
        string +=  AMOUNTH.getValue() + " DOUBLE ,";

        string += "FOREIGN KEY ("+ ID_CATEGORY_FOREIGN_KEY.getValue()+
                  ") REFERENCES " + TABLE_NAME_CATEGORY.getValue()+" (" +ID_CATEGORY.getValue() + "),";

        string += "FOREIGN KEY ("+ ID_ACCOUNT_FOREIGN_KEY.getValue()+
                ") REFERENCES " + TABLE_NAME_ACCOUNT.getValue()+" (" + ID_ACCOUNT.getValue() + "));";

        return string;
    }

}
