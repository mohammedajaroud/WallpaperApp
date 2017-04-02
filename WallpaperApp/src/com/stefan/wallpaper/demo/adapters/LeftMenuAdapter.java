package com.stefan.wallpaper.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stefan.wallpaper.demo.BaseActivity;
import com.stefan.wallpaper.demo.R;
import com.stefan.wallpaper.demo.models.Category;

import java.util.List;

public class LeftMenuAdapter extends BaseAdapter {

    private List<Category> mCategories;
    private LayoutInflater mInflater;

    public LeftMenuAdapter(Context context, List<Category> categories) {
        mCategories = categories;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCategories.size() + 3; // + Recent and Favourites
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public View divider;
        public View separator;
        public TextView label;
        public TextView categoriesLabel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.divider = convertView
                    .findViewById(R.id.drawer_list_item_divider);
            holder.separator = convertView
                    .findViewById(R.id.drawer_list_separator);
            holder.label = (TextView) convertView
                    .findViewById(R.id.drawer_list_item_text_label);
            holder.categoriesLabel = (TextView) convertView
                    .findViewById(R.id.drawer_list_categories_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < 3) {
            if (position == 0) {
                holder.label.setText(R.string.recent);
                holder.categoriesLabel.setVisibility(View.GONE);
                holder.divider.setVisibility(View.GONE);
                holder.separator.setVisibility(View.GONE);
            } else if (position == 1) {
                holder.label.setText(R.string.favourites);
                holder.categoriesLabel.setVisibility(View.GONE);
                holder.divider.setVisibility(View.GONE);
                holder.separator.setVisibility(View.GONE);
            } else if (position == 2) {
                holder.label.setText(R.string.about_us);
                holder.categoriesLabel.setVisibility(View.VISIBLE);
                holder.divider.setVisibility(View.GONE);
                holder.separator.setVisibility(View.VISIBLE);
            }
        } else {
            Category category = mCategories.get(position - 3);
            holder.label.setText(category.getName());
            holder.divider.setVisibility(View.VISIBLE);
            holder.separator.setVisibility(View.GONE);
            holder.categoriesLabel.setVisibility(View.GONE);
        }
        holder.label.setTypeface(BaseActivity.sRobotoLight);
        holder.categoriesLabel.setTypeface(BaseActivity.sRobotoBlack);

        return convertView;
    }
}
