package tlu.cse.ht63.cosmetics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import tlu.cse.ht63.cosmetics.Helper.ChangeNumberItemsListener;
import tlu.cse.ht63.cosmetics.Helper.ManagmentCart;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.databinding.ViewholderCartBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private List<ItemsPopularModel> listItemSelected;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(List<ItemsPopularModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ItemsPopularModel item = listItemSelected.get(position);
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(item.getNumberInCart() * item.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());
        Glide.with(holder.itemView.getContext()).load(item.getPicUrl().get(0))
                .apply(requestOptions).into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ArrayList<ItemsPopularModel> arrayList = new ArrayList<>(listItemSelected);
                    managmentCart.plusItem(arrayList, adapterPosition, () -> {
                        listItemSelected.clear();
                        listItemSelected.addAll(arrayList);
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    });
                }
            }
        });

        holder.binding.minusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ArrayList<ItemsPopularModel> arrayList = new ArrayList<>(listItemSelected);
                    managmentCart.minusItem(arrayList, adapterPosition, () -> {
                        listItemSelected.clear();
                        listItemSelected.addAll(arrayList);
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
