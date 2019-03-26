package com.casasolarctpi.appsolar.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casasolarctpi.appsolar.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MenuEAdapter extends ExpandableRecyclerViewAdapter<ItemMenuViewHolder,ItemSubMenuHolder> {
    private OnChildListener mOnChildListener;
    private OnClickListener mListener;
    public interface OnChildListener{
        void itemClick(String group, int child);
    }

    public interface OnClickListener{
        void itemClick(String n);
    }

    public void setmOnChildListener(OnChildListener mOnChildListener) {
        this.mOnChildListener = mOnChildListener;
    }

    public void setmListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    public MenuEAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ItemMenuViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_group,parent,false);
        return new ItemMenuViewHolder(view);
    }

    @Override
    public ItemSubMenuHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
        return new ItemSubMenuHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ItemSubMenuHolder holder, int flatPosition, final ExpandableGroup group, int childIndex) {
        final ItemSubMenu  itemSubMenu = (ItemSubMenu) group.getItems().get(childIndex);
        holder.setMlistener(new ItemSubMenuHolder.OnChildClickListener() {
            @Override
            public void itemClick(String g, int child) {
                if (mOnChildListener!=null){
                    mOnChildListener.itemClick(g,child);
                }
            }
        });
        holder.bind(itemSubMenu,group,childIndex);
    }

    @Override
    public void onBindGroupViewHolder(ItemMenuViewHolder holder, final int flatPosition, final ExpandableGroup group) {
        final ItemMenu itemMenu = (ItemMenu) group;
        holder.setMlistener(new ItemMenuViewHolder.OnParentClickListener() {
            @Override
            public void itemClick(String groupP) {
                if (mListener!=null){
                    mListener.itemClick(groupP);
                    mListener=null;
                }
            }
        });
        holder.bind(itemMenu,group);
    }



}
