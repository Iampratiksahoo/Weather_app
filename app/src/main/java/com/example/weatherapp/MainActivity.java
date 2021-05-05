package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity
{
    EditText userEditText;
    TextView resultTextView;

    String cityName;


    public void onSubmit(View view)
    {
        try {

            cityName = String.valueOf(userEditText.getText());

            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=4d7f0ef6170b1d400f4656eabfbea01a");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  // To hide keyboard
            inputMethodManager.hideSoftInputFromWindow(userEditText.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Could get Weather :( ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String, Void,String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try
            {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1)
                {
                    char current = (char)data;
                    result = result + current;

                    data = reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            try
            {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);
                String message = "";

                for(int i = 0; i < arr.length() ; i++)
                {
                    JSONObject jsonObject1 = arr.getJSONObject(i);

                    String main = jsonObject1.getString("main");
                    String description = jsonObject1.getString("description");

                    if(!main.equals("") && !description.equals(""))
                    {
                        message = message+  main + " : " + description +"\r\n";
                    }
                }

                if(!message.equals(""))
                {
                    resultTextView.setText(message);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Could get Weather :( ", Toast.LENGTH_SHORT).show();
                }


            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Could get Weather :( ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declaration
        userEditText = (EditText)findViewById(R.id.userEditText);
        resultTextView = (TextView)findViewById(R.id.resultTextView);



    }
}