package com.tradeapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tradeapplication.adapter.OrderListAdapter;
import com.tradeapplication.database.SqliteHelper;
import com.tradeapplication.responses.Order;

import java.util.ArrayList;

public class DetailPage extends AppCompatActivity implements View.OnClickListener {
    TextView buyLyt, sellLyt, avblBal;
    SqliteHelper db;
    Button btnReset, btnOrder;
    EditText qty, rate;
    String type = "buy";
    ArrayList<Order> arrayList = new ArrayList<>();
    RecyclerView recycler_view_orders;
    OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("coin").toUpperCase() + " TRADE");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new SqliteHelper(this);
        recycler_view_orders = findViewById(R.id.recycler_view_orders);
        adapter = new OrderListAdapter(arrayList, DetailPage.this);
        recycler_view_orders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        buyLyt = findViewById(R.id.buyLyt);
        sellLyt = findViewById(R.id.sellLyt);
        buyLyt.setOnClickListener(this);
        sellLyt.setOnClickListener(this);
        buyLyt.setTextColor(Color.WHITE);
        sellLyt.setTextColor(getResources().getColor(R.color.colordarkgrey));
        buyLyt.setBackground(getResources().getDrawable(R.drawable.red_bg));
        sellLyt.setBackground(getResources().getDrawable(R.drawable.white_bg));
        avblBal = findViewById(R.id.avblBal);
        avblBal.setText(Html.fromHtml("Avbl:" + getResources().getString(R.string.RswithPlus) + "10,000.00"));
        btnOrder = findViewById(R.id.btnOrder);
        btnReset = findViewById(R.id.btnReset);
        qty = findViewById(R.id.qty);
        rate = findViewById(R.id.rate);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setText("");
                rate.setText("");
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please enter Qty", Toast.LENGTH_LONG).show();
                    return;
                }

                if(rate.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please enter Rate", Toast.LENGTH_LONG).show();
                    return;
                }

                db.insertOrder(type, rate.getText().toString(), qty.getText().toString());
                qty.setText("");
                rate.setText("");
                arrayList.clear();
                arrayList = db.getOrders();
                System.out.println(arrayList.size());
                adapter.notifyDataSetChanged();
            }
        });

        arrayList.clear();
        arrayList = db.getOrders();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buyLyt) {
            buyLyt.setTextColor(Color.WHITE);
            sellLyt.setTextColor(getResources().getColor(R.color.colordarkgrey));
            buyLyt.setBackground(getResources().getDrawable(R.drawable.red_bg));
            sellLyt.setBackground(getResources().getDrawable(R.drawable.white_bg));
            avblBal.setText(Html.fromHtml("Avbl: " + getResources().getString(R.string.RswithPlus) + "10,000.00"));
            type = "buy";
        } else if (v.getId() == R.id.sellLyt) {
            sellLyt.setTextColor(Color.WHITE);
            buyLyt.setTextColor(getResources().getColor(R.color.colordarkgrey));
            buyLyt.setBackground(getResources().getDrawable(R.drawable.white_bg));
            sellLyt.setBackground(getResources().getDrawable(R.drawable.red_bg));
            avblBal.setText(Html.fromHtml("Avbl:" + "1.00000000 " + getIntent().getStringExtra("symbol")));
            type = "sell";
        }
    }
}
