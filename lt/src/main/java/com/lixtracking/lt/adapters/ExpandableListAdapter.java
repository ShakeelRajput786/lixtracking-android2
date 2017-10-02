package com.lixtracking.lt.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixtracking.lt.R;

import java.util.HashMap;
import java.util.List;


/**
 * Created by shakeel on 9/23/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader; //header title
    private HashMap<String,List<String>> listDataChild;

    public ExpandableListAdapter(Context context,List<String> listDataHeader,HashMap<String,List<String>> listDataChild){
        this.context=context;
        this.listDataHeader=listDataHeader;
        this.listDataChild=listDataChild;
    }
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        {
            return listDataChild.get(listDataHeader.get(groupPosition)).size();
        }


    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_new, null);
        }
        String data[]=childText.split("#:#");

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tvGPSID);

        TextView tvVehicleId = (TextView) convertView
                .findViewById(R.id.tvVehicleId);

        TextView tvSpeedId = (TextView) convertView
                .findViewById(R.id.tvSpeedId);
        TextView tvVin = (TextView) convertView
                .findViewById(R.id.tvVin);

        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);

        txtListChild.setText("GPS ID :"+data[0]);
        tvVehicleId.setText("Vehicle Id : "+data[1]);
        tvSpeedId.setText((data[2] .equals("0") ? "Speed: 0 km/h" : "Speed: " + data[2]));

        String status=data[3];
        tvVin.setText(data[4]);
        if(status.equals("1")){
                icon.setImageResource(R.drawable.car);
        }
        else{
            icon.setImageResource(R.drawable.car_na);
        }
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.scale_test);
        convertView.setAnimation(animation);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
