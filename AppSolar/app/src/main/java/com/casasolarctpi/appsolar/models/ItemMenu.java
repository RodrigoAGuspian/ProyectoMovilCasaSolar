package com.casasolarctpi.appsolar.models;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ItemMenu extends ExpandableGroup<ItemSubMenu> {
    private int index;
    public ItemMenu(String title, List<ItemSubMenu> items, int index) {
        super(title, items);
        this.index = index;
    }

    public ItemMenu(String title, List<ItemSubMenu> items) {
        super(title, items);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
