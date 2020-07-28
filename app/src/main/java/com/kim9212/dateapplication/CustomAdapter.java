package com.kim9212.dateapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<id_list> arrayList;
    private Context context;


    public CustomAdapter(ArrayList<id_list> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_age.setText(String.valueOf(arrayList.get(position).getAge()));
        holder.tv_gender.setText(arrayList.get(position).getGender());

    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_id;
        TextView tv_age;
        TextView tv_gender;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_age = itemView.findViewById(R.id.tv_age);
            this.tv_gender = itemView.findViewById(R.id.tv_gender);
        }
    }
}
