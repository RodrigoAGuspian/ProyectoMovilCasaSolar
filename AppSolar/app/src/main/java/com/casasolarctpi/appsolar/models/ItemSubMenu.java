package com.casasolarctpi.appsolar.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemSubMenu implements Parcelable {
    public final String name;

    public ItemSubMenu(String name) {
        this.name = name;
    }

    protected ItemSubMenu(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ItemSubMenu> CREATOR = new Creator<ItemSubMenu>() {
        @Override
        public ItemSubMenu createFromParcel(Parcel in) {
            return new ItemSubMenu(in);
        }

        @Override
        public ItemSubMenu[] newArray(int size) {
            return new ItemSubMenu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
