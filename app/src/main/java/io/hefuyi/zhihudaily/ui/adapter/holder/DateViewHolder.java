package io.hefuyi.zhihudaily.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.util.DateUtils;

/**
 * Created by hefuyi on 16/8/4.
 */
public class DateViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvDate;

    public DateViewHolder(View itemView) {
        super(itemView);
        tvDate = (TextView) itemView.findViewById(R.id.date);
    }

    public void bindDate(String date) {
        tvDate.setText(getDate(date, itemView.getContext()));
    }

    public static String getDate(String date, Context context) {
        if (DateUtils.isToday(date)) {
            return context.getResources().getString(R.string.recycler_item_main_today_hottest);
        } else {
            return DateUtils.getMainPageDate(date);
        }
    }
}
