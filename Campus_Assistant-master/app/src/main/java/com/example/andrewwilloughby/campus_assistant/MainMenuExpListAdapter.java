package com.example.andrewwilloughby.campus_assistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andrewwilloughby on 17/10/2016.
 */

public class MainMenuExpListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private HashMap<String, List<String>> menuItems;
    private List<String> itemsList;

    public MainMenuExpListAdapter(Activity context, HashMap<String, List<String>> menuItems, List<String> itemsList){
        this.context = context;
        this.menuItems = menuItems;
        this.itemsList = itemsList;
    }

    @Override
    public int getGroupCount() {
        return itemsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menuItems.get(itemsList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menuItems.get(itemsList.get(groupPosition)).get(childPosition);
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
        String groupTitle = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_menu_list_group, parent, false);
        }

        TextView parentTextview = (TextView) convertView.findViewById(R.id.mainMenuGroupText);
        parentTextview.setTypeface(null, Typeface.BOLD);
        parentTextview.setText(groupTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_menu_list_sub, parent, false);
        }

        TextView childTextview = (TextView) convertView.findViewById(R.id.mainMenuSubText);
        childTextview.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
