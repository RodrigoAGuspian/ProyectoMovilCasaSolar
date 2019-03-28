package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[] mListDataHeader;
    private HashMap<String, ChildClass[]> mListDataChild;
    private OnChildClickListener onChildClickListener;
    public interface OnChildClickListener{
        void childClick(int groupId, int childId);
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }

    public ExpandableListAdapter(Context context, String[] mListDataHeader, HashMap<String, ChildClass[]> mListDataChild) {
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
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildClass childClass = (ChildClass) getChild(groupPosition,childPosition);
        final String childText = childClass.getName();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_child,null);


        }

        TextView txtItem= convertView.findViewById(R.id.txtItemChild);
        txtItem.setText(childText);
        ImageView imgChild = convertView.findViewById(R.id.imgChild);
        try {
            if (childClass.getImage()==0){
                imgChild.setBackground(context.getResources().getDrawable(R.drawable.ic_link));
            }else {
                imgChild.setBackground(context.getResources().getDrawable(childClass.getImage()));
            }

        }catch (Exception ignored){
            imgChild.setBackground(context.getResources().getDrawable(R.drawable.ic_link));
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onChildClickListener!=null){
                    onChildClickListener.childClick(groupPosition,childPosition);
                }
            }
        });



        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
