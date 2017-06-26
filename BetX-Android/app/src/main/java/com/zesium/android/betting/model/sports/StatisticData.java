package com.zesium.android.betting.model.sports;

import java.io.Serializable;

/**
 * Created by savic on 01-Sep-16.
 */
class StatisticData implements Serializable {
    private String HeaderName;
    private String Value;

    public String getHeaderName() {
        return HeaderName;
    }

    public void setHeaderName(String headerName) {
        HeaderName = headerName;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
