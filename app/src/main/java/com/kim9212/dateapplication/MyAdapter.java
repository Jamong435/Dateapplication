package com.kim9212.dateapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Item> items;

    public MyAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.list_item,parent,false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VH vh=(VH)holder;
        Item item=items.get(position);
        vh.tv_time.setText(item.tv_time);
        vh.tv_where.setText(item.tv_where);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tv_time;
        TextView tv_where;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_where=itemView.findViewById(R.id.tv_where);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item=items.get(getLayoutPosition());
                    Intent intent= new Intent(context,NewPlanActivity.class);

                }
            });
        }
    }


    public static class Item {

        int tv_time;
        String tv_where;

        public Item(int tv_time, String tv_where) {
            this.tv_time = tv_time;
            this.tv_where = tv_where;
        }
    }

}
