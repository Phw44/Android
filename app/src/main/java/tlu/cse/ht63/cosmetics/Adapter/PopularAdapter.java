package tlu.cse.ht63.cosmetics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.R;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<ItemsPopularModel> items;
    Context context;

    public PopularAdapter(ArrayList<ItemsPopularModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_popular, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        ItemsPopularModel item = items.get(position);

        holder.title.setText(item.getTitle());
        holder.txtPrice.setText("$" + item.getPrice());

        Glide.with(context)
                .load(item.getPicUrl().get(0))
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title, txtPrice;
        ImageView imgProduct;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            txtPrice = itemView.findViewById((R.id.txtPrice));
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    public void updateList(List<ItemsPopularModel> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

}
