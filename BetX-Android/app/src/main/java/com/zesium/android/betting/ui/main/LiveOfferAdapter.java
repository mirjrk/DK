package com.zesium.android.betting.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.best_offer.BestOfferItem;

import java.util.ArrayList;

/**
 * Adapter for live match items.
 * Created by simikic on 5/5/2017.
 */
class LiveOfferAdapter extends RecyclerView.Adapter<LiveOfferViewHolder> {

    private final IFloatingTicket mTicketInterface;
    private final ArrayList<BestOfferItem> mData;

    LiveOfferAdapter(ArrayList<BestOfferItem> adapterData, IFloatingTicket ticketInterface) {
        mData = adapterData;
        mTicketInterface = ticketInterface;
    }

    @Override
    public LiveOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_live_betting, parent, false);
        return new LiveOfferViewHolder(view, mTicketInterface);
    }

    @Override
    public void onBindViewHolder(LiveOfferViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * Search for adapter item matching match id.
     *
     * @param matchId - Unique match id,
     * @return - Adapter item.
     */
    BestOfferItem getItemByMatchID(Integer matchId) {
        BestOfferItem retItem = null;
        for (BestOfferItem item : mData) {
            if (item.getMatch().getId() == matchId) {
                retItem = item;
                break;
            }
        }
        return retItem;
    }

    /**
     * Search item index by match id.
     *
     * @param matchId - Unique match id,
     * @return - Item index or -1 if there is not match with desired matchId.
     */
    int getIndexByMatchID(int matchId) {
        int retIndex = -1;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMatch().getId() == matchId) {
                retIndex = i;
                break;
            }
        }
        return retIndex;
    }
}