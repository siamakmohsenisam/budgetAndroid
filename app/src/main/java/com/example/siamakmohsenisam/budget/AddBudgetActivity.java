package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siamakmohsenisam.budget.model.Account;
import com.example.siamakmohsenisam.budget.model.Budget;
import com.example.siamakmohsenisam.budget.model.Category;
import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;


public class AddBudgetActivity extends AppCompatActivity implements View.OnClickListener,
        CalendarView.OnDateChangeListener, TextWatcher, AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener {

    ImageButton imageButtonDateI, imageButtonCancelI, imageButtonSaveI;
    Spinner spinnerDownI, spinnerUpI;
    TextView textViewDateI, textViewTitleI, textViewUpI, textViewDownI;
    EditText editTextAmountI;

    Dialog dialog;
    AlertDialog alertDialog;
    AlertDialog.Builder aBuilder;

    CalendarView calendarViewPopup;
    DatabaseManager databaseManager;

    SimpleCursorAdapter simpleCursorAdapter;
    Budget budget;
    Category category;
    Account account1, account2;
    Cursor cursorAccount1,cursorAccount2, cursorCategory;

    Intent intent;

    int idCategory = 0, idAccount1 = 0, idAccount2 = 0, idBudget = 0;
    String current = "";
    int idSpinnerUp = 0, idSpinnerDown = 0;
    double amountOld = 0;
    int categoryPosition =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        initialize();
    }

    private void initialize() {

        databaseManager = (DatabaseManager) getApplication();

        imageButtonDateI = (ImageButton) findViewById(R.id.imageButtonDateI);
        imageButtonCancelI = (ImageButton) findViewById(R.id.imageButtonCancelI);
        imageButtonSaveI = (ImageButton) findViewById(R.id.imageButtonSaveI);
        spinnerUpI = (Spinner) findViewById(R.id.spinnerUpI);
        spinnerDownI = (Spinner) findViewById(R.id.spinnerDownI);
        textViewDateI = (TextView) findViewById(R.id.textViewDateI);
        textViewTitleI = (TextView) findViewById(R.id.textViewTitleI);
        textViewUpI = (TextView) findViewById(R.id.textViewUpI);
        textViewDownI = (TextView) findViewById(R.id.textViewDownI);
        editTextAmountI = (EditText) findViewById(R.id.editTextAmountI);


        imageButtonDateI.setOnClickListener(this);
        imageButtonSaveI.setOnClickListener(this);
        imageButtonCancelI.setOnClickListener(this);
        spinnerDownI.setOnItemSelectedListener(this);
        spinnerUpI.setOnItemSelectedListener(this);
        editTextAmountI.addTextChangedListener(this);



        aBuilder = new AlertDialog.Builder(this);
        budget = new Budget();

        textViewDateI.setText(budget.getStringDate());

        textViewUpI.setText("Choose an account1 :");
        textViewDownI.setText("Choose a category :");

        if (getIntent().getExtras() != null) {
            textViewTitleI.setText(getIntent().getStringExtra("title"));

            if (textViewTitleI.getText().toString().equals("Edit budget")) {
                idBudget = getIntent().getIntExtra("id", 0);
                idSpinnerUp = getIntent().getIntExtra("tagUp", 0);
                idSpinnerDown = getIntent().getIntExtra("tagDown", 0);
                textViewDateI.setText(getIntent().getStringExtra("date"));
                amountOld = getIntent().getDoubleExtra("amount", 0.0);
                if (amountOld < 0)
                    editTextAmountI.setText(String.valueOf(amountOld * -1));
                else
                    editTextAmountI.setText(String.valueOf(amountOld));

            }
        }


        if (textViewTitleI.getText().equals("Add transfer")) {
            cursorAccount1= databaseManager.fillCursorAccountAdapter(simpleCursorAdapter,cursorAccount1,spinnerUpI);
            cursorAccount2= databaseManager.fillCursorAccountAdapter(simpleCursorAdapter,cursorAccount2,spinnerDownI);

            textViewUpI.setText("From :");
            textViewDownI.setText("To :");
        } else {

            cursorAccount1= databaseManager.fillCursorAccountAdapter(simpleCursorAdapter,cursorAccount1,spinnerUpI);
            cursorCategory= databaseManager.fillCursorCategoryAdapter(simpleCursorAdapter,cursorCategory,spinnerDownI);

            spinnerUpI.setSelection(databaseManager.findPositionFromCursorId(cursorAccount1, idSpinnerUp));
            spinnerDownI.setSelection(databaseManager.findPositionFromCursorId(cursorCategory, idSpinnerDown));
        }
    }

    private void startAlert(String title) {
        aBuilder.setTitle(title);
        aBuilder.setNegativeButton("Ok", this);
        aBuilder.setIcon(R.drawable.alert2);
        alertDialog = aBuilder.create();
        alertDialog.show();
    }

    private void fillBudgetFromInput(Account account) {
        budget.setDate(textViewDateI.getText().toString());
        budget.setAmount(Double.valueOf(editTextAmountI.getText().toString().replace("$", "")));
        budget.setAccount(account);
        budget.setCategory(category);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageButtonDateI:

                dialog = new Dialog(this);
                dialog.setContentView(R.layout.date_popup);

                calendarViewPopup = (CalendarView) dialog.findViewById(R.id.calendarViewPopup);
                calendarViewPopup.setOnDateChangeListener(this);
                dialog.show();

                break;

            case R.id.imageButtonSaveI:

                try {
                    if (textViewTitleI.getText().toString().equals("Add transfer")) {

                        category = new Category("Transfer");
                        cursorCategory = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                                DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));

                        categoryPosition = databaseManager.findPositionCategoryNameFromCursor(cursorCategory, "Transfer");
                        if (categoryPosition == -1) {
                            databaseManager.insertInTable(databaseManager.makeContentValue(category),
                                    DatabaseSchema.TABLE_NAME_CATEGORY.getValue());
                            cursorCategory = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                                    DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));
                            categoryPosition = databaseManager.findPositionCategoryNameFromCursor(cursorCategory, "Transfer");

                        }
                        idCategory = databaseManager.findIdFromCursorPosition(cursorCategory, categoryPosition);
                        fillBudgetFromInput(account1);
                        budget.setAmount(budget.getAmount() * -1);
                        saveInsertBudget(cursorAccount1,account1,idAccount1);
                        fillBudgetFromInput(account2);
                        saveInsertBudget(cursorAccount2,account2,idAccount2);


                    } else {
                        fillBudgetFromInput(account1);

                        if (textViewTitleI.getText().toString().equals("Edit budget")) {
                            if (amountOld < 0)
                                budget.setAmount(budget.getAmount() * -1);
                            saveEditBudget();
                        } else {
                            if (textViewTitleI.getText().toString().equals("Add expense"))
                                budget.setAmount(budget.getAmount() * -1);

                            saveInsertBudget(cursorAccount1,account1,idAccount1);

                            Toast.makeText(this, "one row was insert", Toast.LENGTH_LONG).show();
                        }
                    }
                    intent = new Intent(this, ListBudgetActivity.class);
                    startActivity(intent);

                    finish();

                } catch (Exception e) {
                    startAlert(e.getMessage());
                }

                break;
            case R.id.imageButtonCancelI:
                finish();
                break;
        }

    }

    private void saveInsertBudget(Cursor cursor, Account account,int idAccount) {
        databaseManager.insertInTable(databaseManager.makeContentValue(budget, new int[]{idAccount, idCategory}),
                DatabaseSchema.TABLE_NAME_BUDGET.getValue());

        account.setBalance(account.getBalance() + budget.getAmount());

        databaseManager.updateTableAccount(databaseManager.makeContentValue(account),
                DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                new String[]{String.valueOf(cursor.getInt(0))});
    }

    private void saveEditBudget() {

        databaseManager.updateTableBudget(databaseManager.makeContentValue(budget, new int[]{idAccount1, idCategory}),
                DatabaseSchema.TABLE_NAME_BUDGET.getValue(), new String[]{String.valueOf(idBudget)});

        account1.setBalance(account1.getBalance() - amountOld);
        account1.setBalance(account1.getBalance() + budget.getAmount());

        databaseManager.updateTableAccount(databaseManager.makeContentValue(account1),
                DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                new String[]{String.valueOf(cursorAccount1.getInt(0))});


        Toast.makeText(this, "one row was update", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        budget.setDate(year, month, dayOfMonth);
        textViewDateI.setText(budget.getStringDate());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.toString().getBytes()[s.toString().length() - 1] != '$') {
            editTextAmountI.removeTextChangedListener(this);
            current = s.toString().replace("$", "");
            current = current + "$";
            editTextAmountI.setText(current);
            editTextAmountI.setSelection(current.toString().length() - 1);
            editTextAmountI.addTextChangedListener(this);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner) parent;

        switch (spin.getId()) {
            case R.id.spinnerUpI:
                account1 = databaseManager.fillAccountWithCursor(cursorAccount1, position);
                idAccount1 = databaseManager.findIdFromCursorPosition(cursorAccount1, position);
                break;

            case R.id.spinnerDownI:
                if (textViewTitleI.getText().equals("Add transfer")) {
                    account2 = databaseManager.fillAccountWithCursor(cursorAccount2, position);
                    idAccount2 = databaseManager.findIdFromCursorPosition(cursorAccount2, position);
                } else {
                    category = databaseManager.fillCategoryWithCursor(cursorCategory, position);
                    idCategory = databaseManager.findIdFromCursorPosition(cursorCategory, position);
                }
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

}
