package com.zesium.android.betting.model.sports;

import java.util.List;

/**
 * LeagueOffer class that has fields defined by response from server.
 * Created by Ivan Panic on 12/21/2015.
 */
class LeagueOffer {
    private int Count;
    private List<Sport> Response;

    public List<Sport> getResponse() {
        return Response;
    }

    public void setResponse(List<Sport> response) {
        Response = response;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
