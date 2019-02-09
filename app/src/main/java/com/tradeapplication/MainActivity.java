package com.tradeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.tradeapplication.adapter.ExchangeWalletListAdapter;
import com.tradeapplication.responses.WalletCoinModel;
import com.tradeapplication.network.RestAdapter;
import com.tradeapplication.network.TradingApis;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    ExchangeWalletListAdapter adapter;
    ArrayList<WalletCoinModel> coinsList = new ArrayList<>();
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    RelativeLayout parentLyt;
    int limit = 20;
    int start = 0;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recycler_view_exchange);
        adapter = new ExchangeWalletListAdapter(coinsList, MainActivity.this);

        swipeRefresh.setRefreshing(true);
        parentLyt = findViewById(R.id.parentLyt);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (loading) {
                    if (dy > 0) //check for scroll down
                    {
                        if (recyclerView.getLayoutManager() != null)
                            visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                        if (recyclerView.getLayoutManager() != null)
                            totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            loading = false;
                            page++;
                            start = page * limit;
                            makeRequestToGetCoins();
                        }

                    }
                }
            }
        });
        makeRequestToGetCoins();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                start = 0;
                coinsList.clear();
                makeRequestToGetCoins();
            }
        });


    }

    private void makeRequestToGetCoins() {
        TradingApis tradApis = RestAdapter.getApiService();
        Call<List<WalletCoinModel>> getInrTransactions = tradApis.getCoinsOfCoinMarketCap("INR", limit, start);
        getInrTransactions.enqueue(new Callback<List<WalletCoinModel>>() {
            @Override
            public void onResponse(Call<List<WalletCoinModel>> call, Response<List<WalletCoinModel>> response) {
                if ((response.code() == 200) || (response.code() == 201)) {
                    swipeRefresh.setRefreshing(false);
                    updateUIWithNewResponse(response.body());
                } else {
                    swipeRefresh.setRefreshing(false);
                    Snackbar.make(parentLyt, "Something Went Wrong", Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<WalletCoinModel>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Snackbar.make(parentLyt, "Server Not Responding", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void updateUIWithNewResponse(List<WalletCoinModel> body) {
        coinsList.addAll(body);
        adapter.notifyDataSetChanged();
        loading = true;
    }

    public void moveToDetailPage(int position) {
        Intent i = new Intent(MainActivity.this, DetailPage.class);
        i.putExtra("coin", coinsList.get(position).getName());
        i.putExtra("symbol", coinsList.get(position).getSymbol());
        i.putExtra("rate", coinsList.get(position).getPriceInr());
        startActivity(i);
    }

}
