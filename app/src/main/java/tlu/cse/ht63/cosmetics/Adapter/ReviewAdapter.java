package tlu.cse.ht63.cosmetics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;

import java.util.ArrayList;

import tlu.cse.ht63.cosmetics.Model.ReviewModel;
import tlu.cse.ht63.cosmetics.databinding.ViewholderReviewBinding;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Viewholder> {
    ArrayList<ReviewModel> items;
    Context context;

    public ReviewAdapter(ArrayList<ReviewModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ReviewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderReviewBinding binding = ViewholderReviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.Viewholder holder, int position) {
        holder.binding.nameTxt.setText(items.get(position).getName());
        holder.binding.descTxt.setText(items.get(position).getDescription());

        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .transform(new GranularRoundedCorners(100, 100, 100, 100))
                .into(holder.binding.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderReviewBinding binding;

        public Viewholder(ViewholderReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
