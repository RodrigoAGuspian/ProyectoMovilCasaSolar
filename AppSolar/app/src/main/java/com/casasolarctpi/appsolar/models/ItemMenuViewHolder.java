package com.casasolarctpi.appsolar.models;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ItemMenuViewHolder extends GroupViewHolder {
    TextView txtListHeader;
    private OnParentClickListener mlistener;
    ConstraintLayout constraintSelect;
    public interface OnParentClickListener{
        void itemClick(String groupP);
    }

    public void setMlistener(OnParentClickListener mlistener) {
        this.mlistener = mlistener;
    }

    public ItemMenuViewHolder(View itemView) {
        super(itemView);
        txtListHeader = itemView.findViewById(R.id.txtListHeader);
        constraintSelect = itemView.findViewById(R.id.constraintSelect);
    }


    public void bind(final ItemMenu itemMenu, final ExpandableGroup group){
        txtListHeader.setText(itemMenu.getTitle());
        if (itemMenu.getItems()==null){
            Log.e("asd",itemMenu.getTitle());
            constraintSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener!=null){
                        mlistener.itemClick(group.getTitle());
                    }

                }
            });
        }
    }

}
