package com.ssdut.houlx.addrbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssdut.houlx.addrbook.db.Contact;

import java.util.List;

/**
 * @author houlx
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Contact> mContactList;
    private Context mContext;

    ContactAdapter(List<Contact> mContactList, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContactList = mContactList;
        this.mContext = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactTag;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.contactName = view.findViewById(R.id.contact_name);
        viewHolder.contactTag = view.findViewById(R.id.contact_tag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.contactTag.setVisibility(View.VISIBLE);
            holder.contactTag.setText(mContactList.get(position).getNameLetters());
        } else {
            holder.contactTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        holder.contactName.setText(this.mContactList.get(position).getName());

        holder.contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void updateList(List<Contact> list) {
        this.mContactList = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mContactList.get(position);
    }

    private int getSectionForPosition(int position) {
        return mContactList.get(position).getNameLetters().charAt(0);
    }

    private int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mContactList.get(i).getNameLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
