package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener
        , AdapterView.OnItemSelectedListener, TextWatcher, CompoundButton.OnCheckedChangeListener{


    TextView textViewFromDateR ,textViewToDateR ;
    ImageButton imageButtonFromDateR , imageButtonToDateR ;
    Spinner spinnerUpSearchR , spinnerDownSearchR ;
    ListView listViewR ;
    Switch switchDownR, switchUpR;

    Intent intent;
    Dialog dialog;
    CalendarView calendarViewUp, calendarViewDown;

    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursorBudget, cursorAccount , cursorCategory;

    DatabaseManager databaseManager;
    Boolean focuseUp=false;

    String from = "1900-01-01", to = "2500-01-01";
    int id1=-1,id2=-1, idAccount=1, idCategory=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialize();
    }

    private void initialize() {

        databaseManager = (DatabaseManager) getApplication();

        textViewFromDateR = (TextView) findViewById(R.id.textViewFromDateR);
        textViewToDateR = (TextView) findViewById(R.id.textViewToDateR);
        imageButtonFromDateR = (ImageButton) findViewById(R.id.imageButtonFromDateR);
        imageButtonToDateR = (ImageButton) findViewById(R.id.imageButtonToDateR);
        spinnerUpSearchR = (Spinner) findViewById(R.id.spinnerUpSearchR);
        spinnerDownSearchR = (Spinner) findViewById(R.id.spinnerDownSearchR);
        listViewR = (ListView) findViewById(R.id.listViewR);
        switchDownR = (Switch) findViewById(R.id.switchDownR);
        switchUpR = (Switch) findViewById(R.id.switchUpR);

        textViewFromDateR.addTextChangedListener(this);
        textViewToDateR.addTextChangedListener(this);
        imageButtonFromDateR.setOnClickListener(this);
        imageButtonToDateR.setOnClickListener(this);
        spinnerDownSearchR.setOnItemSelectedListener(this);
        spinnerUpSearchR.setOnItemSelectedListener(this);
        switchUpR.setOnCheckedChangeListener(this);
        switchDownR.setOnCheckedChangeListener(this);

        cleanTextViews();
        initializeCalendersDate();
        cursorBudget = databaseManager.fillCursorBudgetAdapter(simpleCursorAdapter,cursorBudget,listViewR);
        cursorAccount = databaseManager.fillCursorAccountAdapter(simpleCursorAdapter,cursorAccount,spinnerUpSearchR);
        cursorCategory = databaseManager.fillCursorCategoryAdapter(simpleCursorAdapter,cursorCategory,spinnerDownSearchR);

    }

    private void cleanTextViews() {
        textViewFromDateR.setText("");
        textViewToDateR.setText("");
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageButtonFromDateR:
                focuseUp = true;
                dialog.show();
                break;

            case R.id.imageButtonToDateR:
                focuseUp=false;
                dialog.show();

                break;

        }
    }
    public void initializeCalendersDate(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.date_popup);

        calendarViewDown = (CalendarView) dialog.findViewById(R.id.calendarViewPopup);
        calendarViewDown.setOnDateChangeListener(this);
        calendarViewUp = (CalendarView) dialog.findViewById(R.id.calendarViewPopup);
        calendarViewUp.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        String myDate="";
        myDate += String.valueOf(year)+"-";
        if (month<10)
            myDate += "0";
        myDate += (String.valueOf(month));
        myDate += "-";
        if (dayOfMonth<10)
            myDate += "0";
        myDate += (String.valueOf(dayOfMonth));

        if (focuseUp)

           textViewFromDateR.setText(myDate);
        else
            textViewToDateR.setText(myDate);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner) parent;

        switch (spin.getId()) {
            case R.id.spinnerUpSearchR:
                idAccount = databaseManager.findIdFromCursorPosition(cursorAccount, position);
                if (switchUpR.isChecked())
                    id1 = idAccount;
                else id1 = -1;
                break;

            case R.id.spinnerDownSearchR:
                idCategory = databaseManager.findIdFromCursorPosition(cursorCategory, position);
                if (switchDownR.isChecked())
                    id2 = idCategory;
                else id2 = -1;
                break;
        }

        cursorBudget = databaseManager.queryBudget(from, to, id1, id2);
        cursorBudget = databaseManager.fillCursorBudgetAdapter2(simpleCursorAdapter, cursorBudget, listViewR);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


        if (!textViewToDateR.getText().toString().isEmpty())
            to = textViewToDateR.getText().toString();

        if (!textViewFromDateR.getText().toString().isEmpty())
            from = textViewFromDateR.getText().toString();

        cursorBudget = databaseManager.queryBudget(from, to, id1, id2);
        cursorBudget = databaseManager.fillCursorBudgetAdapter2(simpleCursorAdapter,cursorBudget,listViewR);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.switchUpR:
                if (switchUpR.isChecked())
                    id1 = idAccount;
                else id1 = -1 ;
                break;
            case R.id.switchDownR:
                if (switchDownR.isChecked())
                    id2 = idCategory;
                else id2 = -1 ;
                break;

        }
        cursorBudget = databaseManager.queryBudget(from, to, id1, id2);
        cursorBudget = databaseManager.fillCursorBudgetAdapter2(simpleCursorAdapter,cursorBudget,listViewR);

    }
}
