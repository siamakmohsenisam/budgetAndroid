package com.example.siamakmohsenisam.budget;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.siamakmohsenisam.budget.model.Budget;

public class AddActivity extends AppCompatActivity implements View.OnClickListener,
        CalendarView.OnDateChangeListener, TextWatcher{

    ImageButton imageButtonDateI;
    Spinner spinnerCategoryI, spinnerAccountI;
    TextView textViewDateI, textViewTitleI;
    EditText editTextAmounthI;

    Dialog dialog;
    CalendarView calendarViewPopup;

    Budget budget;

    String s="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initialize();
    }

    private void initialize() {

        imageButtonDateI = (ImageButton) findViewById(R.id.imageButtonDateI);
        spinnerAccountI = (Spinner) findViewById(R.id.spinnerAccountI);
        spinnerCategoryI = (Spinner) findViewById(R.id.spinnerCategoryI);
        textViewDateI = (TextView) findViewById(R.id.textViewDateI);
        textViewTitleI = (TextView) findViewById(R.id.textViewTitleI);
        editTextAmounthI = (EditText) findViewById(R.id.editTextAmounthI);

        imageButtonDateI.setOnClickListener(this);

        budget = new Budget();

        textViewDateI.setText(budget.getStringDate());
        editTextAmounthI.addTextChangedListener(this);

        if (getIntent().getExtras()!=null){
            textViewTitleI.setText(getIntent().getStringExtra("tag"));
        }
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
editTextAmounthI.setText(s+"$");
    }
}
