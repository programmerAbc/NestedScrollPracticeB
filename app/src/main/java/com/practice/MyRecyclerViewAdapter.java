package com.practice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by usera on 2016/12/27.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    public static final int ITEM_COUNT=20;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
          if(position==0){
              holder.setText("START");
          }else if (position==ITEM_COUNT-1){
              holder.setText("END");
          }else{
              holder.setText("ITEM");
          }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTv;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemTv= (TextView) itemView.findViewById(R.id.itemTv);
        }
        public void setText(String str) {
            itemTv.setText(str);
        }
    }
}
