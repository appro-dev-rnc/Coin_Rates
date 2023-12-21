package com.approdevelopers.coinrates.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.coinrates.Models.CoinsDataList;
import com.approdevelopers.coinrates.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class RatesLiveAdapter extends RecyclerView.Adapter<RatesLiveAdapter.RatesLiveViewHolder> {

    private final Map<String, Double> ratesMap;
    private final Map<String, CoinsDataList.CryptoItem> cryptoDataMap;

    public RatesLiveAdapter(Map<String, Double> ratesMap, Map<String, CoinsDataList.CryptoItem > cryptoDataMap) {
        this.ratesMap = ratesMap;
        this.cryptoDataMap = cryptoDataMap;
    }

    @NonNull
    @Override
    public RatesLiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.rates_item,parent,false);

        return new RatesLiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatesLiveViewHolder holder, int position) {
        String currencyCode = new ArrayList<>(ratesMap.keySet()).get(position);
        Double rate = ratesMap.get(currencyCode);

        if (cryptoDataMap!=null){
            CoinsDataList.CryptoItem cryptoItem = cryptoDataMap.get(currencyCode);

            Glide.with(holder.itemView.getContext()).load(cryptoItem.getIcon_url()).error(R.drawable.ic_block).into(holder.imgSymbol);
            holder.txtCurrencyName.setText(cryptoItem.getName());


        }

        // Bind data to ViewHolder components
        // For example, set text to a TextView
        holder.txtCurrencyCode.setText(currencyCode);
        String exchangeRate = formatExchangeRate(rate);
        holder.txtExchangeRate.setText(exchangeRate);


    }

    @Override
    public int getItemCount() {
        return ratesMap.size();
    }

    public static class RatesLiveViewHolder extends RecyclerView.ViewHolder{

        ImageView imgSymbol;
        TextView txtCurrencyCode,txtExchangeRate,txtCurrencyName;

        public RatesLiveViewHolder(@NonNull View itemView) {
            super(itemView);

            //ui hooks
            imgSymbol = itemView.findViewById(R.id.img_rates_currency_symbol);
            txtCurrencyCode = itemView.findViewById(R.id.txt_currency_code);
            txtExchangeRate = itemView.findViewById(R.id.txt_exchange_rate);
            txtCurrencyName = itemView.findViewById(R.id.txt_currency_name);
        }
    }

    public static String formatExchangeRate(double exchangeRate) {
        // Define a pattern for formatting with 6 decimal places
        String pattern = "0.######"; // 6 decimal places

        // Create a DecimalFormat object with the specified pattern
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        // Format the exchange rate using the DecimalFormat
        return decimalFormat.format(exchangeRate);
    }

}
