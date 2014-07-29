package com.osolve.thor.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.osolve.thor.R;
import com.osolve.thor.model.CoffeeShop;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends BaseAdapter {

    private static class ViewHolder {

        TextView name;
    }

    public ShopListAdapter() {
    }

    private CoffeeShop selectedShop;

    private List<CoffeeShop> shops = new ArrayList<>();

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public CoffeeShop getItem(final int position) {
        return shops.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final CoffeeShop shop = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.shopNameTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(shop.getName());
        return convertView;
    }

    public void reload(final List<CoffeeShop> newShops) {
        shops = newShops;
        notifyDataSetChanged();
    }

    public void setSelectedShop(final CoffeeShop shop) {
        selectedShop = shop;
        notifyDataSetChanged();
    }

    public void add(final CoffeeShop newShop) {
        if (shops.contains(newShop)) {
            return;
        }
        shops.add(newShop);
        notifyDataSetChanged();
    }

}
