package com.goomer.listarango;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RestauranteActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
    JSONObject menuTratado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);


        //obtem o restaurante
        JSONObject restaurante;


        TextView txtRestaurante = (TextView) findViewById(R.id.txtRestaurante);
        TextView txtEndereco = (TextView) findViewById(R.id.txtEndereco);
        TextView txtHorarios = (TextView) findViewById(R.id.txtHorarios);

        ImageView picture = (ImageView) findViewById(R.id.picture);


        try {
            restaurante = new JSONObject(getIntent().getStringExtra("RESTAURANTE"));

            txtRestaurante.setText(restaurante.getString("name"));
            txtEndereco.setText(restaurante.getString("address"));


            String horas = "";

            try {
                horas = restaurante.getString("hours");
            } catch (Throwable t) {
                Log.e("DEBUGGER", "pane1");

            }

            Restaurante r = new Restaurante(restaurante.getInt("id"), restaurante.getString("name"), restaurante.getString("address"), horas, restaurante.getString("image"));


            JSONArray menu = new JSONArray(restaurante.getString("menu"));

            menuTratado = trataMenu(menu);


            txtHorarios.setText(r.montaHorarios());


            try {
                Picasso.get().load(restaurante.getString("image")).into(picture);
            } catch (IllegalArgumentException e) {
            }


        } catch (Throwable t) {
            Log.e("DEBUGGER", "pane");
        }


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });


        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

    //monta o menu do restaurante com base nos grupos
    private void montaMenu(JSONObject menuTratado) {


        //para cada grupo de menu, monta uma accordion contendo os itens que lhe sao relativos.


    }

    private JSONObject trataMenu(JSONArray menu) {

        JSONObject retorno = new JSONObject();
        JSONArray templista = new JSONArray();

        //retorna um array com os produtos dentro de suas categorias
        for (int c = 0; c < menu.length(); c++) {


            try {

                //obtem o menu
                JSONObject itemmenu = menu.getJSONObject(c);

                //obtem o grupo do item do menu
                String grupo = itemmenu.getString("group");

                //obtem o array deste grupo com os items de menu ja adicionados
                try {


                    try {
                        //achou o grupo
                        templista = retorno.getJSONArray(grupo);

                        //adiciona o novo item de menu no seu grupo
                        templista.put(itemmenu);
                        retorno.put(grupo, templista);


                    } catch (JSONException e) {
                        //nao acho uo grupo
                        e.printStackTrace();

                        //adiciona o novo item de menu no seu grupo

                        JSONArray lista = new JSONArray();


                        lista.put(itemmenu);
                        retorno.put(grupo, lista);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return retorno;

    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<JSONObject>>();

        //montaMenu(menuTratado);


        Iterator<String> iter = menuTratado.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            listDataHeader.add(key);
            try {
                JSONArray itensgrupo = (JSONArray) menuTratado.get(key);

                List<JSONObject> lista = new ArrayList<JSONObject>();

                for (int c = 0; c < itensgrupo.length(); c++) {

                    JSONObject item = (JSONObject) itensgrupo.get(c);
                    lista.add(item);

                }

                listDataChild.put(key, lista); // Header, Child data


            } catch (JSONException e) {
                // Something went wrong!
            }
        }


    }
}
