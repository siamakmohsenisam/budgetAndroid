package com.example.siamakmohsenisam.budget.model;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by siamakmohsenisam on 2017-07-08.
 */

public class DatabaseManager extends Application {

    private SQLiteDatabase sqLiteDatabase = null;
    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseOpenHelper = new DatabaseOpenHelper(this);
        sqLiteDatabase = databaseOpenHelper.getWritableDatabase();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Toast.makeText(this,"Database was closed",Toast.LENGTH_LONG).show();
        sqLiteDatabase.close();
        databaseOpenHelper.close();

    }

    /**
     *
     * @param values
     * @param tableName
     * @return
     *   ##  insetr tables
     */

    public long insertInTable(ContentValues values, String tableName) {
            return sqLiteDatabase.insert(tableName, null, values);
    }

    public int updateTableAccount(ContentValues values, String tableName, String[] whereArgs ){
        return sqLiteDatabase.update(tableName,values,DatabaseSchema.ACCOUNT_NAME.getValue()+"=? "+
                DatabaseSchema.BANK_NAME.getValue()+"=? ",whereArgs);
    }

    public Cursor querySelectAll(){

        String sql="";
        sql += "select * from ";
        sql += DatabaseSchema.TABLE_NAME_BUDGET.getValue()+" b , ";
        sql += DatabaseSchema.TABLE_NAME_CATEGORY.getValue()+" c , ";
        sql += DatabaseSchema.TABLE_NAME_ACCOUNT.getValue()+" a ";
        sql += " where ";
        sql += " b."+DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue()+" = a." + DatabaseSchema.ID_ACCOUNT.getValue() +" and ";
        sql += " b."+DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue()+" = c." + DatabaseSchema.ID_CATEGORY.getValue() +" ; ";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor querySelectAll(String tableName , String[] columns ){
        return sqLiteDatabase.query(tableName,columns,null,null,null,null,null);
    }

    public long deleteRowTableAccount(String[] whereArgs){
        return sqLiteDatabase.delete(DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                DatabaseSchema.ID_ACCOUNT.getValue()+"=? ",whereArgs);
    }
    public long deleteRowTableCategory(String[] whereArgs){
        return sqLiteDatabase.delete(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                DatabaseSchema.ID_CATEGORY.getValue()+"=? ",whereArgs);
    }
    public long deleteRowTableBudget(String[] whereArgs){
        return sqLiteDatabase.delete(DatabaseSchema.TABLE_NAME_BUDGET.getValue(),
                DatabaseSchema.ID_BUDGET.getValue()+"=? ",whereArgs);
    }

//
//    public long deleteDatabaseValue(ContentValues values, String[] emails ){
//        return sqLiteDatabase.update(Schema.TABLE_NAME.getValue(),values,Schema.EMAIL.getValue()+"=?",emails);
//    }
//    public Cursor queryDatabaseValue(String column , String[] values ){
//        return sqLiteDatabase.query(Schema.TABLE_NAME.getValue(),Schema.COLUMNS.getValue().split(",")
//                ,column+"=?",values,null,null,null);
//    }
//    public void deleteDatabase(){
//
//        this.deleteDatabase(Schema.DATABASE_NAME.getValue());
//        Log.d("DATABASE","The database "+Schema.DATABASE_NAME.getValue()+" is removed successfully");
//    }
    public <T> ContentValues makeContentValue(T object, int... idAccountCategory) {

        ContentValues contentValues = new ContentValues();

        if (object instanceof Account){
            contentValues.put(DatabaseSchema.ACCOUNT_NAME.getValue(),((Account) object).getAccountName());
            contentValues.put(DatabaseSchema.BANK_NAME.getValue(),((Account) object).getBankName());
            contentValues.put(DatabaseSchema.ACCOUNT_NUMBER.getValue(),((Account) object).getAccountNumber());
            contentValues.put(DatabaseSchema.BALANCE.getValue(),((Account) object).getBalance());
        }

        if (object instanceof Category){
            contentValues.put(DatabaseSchema.CATEGORY_NAME.getValue(),((Category) object).getCategoryName());
        }

        if (object instanceof Budget){
            contentValues.put(DatabaseSchema.BUDGET_DATE.getValue(),((Budget) object).getStringDateReal());
            contentValues.put(DatabaseSchema.AMOUNTH.getValue(),((Budget) object).getAmount());
            contentValues.put(DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue(),(idAccountCategory[0]));
            contentValues.put(DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue(),(idAccountCategory[1]));
        }


        return contentValues;
    }
}
