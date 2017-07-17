package com.example.siamakmohsenisam.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonAddBudgetM, imageButtonAddAccountM , imageButtonAddCategoryM ,
            imageButtonReportM;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {

        imageButtonAddAccountM = (ImageButton) findViewById(R.id.imageButtonAddAccountM);
        imageButtonAddBudgetM = (ImageButton) findViewById(R.id.imageButtonAddBudgetM);
        imageButtonAddCategoryM = (ImageButton) findViewById(R.id.imageButtonAddCategoryM);
        imageButtonReportM = (ImageButton) findViewById(R.id.imageButtonReportM);

        imageButtonAddAccountM.setOnClickListener(this);
        imageButtonReportM.setOnClickListener(this);
        imageButtonAddBudgetM.setOnClickListener(this);
        imageButtonAddCategoryM.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imageButtonAddBudgetM:

                intent = new Intent(this,ListBudgetActivity.class);
                startActivity(intent);

                break;

            case R.id.imageButtonAddAccountM:

                intent = new Intent(this,ListActivity.class);
                intent.putExtra("tag","Account list");
                startActivity(intent);

                break;

            case R.id.imageButtonAddCategoryM:

                intent = new Intent(this,ListActivity.class);
                intent.putExtra("tag","Category list");
                startActivity(intent);

                break;

            case R.id.imageButtonReportM:
                intent = new Intent(this,SearchActivity.class);
                startActivity(intent);

                break;



        }

    }
}
