package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Spinner spinner1,spinner2;
    Button button;
    EditText editText;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.amount);
        spinner1=findViewById(R.id.from);
        spinner2=findViewById(R.id.to);
        button=findViewById(R.id.convert);
        text=findViewById(R.id.result);
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.exchangeratesapi.io/latest";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful())
                {

                    String r=response.body().string();
                    final JsonObject jobj1= new Gson().fromJson(r,JsonObject.class);
                    JsonObject jobj2 =new JsonObject();
                    jobj2 = (JsonObject) jobj1.get("rates");
                    final JsonObject finalJobj = jobj2;
                    String[]currency={"CAD","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON","SEK","IDR","INR","BRL","RUB","HRK","JPY","THB","CHF","SGD","PLN","BGN","TRY","CNY","NOK","NZD","ZAR","USD","MXN","ILS","GBP","KRW","MYR"};
                    final Map<String,Double> c_price = new HashMap<String,Double>();
                    for (int i =0;i<currency.length;i++)
                    {
                         Double temp = jobj2.get(currency[i]).getAsDouble();
                         c_price.put(currency[i],temp);
                     }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Double amount=Double.parseDouble(editText.getText().toString());
                        Double k1= c_price.get(spinner1.getSelectedItem().toString());
                        Double k2= c_price.get(spinner2.getSelectedItem().toString());
                        BigDecimal res= BigDecimal.valueOf(0.0);
                        if (spinner1.getSelectedItem().toString()=="EUR"&&spinner2.getSelectedItem().toString()=="EUR")res=BigDecimal.valueOf(amount);
                        else if (spinner1.getSelectedItem().toString()=="EUR") res = BigDecimal.valueOf(amount*k2);
                        else if (spinner2.getSelectedItem().toString()=="EUR") res = BigDecimal.valueOf(amount/k1);
                        else res = BigDecimal.valueOf(amount/k1*k2);
                        res = res.setScale(2,BigDecimal.ROUND_HALF_DOWN);
                        text.setText(res.toString());
                        }
                    });
                }

            }
        });
        String[]currency={"EUR","CAD","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON","SEK","IDR","INR","BRL","RUB","HRK","JPY","THB","CHF","SGD","PLN","BGN","TRY","CNY","NOK","NZD","ZAR","USD","MXN","ILS","GBP","KRW","MYR"};
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,currency);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
    }
}