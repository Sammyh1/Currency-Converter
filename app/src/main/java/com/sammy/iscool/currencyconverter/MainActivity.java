package com.sammy.iscool.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AQuery aq;
    private EditText currencyText;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView textFrom;
    private TextView textTo;
    private TextView textDisplay1;
    private TextView textDisplay2;
    private Button buttonConvert;
    private String base_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);

        currencyText = (EditText) findViewById(R.id.editText_currency);
        spinnerFrom = (Spinner) findViewById(R.id.spinner_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_to);
        textFrom = (TextView) findViewById(R.id.textView_from);
        textTo = (TextView) findViewById(R.id.textView_to);
        buttonConvert = (Button) findViewById(R.id.button_convert);
        base_url = "https://currency-api.appspot.com/api/";


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        spinnerFrom.setSelection(1);
        textDisplay1 = (TextView) findViewById(R.id.text_display_1);
        textDisplay2 = (TextView) findViewById(R.id.text_display_2);

        textDisplay1.setText(spinnerFrom.getSelectedItem().toString());
        textDisplay2.setText(spinnerTo.getSelectedItem().toString());

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textDisplay1.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textDisplay2.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }


                Double currency_from_value = Double.valueOf(currencyText.getText().toString());
                String currency_from = String.valueOf(spinnerFrom.getSelectedItem());
                String currency_to = String.valueOf(spinnerTo.getSelectedItem());
                String url = base_url + currency_from + "/" + currency_to + ".json?amount=" + currency_from_value;


                aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {


                        if (json != null) {

                            try {
                                Double amount = json.getDouble("amount");
                                String stringAmount = amount.toString();
                                Log.d("1st", stringAmount);
                                Log.d("string Amount", stringAmount.substring(stringAmount.length() - 2));
                                if (stringAmount.substring(stringAmount.length() - 2).contains(".0")) {
                                    stringAmount = stringAmount.substring(0, stringAmount.length() - 2);
                                    Log.d("2nd", stringAmount);
                                }
                                Log.d("3rd", stringAmount);


                                textFrom.setText(currencyText.getText().toString());
                                textTo.setText(stringAmount);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            //ajax error, show error code
                            Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }
}

