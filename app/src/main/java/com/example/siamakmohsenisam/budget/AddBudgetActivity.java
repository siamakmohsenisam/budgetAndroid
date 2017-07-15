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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siamakmohsenisam.budget.model.Account;
import com.example.siamakmohsenisam.budget.model.Budget;
import com.example.siamakmohsenisam.budget.model.Category;
import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;


public class AddBudgetActivity extends AppCompatActivity implements View.OnClickListener,
        CalendarView.OnDateChangeListener, TextWatcher , AdapterView.OnItemSelectedListener,DialogInterface.OnClickListener{

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
    Account account;
    Cursor cursorAccount, cursorCategory;

    Intent intent;

    int idCategory=0, idAccount1=0, idAccount2=0,idBudget=0;
    String current = "";
    int idSpinnerUp =0 , idSpinnerDown =0;
    double amountOld=0;

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

        aBuilder = new  AlertDialog.Builder(this);
        budget = new Budget();

        textViewDateI.setText(budget.getStringDate());

        textViewUpI.setText("Choose a account :");
        textViewDownI.setText("Choose a category :");

        if (getIntent().getExtras()!=null){
            textViewTitleI.setText(getIntent().getStringExtra("title"));
            if (textViewTitleI.getText().toString().equals("Edit budget")){
                idBudget = getIntent().getIntExtra("id",0);
                idSpinnerUp = getIntent().getIntExtra("tagUp",0);
                idSpinnerDown = getIntent().getIntExtra("tagDown",0);
                textViewDateI.setText(getIntent().getStringExtra("date"));
                amountOld = getIntent().getDoubleExtra("amount",0.0);
                if (amountOld<0)
                    editTextAmountI.setText(String.valueOf(amountOld*-1));
                else
                    editTextAmountI.setText(String.valueOf(amountOld));

            }
        }
        if (textViewTitleI.getText().equals("Add transfer")){
            fillCursorAccountAdapter(spinnerUpI);
            fillCursorAccountAdapter(spinnerDownI);
            textViewUpI.setText("From :");
            textViewDownI.setText("To :");
        }
        else {

            fillCursorAccountAdapter(spinnerUpI);
            fillCursorCategoryAdapter(spinnerDownI);

            spinnerUpI.setSelection(databaseManager.findPositionfromCursorId(cursorAccount,idSpinnerUp));
            spinnerDownI.setSelection(databaseManager.findPositionfromCursorId(cursorCategory,idSpinnerDown));
        }
    }
    private void startAlert(String title) {
        aBuilder.setTitle(title);
        aBuilder.setNegativeButton("Ok", this);
        aBuilder.setIcon(R.drawable.alert2);
        alertDialog = aBuilder.create();
        alertDialog.show();
    }
    private void fillBudgetFromInput(){
        budget.setDate(textViewDateI.getText().toString());
        budget.setAmount(Double.valueOf(editTextAmountI.getText().toString().replace("$","")));
        budget.setAccount(account);
        budget.setCategory(category);
    }

    private void fillCursorAccountAdapter(Spinner spinner) {
        cursorAccount = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                DatabaseSchema.ACCOUNT_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.two_text_cell,
                cursorAccount,
                new String[]{DatabaseSchema.BANK_NAME.getValue(),DatabaseSchema.ACCOUNT_NAME.getValue()},
                new int[]{R.id.textViewLeftCell,R.id.textViewRightCell},1
        );
        spinner.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursorAccount);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    private void fillCursorCategoryAdapter(Spinner spinner) {
        cursorCategory = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.one_text_cell,
                cursorCategory,
                new String[]{DatabaseSchema.CATEGORY_NAME.getValue()},
                new int[]{R.id.textViewCell}, 1
        );

        spinner.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursorCategory);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imageButtonDateI:

                dialog = new Dialog(this);
                dialog.setContentView(R.layout.date_popup);

                calendarViewPopup = (CalendarView) dialog.findViewById(R.id.calendarViewPopup);
                calendarViewPopup.setOnDateChangeListener(this);
                dialog.show();

                break;

            case R.id.imageButtonSaveI:

                try {

                    fillBudgetFromInput();

                    if (textViewTitleI.getText().toString().equals("Edit budget")){

                        if (amountOld < 0)
                            budget.setAmount(budget.getAmount()*-1);

                        databaseManager.updateTableBudget(databaseManager.makeContentValue(budget, new int[]{idAccount1, idCategory}),
                                DatabaseSchema.TABLE_NAME_BUDGET.getValue(),new String[]{String.valueOf(idBudget)});

                        account.setBalance(account.getBalance() - amountOld);
                        account.setBalance(account.getBalance() + Double.valueOf(budget.getAmount()));

                        databaseManager.updateTableAccount(databaseManager.makeContentValue(account),
                                DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                                new String[]{String.valueOf(cursorAccount.getInt(0))});


                        Toast.makeText(this, "one row was update", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (textViewTitleI.getText().toString().equals("Add expense"))
                            budget.setAmount(budget.getAmount()*-1);

                        databaseManager.insertInTable(databaseManager.makeContentValue(budget, new int[]{idAccount1, idCategory}),
                                DatabaseSchema.TABLE_NAME_BUDGET.getValue());

                        account.setBalance(account.getBalance() + Double.valueOf(budget.getAmount()));

                        databaseManager.updateTableAccount(databaseManager.makeContentValue(account),
                                DatabaseSchema.TABLE_NAME_ACCOUNT.getValue(),
                                new String[]{String.valueOf(cursorAccount.getInt(0))});

                        Toast.makeText(this, "one row was insert", Toast.LENGTH_LONG).show();
                    }

                    intent = new Intent(this,ListBudgetActivity.class);
                    startActivity(intent);

                    finish();

                }catch (Exception e){
                    startAlert(e.getMessage());}

                break;
            case R.id.imageButtonCancelI:
                finish();
                break;
        }

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        budget.setDate(year,month,dayOfMonth);
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

        if (s.toString().getBytes()[s.toString().length()-1]!= '$') {
            editTextAmountI.removeTextChangedListener(this);
            current = s.toString().replace("$", "");
            current = current+ "$";
            editTextAmountI.setText(current);
            editTextAmountI.setSelection(current.toString().length()-1);
            editTextAmountI.addTextChangedListener(this);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner)parent;

            switch (spin.getId()) {
                case R.id.spinnerUpI:
                    account = databaseManager.fillAccountWithCursor(cursorAccount,position);
                    idAccount1 = databaseManager.findIdFromCursorPosition(cursorAccount,position);
                    break;

                case R.id.spinnerDownI:
                    if (textViewTitleI.getText().equals("Add transfer")) {
                        account = databaseManager.fillAccountWithCursor(cursorAccount,position);
                        idAccount2 = databaseManager.findIdFromCursorPosition(cursorAccount,position);
                    }
                    else {
                        category = databaseManager.fillCategoryWithCursor(cursorCategory,position);
                        idCategory = databaseManager.findIdFromCursorPosition(cursorCategory,position);
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
