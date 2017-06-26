package com.zesium.android.betting.model.configuration;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class ShopConfiguration {
    private boolean ItemOrderingEnabled;
    private boolean OrderListEnabled;

    public boolean isItemOrderingEnabled() {
        return ItemOrderingEnabled;
    }

    public void setItemOrderingEnabled(boolean itemOrderingEnabled) {
        ItemOrderingEnabled = itemOrderingEnabled;
    }

    public boolean isOrderListEnabled() {
        return OrderListEnabled;
    }

    public void setOrderListEnabled(boolean orderListEnabled) {
        OrderListEnabled = orderListEnabled;
    }
}
