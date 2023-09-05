package com.example.cryptotrade;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    TextView txtCoinName;
    private EditText searchBar;
    private RecyclerView cryptoRV;
    private ProgressBar progressBar;
    private ArrayList<CryptoModel> cryptoModelArrayList;
    private CryptoRVAdapter cryptoRVAdapter;
    private Spinner spinnerListSize;
    String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchBar = view.findViewById(R.id.edit_search);
        cryptoRV = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        setUpSpinner(view);


        cryptoRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        cryptoModelArrayList = new ArrayList<>();
        cryptoRVAdapter = new CryptoRVAdapter(cryptoModelArrayList, getActivity());
        cryptoRV.setAdapter(cryptoRVAdapter);
        cryptoRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        getCryptoData(url);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCrypto(editable.toString()
                );
            }
        });

        return view;
    }

    private void filterCrypto(String cryptoName){
        ArrayList<CryptoModel> filteredList = new ArrayList<>();
        for(CryptoModel item : cryptoModelArrayList){
            if(item.getCoinName().toLowerCase().contains(cryptoName.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getActivity(), "No cryptocurrency found with that name!", Toast.LENGTH_SHORT).show();
        } else {
            cryptoRVAdapter.filterList(filteredList);
        }
    }

    private void getCryptoData(String url){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        cryptoModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject dataObj = response.getJSONObject(i);
                        String imageUrl = dataObj.getString("image");
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");
                        double price = dataObj.getDouble("current_price");
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
                Toast.makeText(getActivity(), "Data extraction failed!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);


    }

    private void setUpSpinner(View view){
        spinnerListSize = view.findViewById(R.id.list_size_spinner);

        String listSizes[] = getResources().getStringArray(R.array.list_size);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_size,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSize.setAdapter(spinnerAdapter);

        spinnerListSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String size = adapterView.getItemAtPosition(i).toString();
                url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page="+size+"&page=1&sparkline=false&locale=en";
                getCryptoData(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}