package com.example.siamakmohsenisam.budget.model;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.siamakmohsenisam.budget.R;

import java.sql.Date;

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
        return sqLiteDatabase.update(tableName,values,DatabaseSchema.ID_ACCOUNT.getValue()+" =? " ,whereArgs);
    }
    public int updateTableCategory(ContentValues values, String tableName, String[] whereArgs ){
        return sqLiteDatabase.update(tableName,values,DatabaseSchema.ID_CATEGORY.getValue()+" =? " ,whereArgs);
    }
    public int updateTableBudget(ContentValues values, String tableName, String[] whereArgs ){
        return sqLiteDatabase.update(tableName,values,DatabaseSchema.ID_BUDGET.getValue()+" =? " ,whereArgs);
    }
    public Cursor querySelectAll(){

        String sql="";
        sql += "select * from ";
        sql += DatabaseSchema.TABLE_NAME_BUDGET.getValue()+" b , ";
        sql += DatabaseSchema.TABLE_NAME_CATEGORY.getValue()+" c , ";
        sql += DatabaseSchema.TABLE_NAME_ACCOUNT.getValue()+" a ";
        sql += " where ";
        sql += " b."+DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue()+" = a." + DatabaseSchema.ID_ACCOUNT.getValue() +" and ";
        sql += " b."+DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue()+" = c." + DatabaseSchema.ID_CATEGORY.getValue() +"  ";
        sql += " order by "+ DatabaseSchema.BUDGET_DATE.getValue() + " DESC ; ";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor queryBudget(String from,String to, int idAccount, int idCategory){

        String sql="";
        sql += "select * from ";
        sql += DatabaseSchema.TABLE_NAME_BUDGET.getValue()+" b , ";
        sql += DatabaseSchema.TABLE_NAME_CATEGORY.getValue()+" c , ";
        sql += DatabaseSchema.TABLE_NAME_ACCOUNT.getValue()+" a ";
        sql += " where ";

        if (idAccount!=-1)
            sql += " b."+DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue()+" = " + String.valueOf(idAccount) +" and ";

        if (idCategory!=-1)
            sql += " b."+DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue()+" = " + String.valueOf(idCategory) +" and ";

        sql += " b."+DatabaseSchema.BUDGET_DATE.getValue()+" BETWEEN \"" + from +"\" and \"" + to +"\" and ";

        sql += " b."+DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue()+" = a." + DatabaseSchema.ID_ACCOUNT.getValue() +" and ";
        sql += " b."+DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue()+" = c." + DatabaseSchema.ID_CATEGORY.getValue() +"  ";

        sql += " order by "+ DatabaseSchema.BUDGET_DATE.getValue() + " DESC ; ";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor querySelectAll(String tableName , String[] columns ){
        return sqLiteDatabase.query(tableName,columns,null,null,null,null,null);
    }

    public void deleteRowTableAccount(String[] whereArgs){
         sqLiteDatabase.delete(DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
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
            contentValues.put(DatabaseSchema.AMOUNT.getValue(),((Budget) object).getAmount());
            contentValues.put(DatabaseSchema.ID_ACCOUNT_FOREIGN_KEY.getValue(),(idAccountCategory[0]));
            contentValues.put(DatabaseSchema.ID_CATEGORY_FOREIGN_KEY.getValue(),(idAccountCategory[1]));
        }
        return contentValues;
    }


    public Account fillAccountWithCursor(Cursor cursor,int position) {

        Account account = new Account();
        findIdFromCursorPosition(cursor,position);

        account.setAccountNumber(cursor.getString(4));
        account.setBalance(cursor.getDouble(3));
        account.setBankName(cursor.getString(2));
        account.setAccountName(cursor.getString(1));
        return account;
    }
    public Category fillCategoryWithCursor(Cursor cursor,int position) {
        Category category = new Category();
        findIdFromCursorPosition(cursor,position);
        category.setCategoryName(cursor.getString(1));
        return category;
    }

    public int findIdFromCursorPosition(Cursor cursor,int position) {
        cursor.moveToFirst();
        for (int i = 0; i < position; i++)
            cursor.moveToNext();
        return cursor.getInt(0);
    }
    public int findPositionFromCursorId(Cursor cursor, int id) {
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getInt(0) == id)
                return i;
            cursor.moveToNext();
        }
        return 0;
    }
    public int findPositionCategoryNameFromCursor(Cursor cursor, String name) {
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(1).equals(name))
                return i;
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     *   Fill Cursors
     *
     * @param simpleCursorAdapter
     * @param cursor
     * @param object
     */


    public Cursor fillCursorAccountAdapter(SimpleCursorAdapter simpleCursorAdapter, Cursor cursor,Object object) {
        cursor = querySelectAll(DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                DatabaseSchema.ACCOUNT_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.two_text_cell,
                cursor,
                new String[]{DatabaseSchema.BANK_NAME.getValue(), DatabaseSchema.ACCOUNT_NAME.getValue()},
                new int[]{R.id.textViewLeftCell, R.id.textViewRightCell}, 1
        );
        if (object instanceof Spinner)
            ((Spinner) object).setAdapter(simpleCursorAdapter);
        if (object instanceof ListView)
            ((ListView) object).setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

        return cursor;
    }

    public Cursor fillCursorCategoryAdapter(SimpleCursorAdapter simpleCursorAdapter, Cursor cursor,Object object) {
        cursor = querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.one_text_cell,
                cursor,
                new String[]{DatabaseSchema.CATEGORY_NAME.getValue()},
                new int[]{R.id.textViewCell}, 1
        );

        if (object instanceof Spinner)
            ((Spinner) object).setAdapter(simpleCursorAdapter);
        if (object instanceof ListView)
            ((ListView) object).setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();
        return cursor;
    }

    public Cursor fillCursorBudgetAdapter(SimpleCursorAdapter simpleCursorAdapter, Cursor cursor, Object object) {
        cursor = querySelectAll();
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.four_text_cell,
                cursor,
                new String[]{DatabaseSchema.ACCOUNT_NAME.getValue(), DatabaseSchema.CATEGORY_NAME.getValue(),
                        DatabaseSchema.BUDGET_DATE.getValue(), DatabaseSchema.AMOUNT.getValue()},
                new int[]{R.id.textViewUpLeftCell, R.id.textViewUpMiddelCell,
                        R.id.textViewLeftDownCell, R.id.textViewRightCell}, 1
        );
        if (object instanceof Spinner)
            ((Spinner) object).setAdapter(simpleCursorAdapter);
        if (object instanceof ListView)
            ((ListView) object).setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

        return cursor;
    }

    public Cursor fillCursorAccountAdapter2(SimpleCursorAdapter simpleCursorAdapter, Cursor cursor, Object object) {
        cursor = querySelectAll();
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.four_text_cell,
                cursor,
                new String[]{DatabaseSchema.BANK_NAME.getValue(), DatabaseSchema.ACCOUNT_NAME.getValue(),
                        DatabaseSchema.ACCOUNT_NUMBER.getValue(), DatabaseSchema.BALANCE.getValue()},
                new int[]{R.id.textViewUpLeftCell, R.id.textViewUpMiddelCell,
                        R.id.textViewLeftDownCell, R.id.textViewRightCell}, 1
        );
        if (object instanceof Spinner)
            ((Spinner) object).setAdapter(simpleCursorAdapter);
        if (object instanceof ListView)
            ((ListView) object).setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

        return cursor;
    }
    public Cursor fillCursorBudgetAdapter2(SimpleCursorAdapter simpleCursorAdapter, Cursor cursor, Object object) {

        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.four_text_cell,
                cursor,
                new String[]{DatabaseSchema.ACCOUNT_NAME.getValue(), DatabaseSchema.CATEGORY_NAME.getValue(),
                        DatabaseSchema.BUDGET_DATE.getValue(), DatabaseSchema.AMOUNT.getValue()},
                new int[]{R.id.textViewUpLeftCell, R.id.textViewUpMiddelCell,
                        R.id.textViewLeftDownCell, R.id.textViewRightCell}, 1
        );
        if (object instanceof Spinner)
            ((Spinner) object).setAdapter(simpleCursorAdapter);
        if (object instanceof ListView)
            ((ListView) object).setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

        return cursor;
    }

}
