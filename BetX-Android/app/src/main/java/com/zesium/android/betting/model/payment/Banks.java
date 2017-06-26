package com.zesium.android.betting.model.payment;

/**
 * Created by zesium on 3/16/2017.
 */

public class Banks {
    private Integer Id;
    private String Name;
    private String Url;
    private String ShortName;
    private String BankNumber;
    private String SwiftOrBic;
    private Object IbanAccount;
    private Integer TimeStamp;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getBankNumber() {
        return BankNumber;
    }

    public void setBankNumber(String bankNumber) {
        BankNumber = bankNumber;
    }

    public String getSwiftOrBic() {
        return SwiftOrBic;
    }

    public void setSwiftOrBic(String swiftOrBic) {
        SwiftOrBic = swiftOrBic;
    }

    public Object getIbanAccount() {
        return IbanAccount;
    }

    public void setIbanAccount(Object ibanAccount) {
        IbanAccount = ibanAccount;
    }

    public Integer getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        TimeStamp = timeStamp;
    }
}
