package com.example.cryptotrade;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoRVAdapter extends RecyclerView.Adapter<CryptoRVAdapter.ViewHolder> {
    private ArrayList<CryptoModel> cryptoModels;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public CryptoRVAdapter(ArrayList<CryptoModel> cryptoModels, Context context) {
        this.cryptoModels = cryptoModels;
        this.context = context;
    }

    public void filterList(ArrayList<CryptoModel> filteredList){
        cryptoModels = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CryptoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coin_rv_layout,parent, false);
        return new CryptoRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoRVAdapter.ViewHolder holder, int position) {
        CryptoModel cryptoModel = cryptoModels.get(position);
        holder.coinNameTV.setText(cryptoModel.getCoinName());
        holder.symbolTV.setText(cryptoModel.getSymbol());
        holder.valueTV.setText("$" + df2.format(cryptoModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return cryptoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView coinNameTV, symbolTV, valueTV;
        private ImageView coinImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinNameTV = itemView.findViewById(R.id.idTVcoinname);
            symbolTV = itemView.findViewById(R.id.idTVsymbol);
            valueTV = itemView.findViewById(R.id.idTVvalue);
        }
    }
}
