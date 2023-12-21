package com.approdevelopers.coinrates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.coinrates.Adapters.RatesLiveAdapter;
import com.approdevelopers.coinrates.Models.CoinRatesResponse;
import com.approdevelopers.coinrates.Models.CoinsDataList;
import com.approdevelopers.coinrates.R;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //UI variables
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerCoins;
    private ProgressBar progressBar;
    private MaterialCardView materialLastUpdate;
    private TextView txtLastUpdated;


    //Okhttp client
    private OkHttpClient okHttpClient;

    // Constants
    private static final String baseLiveUrl  = " http://api.coinlayer.com/live?access_key=";
    private static final String baseDataListUrl  = "http://api.coinlayer.com/list?access_key=";

    private RatesLiveAdapter adapter;

    //Handler
    private final Handler refreshHandler = new Handler(Looper.getMainLooper());

    private Runnable refreshTask ;

    private Gson gson = new Gson();

    private Map<String, CoinsDataList.CryptoItem> cryptoDataList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //ui hooks
        refreshLayout= findViewById(R.id.refresh_layout);
        recyclerCoins = findViewById(R.id.recycler_coins);
        txtLastUpdated = findViewById(R.id.txt_last_updated_at);
        progressBar = findViewById(R.id.progress_bar);
        materialLastUpdate = findViewById(R.id.material_last_updated);

        recyclerCoins.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        okHttpClient = new OkHttpClient();

        //fetching list of currency and symbols
        fetchCoinsListData();
        progressBar.setVisibility(View.VISIBLE);


        refreshLayout.setOnRefreshListener(() -> {
            refreshHandler.removeCallbacks(refreshTask);
            fetchAndUpdateData();
            refreshHandler.post(refreshTask);
        });



        new Handler().postDelayed(() -> {
            fetchAndUpdateData();
            startAutoRefresh();
        },2000);


        recyclerCoins.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    materialLastUpdate.setVisibility(View.GONE);
                }else if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    materialLastUpdate.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void fetchAndUpdateData() {

        String url = baseLiveUrl+ getString(R.string.api_key);
        Request request = new Request.Builder().url(url)
                .build();
        
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Failed to load new data", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    CoinRatesResponse coinRatesResponse = gson.fromJson(jsonResponse,CoinRatesResponse.class);
                    Map<String, Double> ratesMap = coinRatesResponse.getRates();

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);

                        adapter = new RatesLiveAdapter(ratesMap,cryptoDataList);
                        recyclerCoins.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);
                        updateLastUpdateTime();

                    });

                }
            }
        });

    }

    // Update last update timestamp in the TextView
    private void updateLastUpdateTime() {
        // Get the current time and format it as a string
        long currentTimeInMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(currentTimeInMillis);
        String formattedDate = format.format(date);

        txtLastUpdated.setText(formattedDate);
    }


    // Start the auto-refresh initially
    private void startAutoRefresh() {
        refreshTask = new Runnable() {
            @Override
            public void run() {
                // Refresh the data
                fetchAndUpdateData();

                // Schedule the next refresh after 3 minutes
                refreshHandler.postDelayed(this, 3*60*1000);
            }
        };

        refreshHandler.post(refreshTask);
    }

    private void fetchCoinsListData() {

        String url = baseDataListUrl+ getString(R.string.api_key);
        Request request = new Request.Builder().url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                cryptoDataList = null;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String jsonResponse = response.body().string();
                    CoinsDataList dataList = gson.fromJson(jsonResponse,CoinsDataList.class);
                    cryptoDataList = dataList.getCrypto();
                }
            }
        });

    }

}