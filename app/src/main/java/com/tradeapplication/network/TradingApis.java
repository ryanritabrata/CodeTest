package com.tradeapplication.network;

import com.tradeapplication.responses.WalletCoinModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TradingApis {

    //Coin market cap API
    @Headers("Connection: close")
    @POST("v1/ticker?")
    Call<List<WalletCoinModel>> getCoinsOfCoinMarketCap(@Query("convert") String convert, @Query("limit") int limit, @Query("start") int start);

}