package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 7/1/2016.
 */
public class City {
    private int Id;
    private String TaxPercent;
    private String Name;
    private int RegionId;
    private int TimeStamp;
    private String CityAndZipCode;
    private String CountryCode;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTaxPercent() {
        return TaxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        TaxPercent = taxPercent;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRegionId() {
        return RegionId;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
    }

    public int getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getCityAndZipCode() {
        return CityAndZipCode;
    }

    public void setCityAndZipCode(String cityAndZipCode) {
        CityAndZipCode = cityAndZipCode;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }
}
