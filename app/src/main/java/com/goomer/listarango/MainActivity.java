package com.goomer.listarango;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    JSONArray listaRestaurantes, listaMenu;
    AutoCompleteTextView txtRestaurantes;
    MyAdapter meuAdapter;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        configuraCoisas();


        //sucesso
        if (pegaDados()) {

            montaUI();

        } else {
            //aviso de erro

            avisaErro();
            timerTentaNovamente();

        }

    }

    private void configuraCoisas() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        // Get a reference to the AutoCompleteTextView in the layout
        txtRestaurantes = (AutoCompleteTextView) findViewById(R.id.txtRestaurantes);
        txtRestaurantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

            }
        });

    }

    private void timerAtualiza() {


        final int interval = 6000; // 1 minuto
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {


                meuAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                gridView.setAdapter(meuAdapter);


                Toast.makeText(MainActivity.this, "Atualizando horários dos estabelecimentos.", Toast.LENGTH_SHORT).show();


                timerAtualiza();
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis() + interval);
        handler.postDelayed(runnable, interval);


    }

    private void timerTentaNovamente() {


    }

    private void avisaErro() {
    }

    private void montaUI() {


        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);

        // Create the adapter and set it to the AutoCompleteTextView
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        txtRestaurantes.setAdapter(adapter);


        meuAdapter = new MyAdapter(this, listaRestaurantes);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(meuAdapter);


        timerAtualiza();


    }


    boolean pegaDados() {

        String urlRestaurantes = getString(R.string.urlRestaurates);
        String urlMenu = getString(R.string.urlMenu);

        boolean retorno = true;

        try {
            String restaurantes = run(urlRestaurantes);
            String menu = run(urlMenu);


            try {
                listaRestaurantes = new JSONArray(restaurantes);
                Log.d("DEBUGGER", listaRestaurantes.toString());

            } catch (Throwable t) {
                Log.e("DEBUGGER", "Could not parse malformed JSON: \"" + restaurantes + "\"");
                retorno = false;
            }


            try {
                listaMenu = new JSONArray(menu);
                Log.d("DEBUGGER", listaMenu.toString());

            } catch (Throwable t) {
                Log.e("DEBUGGER", "Could not parse malformed JSON: \"" + restaurantes + "\"");
                retorno = false;
            }


        } catch (IOException e) {
            e.printStackTrace();
            retorno = false;
        }

        return retorno;

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


}
