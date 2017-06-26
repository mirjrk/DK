package com.zesium.android.betting.model.util;

import com.zesium.android.betting.model.FavouriteLeague;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by Ivan Panic_2 on 9/19/2016.
 */
public class BetXDataStore implements Serializable {
    private final LinkedHashMap<Integer, FavouriteLeague> mFavouriteLeagues;

    public BetXDataStore() {
        mFavouriteLeagues = new LinkedHashMap<>();
    }

    public LinkedHashMap<Integer, FavouriteLeague> getFavouriteLeagues() {
        return mFavouriteLeagues;
    }
}
