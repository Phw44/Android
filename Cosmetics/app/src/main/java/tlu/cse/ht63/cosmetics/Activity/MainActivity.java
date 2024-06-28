package tlu.cse.ht63.cosmetics.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import tlu.cse.ht63.cosmetics.Adapter.PopularAdapter;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.R;

import tlu.cse.ht63.cosmetics.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private PopularAdapter adapter;
    private List<ItemsPopularModel> items;
    private List<ItemsPopularModel> originalItems;
    private DatabaseReference database;
    EditText edtSearch;
    TextView txtExplorer, txtCart, txtProfile, txtProducts;
    RecyclerView rvItemsProduct;
    ProgressBar progressBarItemsProduct;
    ImageView cartBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        originalItems = new ArrayList<>(); // Initialize the originalItems list
        adapter = new PopularAdapter((ArrayList<ItemsPopularModel>) items);

        database = FirebaseDatabase.getInstance().getReference();

        initUI();
        initListener();
        initItemsPopular();
        bottomNavigation();
    }

    private void bottomNavigation() {
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this
                , CartActivity.class)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this
                , ProfileActivity.class)));
    }

    private void initListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    adapter.updateList(originalItems); // Reset to original items
                }
            }
        });
    }

    private void initItemsPopular() {
        DatabaseReference myRef = database.child("Items");

        progressBarItemsProduct.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    items.clear();  // Clear the existing items
                    originalItems.clear(); // Clear the original items

                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsPopularModel item = issue.getValue(ItemsPopularModel.class);
                        if (item != null) {
                            items.add(item);
                            originalItems.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                }
                progressBarItemsProduct.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void filterItems(String query) {
        List<ItemsPopularModel> filteredList = new ArrayList<>();

        for (ItemsPopularModel item : originalItems) { // Sử dụng danh sách gốc để lọc
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }

    private void initUI() {
        rvItemsProduct = findViewById(R.id.rvItemsProduct);
        edtSearch = findViewById(R.id.edtSearch);
        txtExplorer = findViewById(R.id.txtExplorer);
        txtCart = findViewById(R.id.txtCart);
        txtProfile = findViewById(R.id.txtProfile);
        progressBarItemsProduct = findViewById(R.id.progressBarItemsProduct);
        txtProducts = findViewById(R.id.txtProducts);
        cartBtn = findViewById(R.id.cartBtn);
        profileBtn = findViewById(R.id.profileBtn);

        // Set up RecyclerView
        rvItemsProduct.setLayoutManager(new GridLayoutManager(this, 2));
        rvItemsProduct.setAdapter(adapter);
    }


    // chuyen doi ngon ngu
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.isEmpty()) {
            setLocale(language);
        }
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();

        updateTexts(); // Update UI texts directly instead of recreating the activity
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLocale(); // Kiểm tra và cập nhật ngôn ngữ mỗi khi MainActivity được khởi động lại
    }


    private void updateTexts() {
        String searchHint = getResources().getString(R.string.search_hint);
        edtSearch.setHint(searchHint);
        txtProducts.setText(R.string.txt_products);
        txtExplorer.setText(R.string.txt_explorer);
        txtCart.setText(R.string.txt_cart);
        txtProfile.setText(R.string.txt_profile);
    }

}