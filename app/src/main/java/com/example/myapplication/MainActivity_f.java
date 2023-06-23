package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity_f extends AppCompatActivity {
    private EditText User_field;
    private TextView result_inf;
    private Button btn_ser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_f);

        User_field = findViewById(R.id.User_field);
        btn_ser = findViewById(R.id.btn_serch);
        result_inf = findViewById(R.id.res);

        btn_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User_field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity_f.this, R.string.wrng_user_inp, Toast.LENGTH_LONG).show();
                else {
                    String city = User_field.getText().toString();
                    String key = "8a941067003587801009146e1994d831";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",&appid=" + key + "&units=metric&lang=ru";

                    //noinspection deprecation
                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        @SuppressWarnings("deprecation")
        protected void onPreExecute() {
            super.onPreExecute();
            result_inf.setText("Waiting...");

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) !=null)
                    buffer.append(line).append("\n");

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection !=null)
                    connection.disconnect();

                try {
                    if (reader !=null)
                        reader.close();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject JsonObj = new JSONObject(result);

                result_inf.setText("Temp: " + JsonObj.getJSONObject("main").getDouble("temp") + "\n" + "Feels Like: " + JsonObj.getJSONObject("main").getDouble("feels_like") + "\n" +
                        "Temp MIN: " + JsonObj.getJSONObject("main").getDouble("temp_min") + "\n" + "Temp MAX: " + JsonObj.getJSONObject("main").getDouble("temp_max"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
