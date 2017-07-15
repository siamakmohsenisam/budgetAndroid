package com.example.siamakmohsenisam.budget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.siamakmohsenisam.budget.model.DatabaseManager;
import com.example.siamakmohsenisam.budget.model.DatabaseSchema;

public class ListBudgetActivity extends AppCompatActivity implements SwipeMenuCreator,
        SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener {

    Button buttonIncomeLB, buttonExpenseLB, buttonTransferLB;

    SwipeMenuListView swipeMenuListViewLB;

    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    DatabaseManager databaseManager;

    Intent intent;

    int editIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_budget);

        initialize();
    }
    /**
     * ****************************************
     * <p>
     * All initialize
     * <p>
     * ****************************************
     */
    private void initialize() {

        databaseManager = (DatabaseManager) getApplication();

        swipeMenuListViewLB = (SwipeMenuListView) findViewById(R.id.swipeMenuListViewLB);
        buttonExpenseLB = (Button) findViewById(R.id.buttonExpenseLB);
        buttonIncomeLB = (Button) findViewById(R.id.buttonIncomeLB);
        buttonTransferLB = (Button) findViewById(R.id.buttonTransferLB);

        buttonExpenseLB.setOnClickListener(this);
        buttonIncomeLB.setOnClickListener(this);
        buttonTransferLB.setOnClickListener(this);
        swipeMenuListViewLB.setMenuCreator(this);
        swipeMenuListViewLB.setOnMenuItemClickListener(this);

        fillCursorBudgetAdapter();
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
                databaseManager.findIdFromCursorPosition(cursor,position);
                intent = new Intent(this,AddBudgetActivity.class);
                intent.putExtra("editIndex",editIndex);
                intent.putExtra("title","Edit budget");

                intent.putExtra("id",cursor.getInt(0));
                intent.putExtra("tagUp",cursor.getInt(2));
                intent.putExtra("tagDown",cursor.getInt(1));
                intent.putExtra("date",cursor.getString(3));
                intent.putExtra("amount",cursor.getDouble(4));
                startActivity(intent);
                finish();

                break;
            case 1:

                    databaseManager.deleteRowTableBudget(new String[]{
                            String.valueOf(databaseManager.findIdFromCursorPosition(cursor,position))
                    });
                    fillCursorBudgetAdapter();
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
            case R.id.buttonExpenseLB:
                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("title","Add expense");
                startActivity(intent);
                finish();
                break;
            case R.id.buttonIncomeLB:
                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("title","Add income");
                startActivity(intent);
                finish();
                break;
            case R.id.buttonTransferLB:
                intent = new Intent(this, AddBudgetActivity.class);
                intent.putExtra("title","Add transfer");
                startActivity(intent);
                finish();
                break;
        }
    }
    /**
     * fill coursors
     */
    private void fillCursorBudgetAdapter() {
        cursor = databaseManager.querySelectAll();
        simpleCursorAdapter = new
                SimpleCursorAdapter(this,
                R.layout.four_text_cell,
                cursor,
                new String[]{DatabaseSchema.ACCOUNT_NAME.getValue(), DatabaseSchema.CATEGORY_NAME.getValue(),
                        DatabaseSchema.BUDGET_DATE.getValue(), DatabaseSchema.AMOUNTH.getValue()},
                new int[]{R.id.textViewUpLeftCell, R.id.textViewUpMiddelCell,
                        R.id.textViewLeftDownCell, R.id.textViewRightCell}, 1
        );
        swipeMenuListViewLB.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.changeCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();

    }

}
