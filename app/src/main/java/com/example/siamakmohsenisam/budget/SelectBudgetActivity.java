package com.example.siamakmohsenisam.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SelectBudgetActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonAddIncomeB, imageButtonAddExpenseB, imageButtonTransferB;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_budget);

        initialize();
    }

    private void initialize() {

        imageButtonAddExpenseB = (ImageButton) findViewById(R.id.imageButtonAddExpenseB);
        imageButtonAddIncomeB = (ImageButton) findViewById(R.id.imageButtonAddIncomeB);
        imageButtonTransferB = (ImageButton) findViewById(R.id.imageButtonTransferB);

        imageButtonAddIncomeB.setOnClickListener(this);
        imageButtonTransferB.setOnClickListener(this);
        imageButtonAddExpenseB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imageButtonAddExpenseB:

                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("tag","Add expense");
                startActivity(intent);
                break;

            case R.id.imageButtonAddIncomeB:

                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("tag","Add income");
                startActivity(intent);

                break;

            case R.id.imageButtonTransferB:

                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("tag","Add transfer");
                startActivity(intent);
                break;
        }
    }
}
