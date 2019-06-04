package com.goomer.listarango;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<JSONObject>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<JSONObject>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ImageView picture;

        String nomeitem = "";
        String descricao = "";
        String preco = "";
        String precopromocao = "";
        String image = "";
        try {
            nomeitem = ((JSONObject) getChild(groupPosition, childPosition)).getString("name");
//            descricao = ((JSONObject) getChild(groupPosition, childPosition)).getString("description");
            preco = ((JSONObject) getChild(groupPosition, childPosition)).getString("price");
            image = ((JSONObject) getChild(groupPosition, childPosition)).getString("image");
            //precopromocao = ((JSONObject) getChild(groupPosition, childPosition)).getString("sales");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        final String nomeitem = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txtNome);
        TextView txtPreco = (TextView) convertView
                .findViewById(R.id.txtPreco);

        TextView txtPrecoPromocao = (TextView) convertView
                .findViewById(R.id.txtPrecoPromocao);

        TextView txtDescricao = (TextView) convertView
                .findViewById(R.id.txtDescricao);

        picture = (ImageView) convertView.findViewById(R.id.picture);

        txtListChild.setText(nomeitem);
        txtPreco.setText(preco);
        txtPrecoPromocao.setText(precopromocao);
        txtDescricao.setText(descricao);


        try {
            Picasso.get().load(image).into(picture);
        } catch (IllegalArgumentException e) {
//            java.lang.IllegalArgumentException: Path must not be empty.
//                    at com.squareup.picasso.Picasso.load(Picasso.java:332)
//            at com.goomer.listarango.ExpandableListAdapter.getChildView(ExpandableListAdapter.java:91)
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}