package com.zesium.android.betting.ui.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.best_offer.BestOfferItem;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.Odd;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.util.DateAndTimeHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.SportsBettingHelper;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.ui.widgets.SportBetRadioButton;

import org.apache.commons.lang3.SerializationUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Adapter for best offer list of matches.
 * Created by Ivan Panic on 12/16/2015.
 */
class BestOfferRecyclerAdapter extends RecyclerView.Adapter<BestOfferRecyclerAdapter.DataObjectHolder> {
    private static final String TAG = BestOfferRecyclerAdapter.class.getSimpleName();

    private final ArrayList<BestOfferItem> mBestOfferItems;
    private final Context mContext;
    private final IFloatingTicket mTicketInterface;
    private static final int THREE_ITEMS = 2;
    private static final int TWO_ITEMS = 1;
    private static final int ONE_ITEM = 0;
    private NumberFormat mNumberFormat;

    /**
     * Object holder that holds all the data required for one best offer match.
     */
    static class DataObjectHolder extends RecyclerView.ViewHolder {
        final SFFontTextView teamHome;
        final SFFontTextView teamAway;
        final SFFontTextView dateTime;
        final RadioGroup radioGroup;
        final CardView bestOfferItem;

        DataObjectHolder(View itemView) {
            super(itemView);
            teamHome = (SFFontTextView) itemView.findViewById(R.id.best_offer_game_home);
            teamAway = (SFFontTextView) itemView.findViewById(R.id.best_offer_game_away);
            dateTime = (SFFontTextView) itemView.findViewById(R.id.best_offer_date);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.rg_fix);
            bestOfferItem = (CardView) itemView.findViewById(R.id.cv_best_offer_item);
        }
    }

    BestOfferRecyclerAdapter(Context context, ArrayList<BestOfferItem> bestOfferItems, IFloatingTicket floatingTicket) {
        mContext = context;
        mBestOfferItems = bestOfferItems;
        mTicketInterface = floatingTicket;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBestOfferItems.get(position).getMatch().getOffer().getOdds().size() == 3) {
            return THREE_ITEMS;
        } else if (mBestOfferItems.get(position).getMatch().getOffer().getOdds().size() == 2) {
            return TWO_ITEMS;
        } else {
            return ONE_ITEM;
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_best_offer, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        int itemType = getItemViewType(position);

        BestOfferItem item = mBestOfferItems.get(position);

        initialiseMatchDetails(holder, item);


        if (itemType == THREE_ITEMS) {
            initialiseOffer(holder, item, R.layout.horizontal_sports_bet_layout);
        } else if (itemType == TWO_ITEMS) {
            initialiseOffer(holder, item, R.layout.horizontal_sport_bet_layout_two_items);
        } else if (itemType == ONE_ITEM) {
            initialiseOffer(holder, item, R.layout.horizontal_sport_bet_layout_one_item);
        }
    }

    /**
     * Method initialiseOffer initialises view which represents odds with name and value.
     *
     * @param holder    holder which contains views,
     * @param item      item which contains all data for one match,
     * @param offerView resource of layout which will be used based on odds size.
     */
    private synchronized void initialiseOffer(DataObjectHolder holder, BestOfferItem item, int offerView) {
        if (holder.radioGroup.getChildCount() == 0) {
            createOfferView(holder, item, offerView);
        } else {
            fillOfferView(holder, item);
        }
    }

    /**
     * Method createOfferView creates offer view for one match based on odds size.
     *
     * @param holder    holder which contains views,
     * @param item      item which contains all data for one match,
     * @param offerView resource of layout which will be used based on odds size.
     */
    private void createOfferView(DataObjectHolder holder, BestOfferItem item, int offerView) {

        // Initialise number format for money texts
        mNumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        mNumberFormat.setMinimumFractionDigits(2);
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setGroupingUsed(true);
        int matchId = item.getMatch().getId();

        for (int i = 0; i < item.getMatch().getOffer().getOdds().size(); i++) {

            final Odd odd = item.getMatch().getOffer().getOdds().get(i);

            final SportBetRadioButton rb = new SportBetRadioButton(mContext, offerView);
            rb.setText1(odd.getName());

            rb.setText2(String.valueOf(odd.getOdd()));

            holder.radioGroup.addView(rb.getView());

            SFFontTextView sf1 = (SFFontTextView) rb.getView().findViewById(R.id.textView1);
            SFFontTextView sf2 = (SFFontTextView) rb.getView().findViewById(R.id.textView2);

            if (item.getButtonSelected() == i) {
                rb.getRb().setChecked(true);
                sf1.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));
                sf2.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));
            } else {
                rb.getRb().setChecked(false);
                sf1.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
                sf2.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
            }

            setClickListener(rb.getView(), item);

            if (BetXApplication.getApp().getSelectedBetIds().get(matchId) != null) {
                if (item.isDisabled()) {
                    disableOrEnableRelativeLayout(i, holder.radioGroup, false, rb.getRb());

                } else {
                    disableOrEnableRelativeLayout(i, holder.radioGroup, true, rb.getRb());

                }
            } else {
                for (int z = 0; z < holder.radioGroup.getChildCount(); z++) {
                    disableOrEnableRelativeLayout(z, holder.radioGroup, true, rb.getRb());
                }
            }
        }
    }

    /**
     * Method fillOfferView fills view with new match details.
     *
     * @param holder holder which contains views,
     * @param item   item which contains all data for one match.
     */
    private void fillOfferView(DataObjectHolder holder, BestOfferItem item) {

        // Initialise number format for money texts
        mNumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        mNumberFormat.setMinimumFractionDigits(2);
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setGroupingUsed(true);
        int matchId = item.getMatch().getId();

        for (int i = 0; i < item.getMatch().getOffer().getOdds().size(); i++) {
            View view = holder.radioGroup.getChildAt(i);

            SFFontTextView sf1 = (SFFontTextView) view.findViewById(R.id.textView1);
            sf1.setText(item.getMatch().getOffer().getOdds().get(i).getName());
            SFFontTextView sf2 = (SFFontTextView) view.findViewById(R.id.textView2);

            sf2.setText(String.valueOf(item.getMatch().getOffer().getOdds().get(i).getOdd()));

            final RadioButton rb = (RadioButton) view.findViewById(R.id.radioButton1);

            if (i == item.getButtonSelected()) {
                rb.setChecked(true);
                sf1.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));
                sf2.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));
            } else {
                rb.setChecked(false);
                sf1.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
                sf2.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
            }

            if (BetXApplication.getApp().getSelectedBetIds().get(matchId) != null) {

                if (item.isDisabled()) {
                    disableOrEnableRelativeLayout(i, holder.radioGroup, false, rb);
                } else {
                    disableOrEnableRelativeLayout(i, holder.radioGroup, true, rb);
                }
            } else {
                disableOrEnableRelativeLayout(i, holder.radioGroup, true, rb);
            }

            setClickListener(view, item);
        }
    }

    /**
     * Method initialiseMatchDetails initializes basic match data.
     *
     * @param holder holder which contains views,
     * @param item   item which contains all data for one match.
     */
    private synchronized void initialiseMatchDetails(DataObjectHolder holder, final BestOfferItem item) {
        holder.bestOfferItem.setOnClickListener(view -> Toast.makeText(mContext, "Disabled", Toast.LENGTH_SHORT).show());

        setLeagueNameAndDate(holder, item);
        item.setButtonSelected(SportsBettingHelper.findPreviouslySelectedOdd(item.getMatch().getId(), item.getMatch().getOffer().getId()));
    }

    /**
     * Set league name and date for match.
     *
     * @param holder holder which contains views,
     * @param item   item which contains all data for one match.
     */
    private synchronized void setLeagueNameAndDate(DataObjectHolder holder, BestOfferItem item) {
        String leagueName = item.getLeagueName();

        if (leagueName.length() > 15) {
            leagueName = leagueName.substring(0, 15) + "..., ";
        } else {
            leagueName += ", ";
        }

        // Fill data about match from returned item
        holder.teamAway.setText(item.getMatch().getTeamAway());
        holder.teamHome.setText(item.getMatch().getTeamHome());

        // Parse and fill date and time data for match
        String dateAndTime = "";
        try {
            Date receivedDate = DateAndTimeHelper.getReceivedFormat().parse(item.getMatch().getMatchStartTime());
            String localDate = DateAndTimeHelper.getLocalDateFormat().format(receivedDate);
            Date localDateValue = DateAndTimeHelper.getLocalDateFormat().parse(localDate);
            dateAndTime = DateAndTimeHelper.DATE_VALUE_FORMAT.format(localDateValue) + " " + DateAndTimeHelper.TIME_VALUE_FORMAT.format(localDateValue);
        } catch (ParseException e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        }
        String leagueAndDateAndTime = leagueName + dateAndTime;
        holder.dateTime.setText(leagueAndDateAndTime);
    }

    /**
     * Set click listener is used for every odd to handle select/deselect of it and to store/delete
     * it from offer storage.
     *
     * @param view view which represent one odd,
     * @param item item which contains all data for one match.
     */
    private synchronized void setClickListener(View view, final BestOfferItem item) {

        view.setOnClickListener(view1 -> {
            RadioButton current = (RadioButton) view1.findViewById(R.id.radioButton1);
            boolean nextState = !current.isChecked();

            Match match = item.getMatch();

            RadioGroup lGroup = (RadioGroup) view1.getParent();
            if (lGroup != null) {
                if (item.getButtonSelected() >= 0) {
                    View parentView = lGroup.getChildAt(item.getButtonSelected());

                    if (parentView != null) {
                        RadioButton br = ((RadioButton) parentView.findViewById(R.id.radioButton1));
                        br.setChecked(false);

                        SFFontTextView sf1 = (SFFontTextView) parentView.findViewById(R.id.textView1);
                        sf1.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));

                        SFFontTextView sf2 = (SFFontTextView) parentView.findViewById(R.id.textView2);
                        sf2.setTextColor(ContextCompat.getColor(mContext, R.color.sports_betting_odd));
                    }
                }

                current.setChecked(nextState);
                final int selectedIndex;
                if (!nextState) {
                    SportsBettingHelper.removeOdd(match.getId(), match.getOffer().getId());

                    mTicketInterface.updateFloatingTicket();
                    selectedIndex = -1;
                } else {
                    selectedIndex = lGroup.indexOfChild(view1);
                    SFFontTextView sf1 = (SFFontTextView) view1.findViewById(R.id.textView1);
                    sf1.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));

                    SFFontTextView sf2 = (SFFontTextView) view1.findViewById(R.id.textView2);
                    sf2.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_subtitle_background));

                    SportsBettingHelper.addOdd(SerializationUtils.clone(match), selectedIndex);

                    mTicketInterface.updateFloatingTicket();
                }

                item.setButtonSelected(selectedIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBestOfferItems.size();
    }

    /**
     * Method that sets clicks on relative layout enabled or disabled.
     *
     * @param z           position in the list(radio group).
     * @param mRadioGroup is holding radio button that represents one odd.
     * @param enable      if it's true click is enabled, if it's false click is disabled.
     * @param rb          radio button that is enabled or disabled and colored.
     */
    private void disableOrEnableRelativeLayout(int z, RadioGroup mRadioGroup, boolean enable, RadioButton rb) {
        RelativeLayout rl = (RelativeLayout) mRadioGroup.getChildAt(z);

        rl.setEnabled(enable);
        if (enable) {
            if (Preferences.getTheme(mContext)) {
                rb.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rbtn_selector_dark));
            } else {
                rb.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rbtn_selector_white));
            }
        } else {
            rb.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_transparent_odd));
        }
    }
}