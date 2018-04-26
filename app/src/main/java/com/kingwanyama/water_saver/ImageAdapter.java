package com.kingwanyama.water_saver;

/**
 * Created by kingwanyama on 10/10/17.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    ArrayList<String> img_links;

    public ImageAdapter(Context mContext, ArrayList<String> img_links) {
        this.mContext = mContext;
        this.img_links = img_links;
    }

    @Override
    public int getCount() {
        return img_links.size();
    }

    @Override
    public Object getItem(int position) {
        return img_links.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img_grid);
            viewHolder.imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String link = img_links.get(position);
        Picasso.with(mContext).load(link).into(viewHolder.imgView);
        return convertView;
    }

    static class ViewHolder {
        ImageView imgView;

    }

}