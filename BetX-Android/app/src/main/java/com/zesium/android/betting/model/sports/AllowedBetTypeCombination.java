package com.zesium.android.betting.model.sports;

import java.io.Serializable;

/**
 * Created by Ivan Panic_2 on 11/30/2016.
 */
public class AllowedBetTypeCombination implements Serializable {
    private String Id;
    private String Name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
