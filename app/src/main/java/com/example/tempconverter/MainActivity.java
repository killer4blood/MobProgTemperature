package com.example.tempconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    public void getWeatherDetails(View view) {
        String tempURL = "";
        String city = cityname.getText().toString().trim();
        if (city.equals("")){
            Toast.makeText(getApplicationContext(), "City name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            tempURL = url + "?q=" + city + "&appid=" + appid;
            StringRequest stringrequest = new StringRequest(Request.Method.POST, tempURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonresponse = new JSONObject(response);
                        JSONObject jsonobjectmain = jsonresponse.getJSONObject("main");
                        double temp = jsonobjectmain.getDouble("temp") - 273.15; //this is to convert to celsius
                        temperature.setText(df.format(temp) + "°C");
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
}