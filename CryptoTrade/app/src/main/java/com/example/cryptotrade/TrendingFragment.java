package com.example.cryptotrade;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    private RecyclerView cryptoRV;
    private ProgressBar progressBar;
    private ArrayList<CryptoModel> cryptoModelArrayList;
    private CryptoRVAdapter cryptoRVAdapter;
    String url = "https://api.coingecko.com/api/v3/search/trending";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        cryptoRV = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        cryptoModelArrayList = new ArrayList<>();
        cryptoRVAdapter = new CryptoRVAdapter(cryptoModelArrayList, getActivity());
        cryptoRV.setAdapter(cryptoRVAdapter);
        cryptoRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        getCryptoData(url);

        return view;
    }

    private void getCryptoData(String url){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        cryptoModelArrayList.clear();
        JsonObjectRequest jsonObjectRequestRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray coinsArray = jsonObject.getJSONArray("coins");
                    for(int i=0; i<coinsArray.length(); i++){
                        JSONObject coinObj = coinsArray.getJSONObject(i);
                        JSONObject itemObj = coinObj.getJSONObject("item");
                        String name = itemObj.getString("name");
                        String symbol = itemObj.getString("symbol");
                        double price = itemObj.getDouble("price_btc");
                        cryptoModelArrayList.add(new CryptoModel(name, symbol, price));
                    }
                    cryptoRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to get JSON data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Main",error.toString());
                Toast.makeText(requireContext(), "Data extraction failed!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequestRequest);


    }


}