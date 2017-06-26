package com.zesium.android.betting.model.sports;

import com.zesium.android.betting.model.sports.Category;

import java.io.Serializable;
import java.util.List;

/**
 * Sport class that has fields defined by response from server and contains all data for one sport.
 * Created by Ivan Panic on 12/18/2015.
 */
public class Sport implements Serializable{
    private int Id;
    private String Name;
    private String OriginName;
    private List<Category> Categories;

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

    public List<Category> getCategories() {
        return Categories;
    }

    public void setCategories(List<Category> categories) {
        Categories = categories;
    }

    public String getOriginName() {
        return OriginName;
    }

    public void setOriginName(String originName) {
        OriginName = originName;
    }
}
