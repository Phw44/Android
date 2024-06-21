package tlu.cse.ht63.cosmetics.Helper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItem(ItemsPopularModel item) {
        ArrayList<ItemsPopularModel> listItems = getListCart();
        boolean existAlready = false;
        int index = 0;

        for (ItemsPopularModel currentItem : listItems) {
            if (currentItem.getTitle().equals(item.getTitle())) {
                existAlready = true;
                currentItem.setNumberInCart(item.getNumberInCart());
                break;
            }
            index++;
        }

        if (!existAlready) {
            listItems.add(item);
        }

        tinyDB.putListObject("CartList", listItems);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsPopularModel> getListCart() {
        return (ArrayList<ItemsPopularModel>) tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemsPopularModel> listItems, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItems.get(position).getNumberInCart() == 1) {
            listItems.remove(position);
        } else {
            listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listItems);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsPopularModel> listItems, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItems);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemsPopularModel> listItems = getListCart();
        double fee = 0;

        for (ItemsPopularModel item : listItems) {
            fee += (item.getPrice() * item.getNumberInCart());
        }

        return fee;
    }
}
