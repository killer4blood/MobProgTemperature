package com.example.tempconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        //search = findViewById(R.id.btnSearch);
        celcius = findViewById(R.id.btnCelcius);
        fahrenheit = findViewById(R.id.btnFahrenheit);
        temperature = findViewById(R.id.TViewTemp);

    }

    public void getWeatherDetails(View view) {
    }
}