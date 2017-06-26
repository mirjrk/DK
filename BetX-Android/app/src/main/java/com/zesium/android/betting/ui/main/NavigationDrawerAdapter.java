package com.zesium.android.betting.ui.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.NavigationDrawerItem;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for navigation drawer items.
 * Created by Ivan Panic on 12/16/2015.
 */
class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private List<NavigationDrawerItem> data = Collections.emptyList();
    private final LayoutInflater inflater;
    private final Context context;

    NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    void addNewItemToDrawer(NavigationDrawerItem newItem) {
        data.add(newItem);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavigationDrawerItem current = data.get(position);
        holder.tvNavDrawerTitle.setText(current.getTitle());
        holder.imgNavDrawer.setImageDrawable(ContextCompat.getDrawable(context, current.getIcon()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final SFFontTextView tvNavDrawerTitle;
        final ImageView imgNavDrawer;

        MyViewHolder(View itemView) {
            super(itemView);
            tvNavDrawerTitle = (SFFontTextView) itemView.findViewById(R.id.tv_nav_drawer_title);
            imgNavDrawer = (ImageView) itemView.findViewById(R.id.ic_nav_drawer);
        }
    }
}