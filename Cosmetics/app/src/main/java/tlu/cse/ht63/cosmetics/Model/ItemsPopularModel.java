package tlu.cse.ht63.cosmetics.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsPopularModel implements Serializable {
    private String title;
    private ArrayList<String> picUrl;
    private double price;

    public ItemsPopularModel(){

    }

    public ItemsPopularModel(String title, ArrayList<String> picUrl, double price) {
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
