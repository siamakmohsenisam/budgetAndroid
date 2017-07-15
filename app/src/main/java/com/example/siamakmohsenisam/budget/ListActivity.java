package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.siamakmohsenisam.budget.model.Account;
import com.example.siamakmohsenisam.budget.model.Category;
import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;

public class ListActivity extends AppCompatActivity implements SwipeMenuCreator,
        SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, TextWatcher,DialogInterface.OnClickListener{

    /**
     * ****************************************
     * <p>
     * All Value
     * <p>
     * ****************************************
     */

    EditText editTextNameA, editTextNumberA, editTextBankNameA, editTextBalanceA,
            editTextNameC;

    ImageButton imageButtonCancelA, imageButtonSaveA, imageButtonCancelC, imageButtonSaveC;

    TextView textViewAddAC, textViewtitleAC;

    Account account;
    Category category;

    Dialog dialog;
    AlertDialog alertDialog;
    AlertDialog.Builder aBuilder;

    SwipeMenuListView swipeMenuListViewAC;

    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor,cursorForDelete;
    DatabaseManager databaseManager;

    Intent intent;

    int editIndex = -1;
    String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initianize();

        if (getIntent().getExtras() != null)
            textViewtitleAC.setText(getIntent().getStringExtra("tag"));

        if (textViewtitleAC.getText().equals("Account list"))
            initializeDialogAccount();
        if (textViewtitleAC.getText().equals("Category list"))
            initializeDialogCategory();

    }

    /**
     * ****************************************
     * <p>
     * All initialize
     * <p>
     * ****************************************
     */
    private void initianize() {

        databaseManager = (DatabaseManager) getApplication();

        swipeMenuListViewAC = (SwipeMenuListView) findViewById(R.id.swipeMenuListViewAC);
        textViewAddAC = (TextView) findViewById(R.id.textViewAddAC);
        textViewtitleAC = (TextView) findViewById(R.id.textViewtitleAC);

        textViewAddAC.setOnClickListener(this);
        swipeMenuListViewAC.setMenuCreator(this);
        swipeMenuListViewAC.setOnMenuItemClickListener(this);

        aBuilder = new AlertDialog.Builder(this);
    }

    private void initializeDialogAccount() {

        fillCursorAccountAdapter();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.account_popup);

        editTextBalanceA = (EditText) dialog.findViewById(R.id.editTextBalanceA);
        editTextBankNameA = (EditText) dialog.findViewById(R.id.editTextBankNameA);
        editTextNameA = (EditText) dialog.findViewById(R.id.editTextNameA);
        editTextNumberA = (EditText) dialog.findViewById(R.id.editTextNumberA);
        imageButtonCancelA = (ImageButton) dialog.findViewById(R.id.imageButtonCancelA);
        imageButtonSaveA = (ImageButton) dialog.findViewById(R.id.imageButtonSaveA);

        editTextBalanceA.addTextChangedListener(this);
        imageButtonCancelA.setOnClickListener(this);
        imageButtonSaveA.setOnClickListener(this);

    }

    private void initializeDialogCategory() {

        fillCursorCategoryAdapter();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.category_popup);

        editTextNameC = (EditText) dialog.findViewById(R.id.editTextNameC);
        imageButtonSaveC = (ImageButton) dialog.findViewById(R.id.imageButtonSaveC);
        imageButtonCancelC = (ImageButton) dialog.findViewById(R.id.imageButtonCancelC);

        imageButtonCancelC.setOnClickListener(this);
        imageButtonSaveC.setOnClickListener(this);

    }
    private void startAlert(String title) {
        aBuilder.setTitle(title);
        aBuilder.setNegativeButton("Ok", this);
        aBuilder.setIcon(R.drawable.alert);
        alertDialog = aBuilder.create();
        alertDialog.show();
    }
    /**
     * *     ****************************************
     *
     * @param menu swipe list view
     *             <p>
     *             *     ****************************************
     */
    @Override
    public void create(SwipeMenu menu) {

        SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
        // set item background
        editItem.setBackground(R.drawable.solid_green);
        // set item width
        editItem.setWidth(170);
        // set item title
        editItem.setTitle("Edit");
        // set item title fontsize
        editItem.setTitleSize(12);
        // set item title font color
        editItem.setTitleColor(Color.WHITE);
        editItem.setIcon(R.drawable.edit_bar);
        // add to menu
        menu.addMenuItem(editItem);

        // second butten
        SwipeMenuItem cancelItem = new SwipeMenuItem(getApplicationContext());
        // set item background
        cancelItem.setBackground(R.drawable.solid_red);
        // set item width
        cancelItem.setWidth(170);
        // set item title
        cancelItem.setTitle("delete");
        // set item title fontsize
        cancelItem.setTitleSize(12);
        // set item title font color
        cancelItem.setTitleColor(Color.WHITE);
        cancelItem.setIcon(R.drawable.delete_bar);
        // add to menu
        menu.addMenuItem(cancelItem);
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

        switch (index) {

            // delete button
            case 0:
                editIndex = position;
                if (textViewtitleAC.getText().equals("Account list")) {
                    fillPopupAccount();
                    dialog.show();
                }
                if (textViewtitleAC.getText().equals("Category list")) {
                    fillPopupCategory();
                    dialog.show();
                }
                break;
            case 1:
                if (textViewtitleAC.getText().equals("Account list")) {
                    deleteFromBudget(databaseManager.findIdFromCursorPosition(cursor, position),2);
                    databaseManager.deleteRowTableAccount(new String[]{
                            String.valueOf(databaseManager.findIdFromCursorPosition(cursor,position))
                    });
                    fillCursorAccountAdapter();
                }
                if (textViewtitleAC.getText().equals("Category list")) {
                    deleteFromBudget(databaseManager.findIdFromCursorPosition(cursor,position),1);
                    databaseManager.deleteRowTableCategory(new String[]{
                            String.valueOf(databaseManager.findIdFromCursorPosition(cursor,position))
                    });
                    fillCursorCategoryAdapter();
                }
                break;
        }

        return false;
    }

    /**
     * @param v Onclicklistener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textViewAddAC:
                if (textViewtitleAC.getText().equals("Account list"))
                    cleanPopupAccount();
                if (textViewtitleAC.getText().equals("Category list"))
                    cleanPopupCategory();
                dialog.show();
                break;

            case R.id.imageButtonCancelA:
                dialog.dismiss();
                break;

            case R.id.imageButtonSaveA:

                try {
                    account = fillAccountWithInput();
                    editTextNameA.requestFocus();
                    // we want know when we are in edit mode
                    if (editIndex != -1) {
                        databaseManager.findIdFromCursorPosition(cursor, editIndex);
                        databaseManager.updateTableAccount(databaseManager.makeContentValue(account),
                                DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                                new String[]{String.valueOf(cursor.getInt(0))});
                        editIndex = -1;
                    }
                    else
                    databaseManager.insertInTable(databaseManager.makeContentValue(account), DatabaseSchema.TABLE_NAME_ACCOUNT.getValue());

                    fillCursorAccountAdapter();

                    Toast.makeText(this, "add one row in account1 table", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (Exception e) { startAlert(e.getMessage());
                }
                break;

            case R.id.imageButtonCancelC:
                dialog.dismiss();
                break;

            case R.id.imageButtonSaveC:

                try {
                    category = fillCategoryWithInput();
                    editTextNameC.requestFocus();
                    if (editIndex != -1) {
                        databaseManager.findIdFromCursorPosition(cursor, editIndex);
                        databaseManager.updateTableCategory(databaseManager.makeContentValue(category),
                                DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),new String[]{String.valueOf(cursor.getInt(0))});
                        editIndex = -1;
                    }
                    databaseManager.insertInTable(databaseManager.makeContentValue(category),
                            DatabaseSchema.TABLE_NAME_CATEGORY.getValue());

                    fillCursorCategoryAdapter();

                    Toast.makeText(this, "add one row in category table", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (Exception e) {startAlert(e.getMessage());
                }
                break;
        }
    }

    /**
     * fill and clean popup
     */

    private void cleanPopupAccount() {
        editTextBankNameA.setText("");
        editTextNumberA.setText("");
        editTextBalanceA.setText("");
        editTextNameA.setText("");
    }

    private void cleanPopupCategory() {
        editTextNameC.setText("");
    }

    /**
     * fills
     */
    private Account fillAccountWithInput(){
        Account account = new Account();
        account.setAccountName(editTextNameA.getText().toString());
        account.setAccountNumber(editTextNumberA.getText().toString());
        account.setBankName(editTextBankNameA.getText().toString());
        account.setBalance(Double.valueOf(editTextBalanceA.getText().toString().replace("$", "")));
        return account;
    }

    private Category fillCategoryWithInput() {
        Category category= new Category();
        category.setCategoryName(editTextNameC.getText().toString());
        return category;
    }


    private void fillPopupAccount() {
        account = databaseManager.fillAccountWithCursor(cursor,editIndex);
        editTextBankNameA.setText(account.getBankName());
        editTextNumberA.setText(account.getAccountNumber());
        editTextBalanceA.setText(String.valueOf(account.getBalance()));
        editTextNameA.setText(account.getAccountName());
    }

    private void fillPopupCategory() {
        category = databaseManager.fillCategoryWithCursor(cursor,editIndex);
        editTextNameC.setText(category.getCategoryName());
    }


    /**
     * ***************************************************
     *
     * fill coursors Adapter
     *
     * ***************************************************
     */
    private void fillCursorAccountAdapter() {
        cursor = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                DatabaseSchema.ACCOUNT_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.four_text_cell,
                cursor,
                new String[]{DatabaseSchema.BANK_NAME.getValue(), DatabaseSchema.ACCOUNT_NAME.getValue(),
                        DatabaseSchema.ACCOUNT_NUMBER.getValue(), DatabaseSchema.BALANCE.getValue()},
                new int[]{R.id.textViewUpLeftCell, R.id.textViewUpMiddelCell,
                        R.id.textViewLeftDownCell, R.id.textViewRightCell}, 1
        );
        swipeMenuListViewAC.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

    }

    private void fillCursorCategoryAdapter() {

        cursor = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.one_text_cell,
                cursor,
                new String[]{DatabaseSchema.CATEGORY_NAME.getValue()},
                new int[]{R.id.textViewCell}, 1
        );
        swipeMenuListViewAC.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

    }

    private void deleteFromBudget(int id, int positionInDatabase) {
        cursorForDelete = databaseManager.querySelectAll();
        cursorForDelete.moveToFirst();
        for (int i = 0; i < cursorForDelete.getCount(); i++) {
            if (cursorForDelete.getInt(positionInDatabase)==id) {
                databaseManager.deleteRowTableBudget(new String[]{
                        String.valueOf(cursorForDelete.getInt(0))});

            }

            cursor.moveToNext();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (textViewtitleAC.getText().equals("Account list")) {
            if (editTextBalanceA.isFocused()) {
                editTextBalanceA.removeTextChangedListener(this);
                current = s.toString().replace("$", "");
                current = current + "$";
                editTextBalanceA.setText(current);
                editTextBalanceA.setSelection(current.toString().length() - 1);
                editTextBalanceA.addTextChangedListener(this);
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
