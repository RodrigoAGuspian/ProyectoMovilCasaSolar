package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[] mListDataHeader;
    private HashMap<String, String[]> mListDataChild;

    public ExpandableListAdapter(Context context, String[] mListDataHeader, HashMap<String, String[]> mListDataChild) {
        this.context = context;
        this.mListDataHeader = mListDataHeader;
        this.mListDataChild = mListDataChild;
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader[groupPosition]).length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader[groupPosition])[childPosition];

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
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_group,null);
            TextView txtListHeader = convertView.findViewById(R.id.txtListHeader);
            txtListHeader.setText(headerTitle);
            txtListHeader.setTypeface(null, Typeface.BOLD);

        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition,childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_child,null);

        }

        TextView txtItem= convertView.findViewById(R.id.txtItemChild);
        txtItem.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
