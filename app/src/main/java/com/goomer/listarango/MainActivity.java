package com.goomer.listarango;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    JSONArray listaTempRestaurantes, listaRestaurantes, listaMenu;
    AutoCompleteTextView txtRestaurantes;
    MyAdapter meuAdapter;

    Handler handler = new Handler();
    Runnable runnable;


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


        final int interval = 60000; // 1 minuto

        runnable = new Runnable() {
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                try {
//                    Restaurante restaurante = (Restaurante) listaRestaurantes.get(position);
                    JSONObject restaurante = (JSONObject) listaRestaurantes.get(position);


//                    String item = "Selecionado Restaurante " + restaurante.name;
                    String item = "Selecionado Restaurante " + restaurante.getString("name");

                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();


                    abreRestaurante(restaurante);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        timerAtualiza();


    }

    //abre a tela do Restaurante, com menu e detalhes do estabelecimento
    private void abreRestaurante(JSONObject restaurante) {


        paraTimer();

        Intent intent = new Intent(getBaseContext(), RestauranteActivity.class);
        intent.putExtra("RESTAURANTE", restaurante.toString());
        startActivity(intent);
    }

    private void paraTimer() {




    }


    boolean pegaDados() {


        boolean retorno = true;

        try {

            ObtemDadosAPI tester = new ObtemDadosAPI(MainActivity.this);

            //se o retorno veio true, foi pq a funcao rodou certinho.. :)
            listaRestaurantes = tester.pegaDadosRestaurantesMenu();

         } catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        }

        return retorno;

    }



    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
