package com.goomer.listarango;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

final class MyAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public MyAdapter(Context context, JSONArray lista) {
        mInflater = LayoutInflater.from(context);


        for (int c = 0; c < lista.length(); c++) {

            try {
                JSONObject restaurante = lista.getJSONObject(c);
                int id = restaurante.getInt("id");
                String name = restaurante.getString("name");
                String address = restaurante.getString("address");

                String hours = "";
                try {

                    hours = restaurante.getString("hours");
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                String image = restaurante.getString("image");

                Restaurante r = new Restaurante(id, name, address, hours, image);

                mItems.add(new Item(id, name, image, r));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;
        TextView endereco;
        TextView aberto;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.endereco, v.findViewById(R.id.endereco));
            v.setTag(R.id.aberto, v.findViewById(R.id.aberto));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        endereco = (TextView) v.getTag(R.id.endereco);
        aberto = (TextView) v.getTag(R.id.aberto);

        Item item = getItem(i);


        name.setText(item.name);
        endereco.setText(item.restaurante.address);

        if (item.restaurante.estaAberto())
            aberto.setText("ABERTO");
        else
            aberto.setText("FECHADO");

        Picasso.get().load(item.image).into(picture);

        return v;
    }

    private static class Item {
        public final Restaurante restaurante;
        public final int id;
        public final String name;
        public final String image;

        Item(int id, String name, String image, Restaurante restaurante) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.restaurante = restaurante;
        }
    }
}