package com.tradeapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradeapplication.DetailPage;
import com.tradeapplication.R;
import com.tradeapplication.responses.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MoreRowView> {
    private ArrayList<Order> moreList;
    private DetailPage activityReference;

    class MoreRowView extends RecyclerView.ViewHolder {
        private TextView type, rate, Qty;

        MoreRowView(View view) {
            super(view);
            type = view.findViewById(R.id.typeItem);
            rate = view.findViewById(R.id.rateItem);
            Qty = view.findViewById(R.id.qtyItem);

        }
    }

    public OrderListAdapter(ArrayList<Order> moreList, DetailPage activityRef) {
        this.moreList = moreList;
        this.activityReference = activityRef;
    }

    @NonNull
    @Override
    public MoreRowView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);
        return new MoreRowView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoreRowView holder, int position) {
        Order more = moreList.get(position);
        System.out.println(more.getType());
        holder.type.setText(more.getType().toUpperCase());
        if (more.getType().toUpperCase().equals("BUY"))
            holder.type.setTextColor(activityReference.getResources().getColor(R.color.colorPrimary));
        else
            holder.type.setTextColor(activityReference.getResources().getColor(R.color.colorAccent));
        holder.rate.setText(Html.fromHtml(activityReference.getResources().getString(R.string.RswithPlus) + more.getRate()));
        holder.Qty.setText(Html.fromHtml(more.getQty() + " " + activityReference.returnSymbol()));

    }

    @Override
    public int getItemCount() {
        return moreList.size();
    }

}