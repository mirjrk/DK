package com.zesium.android.betting.model.payment;

import com.google.gson.JsonObject;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class ActionBaseResultWithObjectResult {
    private JsonObject Result;

    public JsonObject getResult() {
        return Result;
    }

    public void setResult(JsonObject result) {
        Result = result;
    }
}

