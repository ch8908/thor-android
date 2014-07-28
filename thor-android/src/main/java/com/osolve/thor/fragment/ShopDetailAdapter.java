package com.osolve.thor.fragment;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.osolve.thor.R;
import com.osolve.thor.model.ShopDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kros on 7/28/14.
 */
public class ShopDetailAdapter extends BaseAdapter {
    private final Context context;

    private List<Pair<String, String>> items = new ArrayList<>();

    public static class ViewHolder {
        TextView titleTextView;
        TextView subTitleTextView;
    }

    public ShopDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Pair<String, String> getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_information, null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolder.subTitleTextView = (TextView) convertView.findViewById(R.id.subTitleTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pair<String, String> item = items.get(position);

        viewHolder.titleTextView.setText(item.first);
        viewHolder.subTitleTextView.setText(item.second);

        return convertView;
    }

    public void reset(ShopDto shopDto) {
        List<Pair<String, String>> newItems = new ArrayList<>();
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_name_title), shopDto.getName()));
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_address), shopDto.getAddress()));
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_description), shopDto.getAddress()));
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_hours), shopDto.getHours()));
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_website_url), shopDto.getWebSiteUrl()));
        newItems.add(new Pair<>(getI18NString(R.string.shopDetail_avg_rating), String.valueOf(shopDto.getRating())));
        items = newItems;
        notifyDataSetChanged();
    }

    private String getI18NString(int resId) {
        return context.getResources().getString(resId);
    }
}

