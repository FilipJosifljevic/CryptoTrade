package com.example.cryptotrade;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import kotlinx.coroutines.GlobalScope;

public class ExchangeFragment extends Fragment {

    String baseCurrency = "BTC";
    String convertedCurrency = "USD";
    double conversionRate = 0;
    EditText firstConversion, secondConversion;
    TextView txtCoinName, txtCoinSymbol, txtCoinDescription, txtCoinHashAlg;
    Spinner spinner1, spinner2;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        txtCoinName = view.findViewById(R.id.exchange_coin_name);
        txtCoinSymbol = view.findViewById(R.id.exchange_coin_symbol);
        txtCoinHashAlg = view.findViewById(R.id.exchange_coin_hash);
        txtCoinDescription = view.findViewById(R.id.exchange_coin_description);
        requestQueue = Volley.newRequestQueue(requireContext());

        spinnerSetup(view);
        textChanged(view);

        return view;
    }

    private void getCurrencyApi(){
        firstConversion = getView().findViewById(R.id.et_firstConversion);
        if(firstConversion != null && !firstConversion.getText().toString().isEmpty() && !firstConversion.getText().toString().trim().isEmpty()){

            String url = "https://api.coingecko.com/api/v3/simple/price?ids="+baseCurrency.toLowerCase()+"&vs_currencies="+convertedCurrency.toLowerCase();
            String urlDisplay = "https://api.coingecko.com/api/v3/coins/"+baseCurrency.toLowerCase();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            conversionRate = response.getJSONObject(baseCurrency.toLowerCase()).getDouble(convertedCurrency.toLowerCase());
                            requireActivity().runOnUiThread(() -> {
                                secondConversion = getView().findViewById(R.id.et_secondConversion);
                                double textval = Double.parseDouble(firstConversion.getText().toString()) * conversionRate;
                                secondConversion.setText(String.valueOf(textval));
                            });
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to get JSON data!", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Main",error.toString());
                        Toast.makeText(requireContext(), "Data extraction failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                JsonObjectRequest requestDisplay = new JsonObjectRequest(Request.Method.GET, urlDisplay, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String coinName = response.getString("name");
                            String coinSymbol = response.getString("symbol");
                            String coinDescription = response.getJSONObject("description").getString("en").substring(0,200);
                            String coinHashAlg = response.getString("hashing_algorithm");
                            txtCoinName.setText(coinName);
                            txtCoinSymbol.setText(coinSymbol);
                            if(coinHashAlg!=null) {
                                txtCoinHashAlg.setText(coinHashAlg);
                            } else {
                                txtCoinHashAlg.setText("No hashing algorithm");
                            }
                            txtCoinDescription.setText(coinDescription);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Coin data extraction failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(request);
                requestQueue.add(requestDisplay);
            }
        }

    private void spinnerSetup(View view){
        spinner1 = view.findViewById(R.id.spinner_firstConversion);
        spinner2 = view.findViewById(R.id.spinner_secondConversion);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currencies1,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currencies2,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                baseCurrency = adapterView.getItemAtPosition(i).toString();
                getCurrencyApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convertedCurrency = adapterView.getItemAtPosition(i).toString();
                getCurrencyApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void textChanged(View view){
        firstConversion = view.findViewById(R.id.et_firstConversion);
        firstConversion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    getCurrencyApi();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private static boolean containsAnyElement(String input, String[] array) {
        for (String element : array) {
            if (input.contains(element)) {
                return true;
            }
        }
        return false;
    }
}