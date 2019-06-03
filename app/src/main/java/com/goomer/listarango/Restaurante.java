package com.goomer.listarango;

//classe gerada conforme ModelGenerator versão input manual....


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Restaurante {
    int id;
    String name = null;
    String address = null;
    String hours = null;
    String image = null;

    public void horarios() {
    }


    public void getMenu() {
    }

    private boolean verificaHorarioDentroLimite(String time, String starttime, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(starttime);
            Date date3 = sdf.parse(endtime);

            if (date1.after(date2) && date1.before(date3)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean verificaDia(int dia, JSONObject hora) {
        boolean retorno = false;


        try {
            JSONArray dias = hora.getJSONArray("days");

            for (int c = 0; c < dias.length(); c++) {
                if (dia == dias.getInt(c)) {
                    retorno = true;
                    break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return retorno;
    }

    public boolean estaAberto() {

        boolean retorno = false;

        //verifica se tem Hours

        try {

            //pra cada horario.. ve se ta no entre o horario inicial e horario final
            //ve se ta no dia...

            Calendar calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.DAY_OF_WEEK);


            Log.d("DEBUGGER", String.valueOf(dia));


            JSONArray listaHoras = new JSONArray(this.hours);


            for (int c = 0; c < listaHoras.length(); c++) {

                JSONObject hora = listaHoras.getJSONObject(c);

                String horaagora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE);

                if (verificaHorarioDentroLimite(horaagora, hora.getString("from"), hora.getString("to")) && verificaDia(dia, hora)) {
                    retorno = true;
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return retorno;
    }


    //Construtor default
    public Restaurante() {
    }

    //Construtor com os atributos
    public Restaurante(int id, String name, String address, String hours, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.image = image;
    }

}
