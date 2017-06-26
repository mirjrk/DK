package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 7/1/2016.
 */
public class Region {
    private int Id;
    private String Name;
    private int CountryId;
    private int TimeStamp;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public int getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        TimeStamp = timeStamp;
    }
}
