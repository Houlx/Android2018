package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.Intent;
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
        View commodityView;

        ViewHolder(View view) {
            super(view);
            commodityView = view;
            commodityName = view.findViewById(R.id.textView_commodity_name);
        }
    }

    CommodityAdapter(List<Commodity> commodityList) {
        mCommodityList = commodityList;
    }

    public void setmCommodityList(List<Commodity> mCommodityList) {
        this.mCommodityList = mCommodityList;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.commodityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Commodity commodity = mCommodityList.get(position);
                Intent intent = new Intent(parent.getContext(), CommodityInformationActivity.class);
                intent.putExtra("commodity", commodity);
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
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
