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
 * Custom Adapter class to handle the methods for the expandable list view.
 */
class MainMenuExpListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private HashMap<String, List<String>> menuItems;
    private List<String> itemsList;

    /**
     * Constructor method.
     *
     * @param context the activity context.
     * @param menuItems hashmap of items to list.
     * @param itemsList arraylist of individual items.
     */
    MainMenuExpListAdapter(Activity context, HashMap<String, List<String>> menuItems, List<String> itemsList){
        this.context = context;
        this.menuItems = menuItems;
        this.itemsList = itemsList;
    }

    /**
     * Getter method to obtain size of itemsList.
     *
     * @return size of itemsList.
     */
    @Override
    public int getGroupCount() {
        return itemsList.size();
    }

    /**
     * Getter method to obtain size of the group passed to it.
     *
     * @param groupPosition the group to check size of.
     * @return size of the group.
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return menuItems.get(itemsList.get(groupPosition)).size();
    }

    /**
     * Method to obtain a particular group from within the expandable list view.
     *
     * @param groupPosition the position of the group title element.
     * @return the position of the group.
     */
    @Override
    public Object getGroup(int groupPosition) {
        return itemsList.get(groupPosition);
    }

    /**
     * Method to obtain the a single element in the list view.
     *
     * @param groupPosition the group within which the child resides.
     * @param childPosition the position of the child within the group.
     * @return the child object.
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menuItems.get(itemsList.get(groupPosition)).get(childPosition);
    }

    /**
     * Method to obtain the ID of a group in the list view.
     *
     * @param groupPosition
     * @return the group ID.
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Method to obtain the ID of a child in the list view.
     *
     * @param groupPosition the group within which the child resides.
     * @param childPosition the position of the child within the group.
     * @return the ID of the child object.
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Boolean method to indicate unstable IDs.
     *
     * @return boolean false value.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Method to get the view of a group within the list view.
     *
     * @param groupPosition the position of the group in the list view.
     * @param isExpanded boolean value to indicate expanded or not.
     * @param convertView a view within which to place the group view (if empty).
     * @param parent the parent of the group.
     * @return the inflated convertView.
     */
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

    /**
     * Method to get the view of a group within the list view.
     * @param groupPosition the position of the group in the list view.
     * @param childPosition the position of the child within the group.
     * @param isLastChild boolean value to indicate is child is last in the group.
     * @param convertView a view within which to place the child.
     * @param parent parent of the child.
     * @return the inflated convertView.
     */
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

    /**
     * Method to indicate child items in the list view are selectable.
     *
     * @param groupPosition the position of the group within which the child resides.
     * @param childPosition the position of the child within the group.
     *
     * @return boolean true value, to indicate selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
