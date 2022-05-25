package com.example.tempconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    SwitchCompat switchCompat;
    EditText cityname;
    Button celcius, fahrenheit;
    TextView temperature;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "8059ef60f7369253acc155d997c6f877";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else{
            setTheme(R.style.Theme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname = findViewById(R.id.ETextCityName);
        celcius = findViewById(R.id.btnCelcius);
        fahrenheit = findViewById(R.id.btnFahrenheit);
        temperature = findViewById(R.id.TViewTemp);
        switchCompat = findViewById(R.id.bt_switch);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }


            }
        });

    }

    public void getTemp(View view) {
        String tempURL = "";
        String city = cityname.getText().toString().trim();
        if (city.equals("")){
            Toast.makeText(getApplicationContext(), "City name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            tempURL = url + "?q=" + city + "&appid=" + appid;
            StringRequest stringrequest = new StringRequest(Request.Method.POST, tempURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        output += " Temp           : " + df.format(temp) + " °C\n"
                                + " Humidity     : " + humidity + "%\n"
                                + " Wind Speed: " + wind + "m/s\n"
                                + " Cloudiness : " + clouds + "%\n"
                                + " Pressure     : " + pressure + " hPa";
                        temperature.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringrequest);
        }
    }

    public void toCelsius(View view) {
        String[] result = temperature.getText().toString().split("\n", 2);
        if (result[0].contains("C")){
            Toast.makeText(getApplicationContext(), "Already in celcius", Toast.LENGTH_SHORT).show();
        } else if (result[0].equals("")){
            Toast.makeText(getApplicationContext(), "Input a city name and click 'Search'", Toast.LENGTH_SHORT).show();
        } else {
            String fah = result[0].replaceAll("[^-?[0-9]{1,12}(?\\.[0-9]{1,4})?$]","");
            double degfah = Double.parseDouble(fah);
            double degcel = (degfah - 32) * (0.5556);
            result[0] = " Temp           : " + df.format(degcel) + " °C\n";
            String output = TextUtils.join("", result);
            temperature.setText(output);
        }
    }

    public void toFahrenheit(View view) {
        String[] result = temperature.getText().toString().split("\n", 2);
        if (result[0].contains("F")){
            Toast.makeText(getApplicationContext(), "Already in fahrenheit", Toast.LENGTH_SHORT).show();
        } else if (result[0].equals("")){
            Toast.makeText(getApplicationContext(), "Input a city name and click 'Search'", Toast.LENGTH_SHORT).show();
        } else {
            String cel = result[0].replaceAll("[^-?[0-9]{1,12}(?\\.[0-9]{1,4})?$]","");
            double degcel = Double.parseDouble(cel);
            double degfah = (degcel * (1.8)) + 32;
            result[0] = " Temp           : " + df.format(degfah) + " °F\n";
            String output = TextUtils.join("", result);
            temperature.setText(output);
        }
    }
}