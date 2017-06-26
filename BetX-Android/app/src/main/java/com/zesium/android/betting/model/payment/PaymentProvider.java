package com.zesium.android.betting.model.payment;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class PaymentProvider {
    private String Name;
    private String Description;
    private String DescriptionTransKey;
    private double MinDeposit;
    private double MaxDeposit;
    private int ProviderId;
    private String ProcessingDescription;
    private int PaymentProviderType;
    private boolean isSelected;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescriptionTransKey() {
        return DescriptionTransKey;
    }

    public void setDescriptionTransKey(String descriptionTransKey) {
        DescriptionTransKey = descriptionTransKey;
    }

    public double getMinDeposit() {
        return MinDeposit;
    }

    public void setMinDeposit(double minDeposit) {
        MinDeposit = minDeposit;
    }

    public double getMaxDeposit() {
        return MaxDeposit;
    }

    public void setMaxDeposit(double maxDeposit) {
        MaxDeposit = maxDeposit;
    }

    public int getProviderId() {
        return ProviderId;
    }

    public void setProviderId(int providerId) {
        ProviderId = providerId;
    }

    public String getProcessingDescription() {
        return ProcessingDescription;
    }

    public void setProcessingDescription(String processingDescription) {
        ProcessingDescription = processingDescription;
    }

    public int getPaymentProviderType() {
        return PaymentProviderType;
    }

    public void setPaymentProviderType(int paymentProviderType) {
        PaymentProviderType = paymentProviderType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
