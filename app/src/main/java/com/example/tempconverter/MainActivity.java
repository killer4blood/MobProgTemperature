package com.example.tempconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText cityname;
    Button celcius, fahrenheit;
    TextView temperature;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "8059ef60f7369253acc155d997c6f877";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname = findViewById(R.id.ETextCityName);
        celcius = findViewById(R.id.btnCelcius);
        fahrenheit = findViewById(R.id.btnFahrenheit);
        temperature = findViewById(R.id.TViewTemp);

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
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        //String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        //double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        //JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        //String countryName = jsonObjectSys.getString("country");
                        //String cityName = jsonResponse.getString("name");
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
        String[] result = temperature.getText().toString().split("\n", 5);
        if (result[0].contains("C")){
            Toast.makeText(getApplicationContext(), "Already in celcius", Toast.LENGTH_SHORT).show();
        } else {
            String fah = result[0].replaceAll("[^-?[0-9]{1,12}(?\\.[0-9]{1,4})?$]","");
            //String fah = temperature.getText().toString().replaceAll("[^-?[0-9]{1,12}(?:\\.[0-9]{1,4})?$]","");
            double degfah = Double.parseDouble(fah);
            double degcel = (degfah - 32) * (0.5556);
            //temperature.setText(df.format(degcel) + "°C");
            result[0] = " Temp           : " + df.format(degcel) + " °C\n";
            String output = TextUtils.join("", result);
            temperature.setText(output);
        }
    }

    public void toFahrenheit(View view) {
        String[] result = temperature.getText().toString().split("\n", 5);
        if (result[0].contains("F")){
            Toast.makeText(getApplicationContext(), "Already in fahrenheit", Toast.LENGTH_SHORT).show();
        } else {
            String cel = result[0].replaceAll("[^-?[0-9]{1,12}(?\\.[0-9]{1,4})?$]","");
            //String cel = temperature.getText().toString().replaceAll("[^-?[0-9]{1,12}(?:\\.[0-9]{1,4})?$]","");
            double degcel = Double.parseDouble(cel);
            double degfah = (degcel * (1.8)) + 32;
            //temperature.setText(df.format(degfah) + "°F");
            result[0] = " Temp           : " + df.format(degfah) + " °F\n";
            String output = TextUtils.join("", result);
            temperature.setText(output);
        }
    }
}