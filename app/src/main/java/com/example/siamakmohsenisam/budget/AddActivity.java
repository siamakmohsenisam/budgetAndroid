package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.database.Cursor;
import android.support.annotation.NonNull;
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
import com.example.siamakmohsenisam.budget.model.Budget;
import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;


public class AddActivity extends AppCompatActivity implements View.OnClickListener,
        CalendarView.OnDateChangeListener, TextWatcher , AdapterView.OnItemSelectedListener{

    ImageButton imageButtonDateI, imageButtonCancelI, imageButtonSaveI;
    Spinner spinnerDownI, spinnerUpI;
    TextView textViewDateI, textViewTitleI;
    EditText editTextAmounthI;

    Dialog dialog;
    CalendarView calendarViewPopup;
    DatabaseManager databaseManager;

    SimpleCursorAdapter simpleCursorAdapter;
    Budget budget;
    Cursor cursor;

    String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initialize();
    }

    private void initialize() {

        databaseManager = (DatabaseManager) getApplication();

        imageButtonDateI = (ImageButton) findViewById(R.id.imageButtonDateI);
        imageButtonCancelI = (ImageButton) findViewById(R.id.imageButtonCancelI);
        imageButtonSaveI = (ImageButton) findViewById(R.id.imageButtonSaveI);

        spinnerUpI = (Spinner) findViewById(R.id.spinnerDownI);
        spinnerDownI = (Spinner) findViewById(R.id.spinnerUpI);
        textViewDateI = (TextView) findViewById(R.id.textViewDateI);
        textViewTitleI = (TextView) findViewById(R.id.textViewTitleI);
        editTextAmounthI = (EditText) findViewById(R.id.editTextAmounthI);

        imageButtonDateI.setOnClickListener(this);
        imageButtonSaveI.setOnClickListener(this);
        imageButtonCancelI.setOnClickListener(this);
        spinnerDownI.setOnItemSelectedListener(this);
        spinnerUpI.setOnItemSelectedListener(this);


        budget = new Budget();

        textViewDateI.setText(budget.getStringDate());
        editTextAmounthI.addTextChangedListener(this);

        if (getIntent().getExtras()!=null){
            textViewTitleI.setText(getIntent().getStringExtra("tag"));
        }
        if (textViewTitleI.getText().equals("Add transfer")){
            fillCursorAccount(spinnerUpI);
            fillCursorAccount(spinnerDownI);
        }
        else {
            fillCursorAccount(spinnerDownI);
            fillCursorCategory(spinnerUpI);
        }
    }
    private void fillCursorAccount(Spinner spinner) {
        cursor = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_ACOUNNT.getValue(),
                DatabaseSchema.ACCOUNT_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.two_text_cell,
                cursor,
                new String[]{DatabaseSchema.BANK_NAME.getValue(),DatabaseSchema.ACCOUNT_NAME.getValue()},
                new int[]{R.id.textViewLeftCell,R.id.textViewRightCell},1
        );
        spinner.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    private void fillCursorCategory(Spinner spinner) {
        cursor = databaseManager.querySelectAll(DatabaseSchema.TABLE_NAME_CATEGORY.getValue(),
                DatabaseSchema.CATEGORY_COLUMNS.getValue().split(","));
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.one_text_cell,
                cursor,
                new String[]{DatabaseSchema.CATEGORY_NAME.getValue()},
                new int[]{R.id.textViewCell}, 1
        );

        spinner.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
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
                    budget.setDate(textViewDateI.getText().toString());
             //       budget.setAccount(spinnerUpI.get);
                }catch (Exception e){}

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
            editTextAmounthI.removeTextChangedListener(this);
            current = s.toString().replace("$", "");
            current = current+ "$";
            editTextAmounthI.setText(current);
            editTextAmounthI.setSelection(current.toString().length()-1);
            editTextAmounthI.addTextChangedListener(this);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
