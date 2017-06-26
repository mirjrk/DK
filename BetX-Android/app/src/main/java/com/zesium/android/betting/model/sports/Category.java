package com.zesium.android.betting.model.sports;

import java.io.Serializable;
import java.util.List;

/**
 * Category class that has fields defined by response from server.
 * Created by Ivan Panic on 12/17/2015.
 */
public class Category implements Serializable {
    private int Id;
    private List<League> Leagues;
    private String Name;
    private int Ordering;
    private int SpecialOrd;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<League> getLeagues() {
        return Leagues;
    }

    public void setLeagues(List<League> leagues) {
        Leagues = leagues;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getOrdering() {
        return Ordering;
    }

    public void setOrdering(int ordering) {
        Ordering = ordering;
    }

    public int getSpecialOrd() {
        return SpecialOrd;
    }

    public void setSpecialOrd(int specialOrd) {
        SpecialOrd = specialOrd;
    }
}
