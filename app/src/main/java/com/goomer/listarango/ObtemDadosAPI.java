package com.goomer.listarango;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObtemDadosAPI {
    JSONArray listaTempRestaurantes, listaRestaurantes, listaMenu;

    Context mCtx;

    OkHttpClient client = new OkHttpClient();


    public ObtemDadosAPI(Context mCtx) {
        this.mCtx = mCtx;
    }

    public boolean pegaDados() {

        String urlRestaurantes = this.mCtx.getString(R.string.urlRestaurates);
        String urlMenu = this.mCtx.getString(R.string.urlMenu);

        listaRestaurantes = new JSONArray();

        boolean retorno = true;

        try {
            String restaurantes = run(urlRestaurantes);
            String menu = run(urlMenu);


            try {
                listaTempRestaurantes = new JSONArray(restaurantes);
                Log.d("DEBUGGER", listaTempRestaurantes.toString());

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


            //para cada restaurante busca seu menu especifico e adiciona no modelo

            for (int c = 0; c < listaTempRestaurantes.length(); c++) {

                try {
                    JSONObject restaurante = (JSONObject) listaTempRestaurantes.get(c);
                    String urlMenuRestaurante = "https://challange.goomer.com.br/restaurants/" + restaurante.getString("id") + "/menu";
                    String menurestaurante = run(urlMenuRestaurante);


                    listaRestaurantes.put(restaurante.put("menu", menurestaurante));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
            retorno = false;
        }

        return retorno;

    }

    public JSONArray pegaDadosRestaurantesMenu() {

        String urlRestaurantes = this.mCtx.getString(R.string.urlRestaurates);

        listaRestaurantes = new JSONArray();

        boolean retorno = true;

        try {
            String restaurantes = run(urlRestaurantes);


            try {
                listaTempRestaurantes = new JSONArray(restaurantes);
                Log.d("DEBUGGER", listaTempRestaurantes.toString());

            } catch (Throwable t) {
                Log.e("DEBUGGER", "Could not parse malformed JSON: \"" + restaurantes + "\"");
                retorno = false;
            }

             //para cada restaurante busca seu menu especifico e adiciona no modelo

            for (int c = 0; c < listaTempRestaurantes.length(); c++) {

                try {
                    JSONObject restaurante = (JSONObject) listaTempRestaurantes.get(c);
                    String urlMenuRestaurante = "https://challange.goomer.com.br/restaurants/" + restaurante.getString("id") + "/menu";
                    String menurestaurante = run(urlMenuRestaurante);


                    listaRestaurantes.put(restaurante.put("menu", menurestaurante));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
            retorno = false;
        }

        return listaRestaurantes;

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
