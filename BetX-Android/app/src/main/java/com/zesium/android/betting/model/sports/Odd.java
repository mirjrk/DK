package com.zesium.android.betting.model.sports;

import com.zesium.android.betting.model.sports.live.EChangeOddType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Odd class that has fields defined by response from server and contains odd data.
 * Created by Ivan Panic on 12/18/2015.
 */
public class Odd implements Serializable {
    private int Id;
    private String Name;
    private double Odd;
    private boolean isSelected;
    private String OrigName;
    private EChangeOddType changeOdd;
    private String CorrectionValue;
    private int mOfferId;

    private boolean Active;
    private String mBetTypeKey;

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

    public double getOdd() {
        double oneDecOdd = Odd;
        if (Odd > 10) {
            BigDecimal bd = new BigDecimal(Odd);
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            oneDecOdd = bd.doubleValue();
        }
        return oneDecOdd;
    }

    public void setOdd(double odd) {
        Odd = odd;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOrigName() {
        return OrigName;
    }

    public void setOrigName(String origName) {
        OrigName = origName;
    }

    public EChangeOddType getChangeOdd() {
        return changeOdd;
    }

    public void setChangeOdd(EChangeOddType changeOdd) {
        this.changeOdd = changeOdd;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getCorrectionValue() {
        return CorrectionValue;
    }

    public void setCorrectionValue(String correctionValue) {
        CorrectionValue = correctionValue;
    }

    public int getOfferId() {
        return mOfferId;
    }

    public void setOfferId(int offerId) {
        this.mOfferId = offerId;
    }

    public void setBetTypeKey(String betTypeKey) {
        this.mBetTypeKey = betTypeKey;
    }

    public String getBetTypeKey() {
        return mBetTypeKey;
    }
}

