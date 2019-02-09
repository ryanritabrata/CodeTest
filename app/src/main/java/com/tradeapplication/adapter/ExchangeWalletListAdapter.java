package com.tradeapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradeapplication.MainActivity;
import com.tradeapplication.R;
import com.tradeapplication.responses.WalletCoinModel;
import com.tradeapplication.utilities.CommonForAll;

import java.util.List;

public class ExchangeWalletListAdapter extends RecyclerView.Adapter<ExchangeWalletListAdapter.MoreRowView> {
    private List<WalletCoinModel> moreList;
    private MainActivity activityReference;

    class MoreRowView extends RecyclerView.ViewHolder {
        private TextView title, coinValue, changeIn24Hrs, coinName;
        private ImageView coinImage;

        MoreRowView(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            coinName = view.findViewById(R.id.coinName);
            coinImage = view.findViewById(R.id.coinImage);
            changeIn24Hrs = view.findViewById(R.id.changeIn24Hrs);
            coinValue = view.findViewById(R.id.coinValue);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityReference.moveToDetailPage(getAdapterPosition());
                }
            });
        }
    }

    public ExchangeWalletListAdapter(List<WalletCoinModel> moreList, MainActivity activityRef) {
        this.moreList = moreList;
        this.activityReference = activityRef;
    }

    @NonNull
    @Override
    public MoreRowView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_coin_row, parent, false);
        return new MoreRowView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreRowView holder, int position) {
        final WalletCoinModel more = moreList.get(position);

        holder.title.setText(more.getSymbol());
        Picasso.with(activityReference).load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + more.getRank() + ".png").into(holder.coinImage);

        holder.coinName.setText(more.getName());
        holder.coinValue.setText(Html.fromHtml(activityReference.getResources().getString(R.string.RswithPlus) + CommonForAll.changeRupeeFormatToIndiaFormatWithDot(CommonForAll.returnValueRoundedOffToTwoDigits(more.getPriceInr()))));

        if (more.getPercentChange24h() != null) {
            if (Double.parseDouble(more.getPercentChange24h()) == 0) {
                String changeVal = more.getPercentChange24h() + "%";
                holder.changeIn24Hrs.setText(changeVal);
                holder.changeIn24Hrs.setTextColor(activityReference.getResources().getColor(R.color.colorPrimary));
            } else if (Double.parseDouble(more.getPercentChange24h()) > 0) {
                String changeVal = "+" + more.getPercentChange24h() + "%";
                holder.changeIn24Hrs.setText(changeVal);
                holder.changeIn24Hrs.setTextColor(activityReference.getResources().getColor(R.color.colorPrimary));
            } else {
                String changeVal = more.getPercentChange24h() + "%";
                holder.changeIn24Hrs.setText(changeVal);
                holder.changeIn24Hrs.setTextColor(activityReference.getResources().getColor(R.color.colorAccent));
            }
        } else {
            String changeVal = "+" + "0.00" + "%";
            holder.changeIn24Hrs.setText(changeVal);
            holder.changeIn24Hrs.setTextColor(activityReference.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return moreList.size();
    }

}