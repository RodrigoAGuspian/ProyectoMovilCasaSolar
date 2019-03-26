package com.casasolarctpi.appsolar.models;

import android.view.View;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ItemSubMenuHolder extends ChildViewHolder {
    TextView txtItemChild;
    private OnChildClickListener mlistener;
    public interface OnChildClickListener{
        void itemClick(String gruop, int child);
    }

    public void setMlistener(OnChildClickListener mlistener) {
        this.mlistener = mlistener;
    }

    public void setTxtItemChild(TextView txtItemChild) {
        this.txtItemChild = txtItemChild;
    }

    public ItemSubMenuHolder(View itemView) {
        super(itemView);
        txtItemChild = itemView.findViewById(R.id.txtItemChild);

    }

    public void bind(final ItemSubMenu itemSubMenu, final ExpandableGroup group, final int childIndex){
        txtItemChild.setText(itemSubMenu.name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener!=null){
                    mlistener.itemClick(group.getTitle(),childIndex);
                }
            }
        });

    }
}
