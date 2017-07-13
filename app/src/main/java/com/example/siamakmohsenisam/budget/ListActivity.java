package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
        SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, TextWatcher {

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

        account = new Account();
        category = new Category();

        swipeMenuListViewAC = (SwipeMenuListView) findViewById(R.id.swipeMenuListViewAC);
        textViewAddAC = (TextView) findViewById(R.id.textViewAddAC);
        textViewtitleAC = (TextView) findViewById(R.id.textViewtitleAC);

        textViewAddAC.setOnClickListener(this);
        swipeMenuListViewAC.setMenuCreator(this);
        swipeMenuListViewAC.setOnMenuItemClickListener(this);

    }

    private void initializeDialogAccount() {

        fillCursorAccount();

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

        fillCursorCategory();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.category_popup);

        editTextNameC = (EditText) dialog.findViewById(R.id.editTextNameC);
        imageButtonSaveC = (ImageButton) dialog.findViewById(R.id.imageButtonSaveC);
        imageButtonCancelC = (ImageButton) dialog.findViewById(R.id.imageButtonCancelC);

        imageButtonCancelC.setOnClickListener(this);
        imageButtonSaveC.setOnClickListener(this);

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
                    deleteFromBudget(findCursor(position),2);
                    databaseManager.deleteRowTableAccount(new String[]{
                            String.valueOf(findCursor(position))
                    });
                    fillCursorAccount();
                }
                if (textViewtitleAC.getText().equals("Category list")) {
                    deleteFromBudget(findCursor(position),1);
                    databaseManager.deleteRowTableCategory(new String[]{
                            String.valueOf(findCursor(position))
                    });
                    fillCursorCategory();
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
                    account.setAccountName(editTextNameA.getText().toString());
                    account.setAccountNumber(editTextNumberA.getText().toString());
                    account.setBankName(editTextBankNameA.getText().toString());
                    account.setBalance(Double.valueOf(editTextBalanceA.getText().toString().replace("$", "")));
                    editTextNameA.requestFocus();
                    // we want know when we are in edit mode
                    if (editIndex != -1) {
                        databaseManager.deleteRowTableAccount(new String[]{
                                String.valueOf(findCursor(editIndex))
                        });
                        editIndex = -1;
                    }
                    databaseManager.insertInTable(databaseManager.makeContentValue(account), DatabaseSchema.TABLE_NAME_ACCOUNT.getValue());

                    fillCursorAccount();

                    Toast.makeText(this, "add one row in account table", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (Exception e) {
                }
                break;

            case R.id.imageButtonCancelC:
                dialog.dismiss();
                break;

            case R.id.imageButtonSaveC:

                try {
                    category.setCategoryName(editTextNameC.getText().toString());
                    editTextNameC.requestFocus();
                    if (editIndex != -1) {
                        databaseManager.deleteRowTableCategory(new String[]{
                                String.valueOf(findCursor(editIndex))
                        });
                        editIndex = -1;
                    }
                    databaseManager.insertInTable(databaseManager.makeContentValue(category),
                            DatabaseSchema.TABLE_NAME_CATEGORY.getValue());

                    fillCursorCategory();

                    Toast.makeText(this, "add one row in category table", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (Exception e) {
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

    private void fillPopupAccount() {
        fillAccount(editIndex);
        editTextBankNameA.setText(account.getBankName());
        editTextNumberA.setText(account.getAccountNumber());
        editTextBalanceA.setText(String.valueOf(account.getBalance()));
        editTextNameA.setText(account.getAccountName());
    }

    private void fillPopupCategory() {
        fillCategory(editIndex);
        editTextNameC.setText(category.getCategoryName());
    }

    private void fillAccount(int position) {
        cursor.moveToFirst();
        for (int i = 0; i < position; i++)
            cursor.moveToNext();
        account.setAccountNumber(cursor.getString(4));
        account.setBalance(cursor.getDouble(3));
        account.setBankName(cursor.getString(2));
        account.setAccountName(cursor.getString(1));
    }

    private void fillCategory(int position) {
        cursor.moveToFirst();
        for (int i = 0; i < position; i++)
            cursor.moveToNext();
        category.setCategoryName(cursor.getString(1));
    }

    /**
     * find coursor
     *
     * @param position
     * @return
     */
    private int findCursor(int position) {
        cursor.moveToFirst();
        for (int i = 0; i < position; i++)
            cursor.moveToNext();
        return cursor.getInt(0);
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
    /**
     * fill coursors
     */
    private void fillCursorAccount() {
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

    private void fillCursorCategory() {

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
}
