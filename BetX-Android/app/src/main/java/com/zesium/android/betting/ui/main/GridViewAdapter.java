package com.zesium.android.betting.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.GridViewItem;
import com.zesium.android.betting.model.sports.Offer;
import com.zesium.android.betting.model.sports.live.EChangeOddType;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.util.ArrayList;

/**
 * Adapter that is user for grid view items. One item represent one betting offer or one odd.
 * Created by Ivan Panic_2 on 8/31/2016.
 */
public class GridViewAdapter extends ArrayAdapter<GridViewItem> {
    private static final String TAG = GridViewAdapter.class.getSimpleName();
    private final Context mContext;
    private ArrayList<GridViewItem> data = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public GridViewAdapter(Context context, ArrayList<GridViewItem> data) {
        super(context, 0, data);
        this.mContext = context;
        this.data = data;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.textColor, typedValue, true);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item, parent, false);

            holder = new ViewHolder();
            holder.tvOddName = (SFFontTextView) convertView.findViewById(R.id.tv_odd_name);
            holder.tvOdd = (SFFontTextView) convertView.findViewById(R.id.tv_odd);
            holder.llOdd = (RelativeLayout) convertView.findViewById(R.id.ll_odd);
            holder.ivLock = (ImageView) convertView.findViewById(R.id.iv_lock);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridViewItem item = getItem(position);
        if (item != null && item.getOfferId() > 0) {
            boolean isSelected = false;
            if (BetXApplication.getApp().getSelectedBetIds().containsKey(item.getMatchId())) {
                for (Offer offer : BetXApplication.getApp().getSelectedBetIds().get(item.getMatchId()).getOffers())
                    if (offer.getId() == item.getOfferId()) {
                        if (offer.getOdds().get(0).getOrigName().equals(item.getOdd().getOrigName())) {
                            isSelected = true;
                            break;
                        }
                    }
            }
            item.setSelected(isSelected);

            // Simplify
            if (item.isSelected()) {
                holder.llOdd.setBackgroundColor(ContextCompat.getColor(mContext, R.color.credentials_circle_background));
                holder.tvOddName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.tvOdd.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            } else {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = mContext.getTheme();
                theme.resolveAttribute(R.attr.matchDetailsOddBackground, typedValue, true);
                int color = typedValue.data;

                holder.llOdd.setBackgroundColor(color);
                holder.tvOddName.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
                holder.tvOdd.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
            }

            holder.tvOddName.setText(item.getOdd().getName());

            String oddValue = "-";
            if (item.getOdd().getOdd() > 1) {
                oddValue = "" + item.getOdd().getOdd();
                holder.ivLock.setVisibility(View.GONE);
            } else {
                convertView.setEnabled(false);
                convertView.setOnClickListener(null);
                holder.ivLock.setVisibility(View.VISIBLE);
            }
            holder.tvOdd.setText(oddValue);

            if (!item.getOdd().isActive()) {
                convertView.setEnabled(false);
                convertView.setOnClickListener(null);
                holder.tvOdd.setVisibility(View.GONE);
                holder.ivLock.setVisibility(View.VISIBLE);
            } else {
                convertView.setEnabled(true);
                holder.tvOdd.setVisibility(View.VISIBLE);
                holder.ivLock.setVisibility(View.GONE);
            }

            int oddTextColor;
            if (item.getOdd().getChangeOdd() != null) {
                if (item.getOdd().getChangeOdd().equals(EChangeOddType.DECREASE)) {
                    oddTextColor = ContextCompat.getColor(mContext, R.color.red_status_ticket_lose);
                    holder.tvOdd.setTypeface(Typeface.DEFAULT_BOLD);
                    createHandlerForOddChangeTextColor(holder.tvOdd, item);
                } else if (item.getOdd().getChangeOdd().equals(EChangeOddType.INCREASE)) {
                    oddTextColor = ContextCompat.getColor(mContext, R.color.green_status_ticket_win);
                    holder.tvOdd.setTypeface(Typeface.DEFAULT_BOLD);
                    createHandlerForOddChangeTextColor(holder.tvOdd, item);
                } else {
                    oddTextColor = holder.tvOdd.getCurrentTextColor();
                }
            } else {
                oddTextColor = holder.tvOdd.getCurrentTextColor();
            }
            holder.tvOdd.setTextColor(oddTextColor);

            if (item.isDisabled()) {
                convertView.setOnClickListener(null);
            }
        } else {
            AppLogger.d(TAG, "Item has invalid offer id");

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = mContext.getTheme();
            theme.resolveAttribute(R.attr.matchDetailsOddBackground, typedValue, true);
            int color = typedValue.data;

            holder.llOdd.setBackgroundColor(color);

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            holder.tvOdd.setText("-");
        }
        return convertView;
    }

    /**
     * Method createHandlerForOddChangeTextColor sets text color of odd back to default after 3 seconds.
     * Odd text color is changed if odd has increased or decreased it's value, so this handler sets it to default.
     *
     * @param tvOdd text view which contains odd value,
     * @param item  grid view item with all data.
     */
    private void createHandlerForOddChangeTextColor(SFFontTextView tvOdd, GridViewItem item) {
        Handler h = new Handler();
        h.postDelayed(() -> {
            int oddColor;
            if (item.isSelected()) {
                oddColor = ContextCompat.getColor(mContext, R.color.black);
            } else {
                oddColor = ContextCompat.getColor(mContext, R.color.sports_betting_odd);
            }
            tvOdd.setTextColor(oddColor);
            item.getOdd().setChangeOdd(EChangeOddType.NONE);
            tvOdd.setTypeface(Typeface.DEFAULT);

        }, 3000);
    }

    private static class ViewHolder {
        SFFontTextView tvOddName;
        SFFontTextView tvOdd;
        RelativeLayout llOdd;
        ImageView ivLock;
    }

    public ArrayList<GridViewItem> getData() {
        return data;
    }

}
