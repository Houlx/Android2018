package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houlx
 */
public class TypeAdapter extends BaseAdapter {
    private List<String> types = new ArrayList<>();
    private Context mContext;

    TypeAdapter(Context context) {
        this.mContext = context;
    }

    public void setTypes(List<String> types) {
        this.types = types;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.commodity_type_spinner_item, null);
            holder.mTextView = (TextView) convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(types.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView mTextView;
    }
}
