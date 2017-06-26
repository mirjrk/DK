package com.zesium.android.betting.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Country data model.
 * Created by zesium on 3/16/2017.
 */
public class Countries {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("BankForm")
    @Expose
    private Object bankForm;
    @SerializedName("AccForm")
    @Expose
    private Object accForm;
    @SerializedName("Sepa")
    @Expose
    private Boolean sepa;
    @SerializedName("Gemini")
    @Expose
    private Boolean gemini;
    @SerializedName("IsEnabled")
    @Expose
    private Boolean isEnabled;
    @SerializedName("CountryCallingCode")
    @Expose
    private Object countryCallingCode;
    @SerializedName("TimeStamp")
    @Expose
    private Integer timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Object getBankForm() {
        return bankForm;
    }

    public void setBankForm(Object bankForm) {
        this.bankForm = bankForm;
    }

    public Object getAccForm() {
        return accForm;
    }

    public void setAccForm(Object accForm) {
        this.accForm = accForm;
    }

    public Boolean getSepa() {
        return sepa;
    }

    public void setSepa(Boolean sepa) {
        this.sepa = sepa;
    }

    public Boolean getGemini() {
        return gemini;
    }

    public void setGemini(Boolean gemini) {
        this.gemini = gemini;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Object getCountryCallingCode() {
        return countryCallingCode;
    }

    public void setCountryCallingCode(Object countryCallingCode) {
        this.countryCallingCode = countryCallingCode;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

}