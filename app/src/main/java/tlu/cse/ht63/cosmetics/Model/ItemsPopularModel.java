package tlu.cse.ht63.cosmetics.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsPopularModel implements Serializable {
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private int NumberInCart;

    public ItemsPopularModel() {
    }

    public ItemsPopularModel(String title, double price, int numberInCart) {
        this.title = title;
        this.price = price;
        this.NumberInCart = numberInCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getNumberInCart() {
        return NumberInCart;
    }

    public void setNumberInCart(int NumberInCart) {
        this.NumberInCart = NumberInCart;
    }
}
