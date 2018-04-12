package com.ssdut.houlx.secondhandtransactionsystem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author houlx
 */
public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.ViewHolder> {
    private List<Commodity> mCommodityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView commodityName;

        ViewHolder(View view) {
            super(view);
            commodityName = view.findViewById(R.id.textView_commodity_name);
        }
    }

    CommodityAdapter(List<Commodity> commodityList) {
        mCommodityList = commodityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commodity commodity = mCommodityList.get(position);
        holder.commodityName.setText(commodity.getName());
    }

    @Override
    public int getItemCount() {
        return mCommodityList.size();
    }
}
