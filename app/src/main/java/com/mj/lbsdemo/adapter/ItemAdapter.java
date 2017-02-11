package com.mj.lbsdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mj.lbsdemo.R;
import com.mj.lbsdemo.activity.LocationActivity;
import com.mj.lbsdemo.activity.MapActivity;
import com.mj.lbsdemo.entity.Item;

import java.util.List;

/**
 * Created by MengJie on 2017/1/14.
 * ItemAdapter
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.getAdapterPosition()) {
                    case 0:
                        LocationActivity.actionStart(context);
                        break;
                    case 1:
                        MapActivity.actionStart(context);
                        break;
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvName.setText(item.getName());
        Glide.with(context).load(item.getImageId()).into(holder.ivItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView ivItem;
        TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            ivItem = (ImageView) cardView.findViewById(R.id.iv_item);
            tvName = (TextView) cardView.findViewById(R.id.tv_name);
        }
    }

}
